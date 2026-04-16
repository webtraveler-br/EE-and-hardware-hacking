package com.roadmap.eehh

import android.app.Application
import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import java.util.concurrent.atomic.AtomicBoolean

class RoadmapApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onStart(owner: LifecycleOwner) {
                AppSessionRefreshManager.requestRefresh(this@RoadmapApp)
                AppAutoSyncManager.requestForegroundSync(this@RoadmapApp)
            }

            override fun onStop(owner: LifecycleOwner) {
                AppAutoSyncManager.requestBackgroundSync(this@RoadmapApp)
            }
        })
        DailyReminderScheduler.sync(this)
    }
}

internal object SyncExecutionGate {
    private val busy = AtomicBoolean(false)

    fun tryAcquire(): Boolean = busy.compareAndSet(false, true)

    fun release() {
        busy.set(false)
    }
}

internal object AppAutoSyncManager {
    private const val OFFLINE_SNAPSHOT_DECK_UID = "all"

    fun requestForegroundSync(context: Context) {
        requestSync(context.applicationContext, refreshSnapshot = true)
    }

    fun requestBackgroundSync(context: Context) {
        requestSync(context.applicationContext, refreshSnapshot = false)
    }

    private fun requestSync(context: Context, refreshSnapshot: Boolean) {
        if (!AppPreferences.isAutoSyncEnabled(context)) {
            return
        }
        if (AppPreferences.isSessionReauthRequired(context)) {
            return
        }
        val cookieHeader = AppPreferences.loadCookieHeader(context) ?: return
        if (!SyncExecutionGate.tryAcquire()) {
            return
        }

        Thread {
            try {
                val store = OfflineStore(context.filesDir)
                val pending = store.loadPendingEvents()
                val existingCards = store.loadCards()

                // If not refreshing snapshot, just do a quick import-only
                if (!refreshSnapshot || !AppPreferences.shouldRefreshSnapshotOnLaunch(context)) {
                    if (pending.isNotEmpty()) {
                        val importResult = OfflineApiClient.importPendingEvents(
                            baseUrl = BuildConfig.BASE_URL,
                            cookieHeader = cookieHeader,
                            events = pending
                        )
                        store.markEventsSynced(importResult.syncedEventIds)
                        if (importResult.deckSummaries.isNotEmpty()) {
                            store.saveDeckCatalog(importResult.deckSummaries)
                        }
                    }
                    return@Thread
                }

                // Unified sync: import + delta in one round trip
                val lastSyncAt = AppPreferences.loadLastSyncAt(context)
                val pushResult = OfflineApiClient.syncPush(
                    baseUrl = BuildConfig.BASE_URL,
                    cookieHeader = cookieHeader,
                    deckUid = OFFLINE_SNAPSHOT_DECK_UID,
                    events = pending,
                    lastSyncAt = if (existingCards.isEmpty()) null else lastSyncAt,
                    localCardCount = existingCards.size,
                )

                // Clear synced events
                if (pending.isNotEmpty()) {
                    val errorEventIds = mutableSetOf<String>()
                    // Events with errors stay pending
                    store.markEventsSynced(pending.map { it.eventId }.toSet() - errorEventIds)
                }

                // Apply delta
                if (existingCards.isEmpty() && pushResult.upsertCards.isNotEmpty()) {
                    // First sync — just save all cards
                    store.saveCards(pushResult.upsertCards)
                } else {
                    store.applyPushDelta(pushResult.upsertCards, pushResult.serverCardUids.toSet())
                }

                store.saveSchedulerProfile(pushResult.schedulerProfile)
                store.saveDeckCatalog(pushResult.deckSummaries)

                val finalCardCount = store.loadCards().size
                val refreshStatus = SnapshotRefreshStatus(
                    incremental = existingCards.isNotEmpty(),
                    upsertCount = pushResult.upsertCards.size,
                    removedCount = maxOf(0, existingCards.size + pushResult.upsertCards.size - finalCardCount),
                    unchangedCount = pushResult.unchangedCount,
                    cardCount = finalCardCount,
                    refreshedAtEpochMs = System.currentTimeMillis()
                )

                AppPreferences.saveSnapshotRefreshStatus(context, refreshStatus)
                if (pushResult.serverNow.isNotBlank()) {
                    AppPreferences.saveLastSyncAt(context, pushResult.serverNow)
                }
            } catch (exc: Exception) {
                if (exc.message == SESSION_REAUTH_REQUIRED_ERROR) {
                    AppPreferences.markSessionReauthRequired(context)
                }
                // Best effort only.
            } finally {
                SyncExecutionGate.release()
            }
        }.start()
    }
}
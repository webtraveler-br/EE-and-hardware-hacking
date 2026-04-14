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

                if (!refreshSnapshot || !AppPreferences.shouldRefreshSnapshotOnLaunch(context)) {
                    return@Thread
                }

                val existingCards = store.loadCards()
                val refreshStatus = if (existingCards.isEmpty()) {
                    val export = OfflineApiClient.exportCards(
                        baseUrl = BuildConfig.BASE_URL,
                        cookieHeader = cookieHeader,
                        deckUid = OFFLINE_SNAPSHOT_DECK_UID
                    )
                    store.saveCards(export.cards)
                    store.saveSchedulerProfile(export.schedulerProfile)
                    store.saveDeckCatalog(export.deckSummaries)
                    SnapshotRefreshStatus(
                        incremental = false,
                        upsertCount = export.cards.size,
                        removedCount = 0,
                        unchangedCount = 0,
                        cardCount = export.cards.size,
                        refreshedAtEpochMs = System.currentTimeMillis()
                    )
                } else {
                    val delta = OfflineApiClient.exportCardsDelta(
                        baseUrl = BuildConfig.BASE_URL,
                        cookieHeader = cookieHeader,
                        deckUid = OFFLINE_SNAPSHOT_DECK_UID,
                        localCards = existingCards
                    )
                    store.applyDelta(delta.upsertCards, delta.removedCardUids)
                    store.saveSchedulerProfile(delta.schedulerProfile)
                    store.saveDeckCatalog(delta.deckSummaries)
                    SnapshotRefreshStatus(
                        incremental = true,
                        upsertCount = delta.upsertCards.size,
                        removedCount = delta.removedCardUids.size,
                        unchangedCount = delta.unchangedCount,
                        cardCount = store.loadCards().size,
                        refreshedAtEpochMs = System.currentTimeMillis()
                    )
                }

                AppPreferences.saveSnapshotRefreshStatus(
                    context,
                    refreshStatus
                )
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
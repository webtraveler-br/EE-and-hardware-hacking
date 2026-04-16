package com.roadmap.eehh

import android.content.Context
import java.io.IOException
import java.net.SocketTimeoutException

data class OfflineLocalSummary(
    val pendingCount: Int,
    val cardCount: Int,
    val lastDeckUid: String
)

data class OfflineSnapshotResult(
    val cardCount: Int,
    val deckUid: String
)

data class OfflineManualSyncResult(
    val accepted: Int,
    val duplicates: Int,
    val errorCount: Int,
    val cardCount: Int,
    val deckUid: String
)

internal object OfflineDataManager {
    private const val OFFLINE_SNAPSHOT_DECK_UID = "all"

    fun readLocalSummary(context: Context): OfflineLocalSummary {
        val store = OfflineStore(context.filesDir)
        return OfflineLocalSummary(
            pendingCount = store.loadPendingEvents().size,
            cardCount = store.loadCards().size,
            lastDeckUid = AppPreferences.loadLastDeckUid(context)
        )
    }

    fun downloadSnapshotForLastDeck(context: Context): OfflineSnapshotResult {
        ensureSessionReadyForSync(context)
        return withSyncGate {
            val cookieHeader = AppPreferences.loadCookieHeader(context)
                ?: throw IOException(context.getString(R.string.offline_need_online_login))
            val store = OfflineStore(context.filesDir)
            val localCards = store.loadCards()
            val lastSyncAt = AppPreferences.loadLastSyncAt(context)
            try {
                val pushResult = OfflineApiClient.syncPush(
                    baseUrl = BuildConfig.BASE_URL,
                    cookieHeader = cookieHeader,
                    deckUid = OFFLINE_SNAPSHOT_DECK_UID,
                    events = emptyList(),
                    lastSyncAt = if (localCards.isEmpty()) null else lastSyncAt,
                    localCardCount = localCards.size,
                )

                if (localCards.isEmpty() && pushResult.upsertCards.isNotEmpty()) {
                    store.saveCards(pushResult.upsertCards)
                } else {
                    store.applyPushDelta(pushResult.upsertCards, pushResult.serverCardUids.toSet())
                }

                store.saveSchedulerProfile(pushResult.schedulerProfile)
                store.saveDeckCatalog(pushResult.deckSummaries)

                val finalCardCount = store.loadCards().size
                val refreshStatus = SnapshotRefreshStatus(
                    incremental = localCards.isNotEmpty(),
                    upsertCount = pushResult.upsertCards.size,
                    removedCount = maxOf(0, localCards.size + pushResult.upsertCards.size - finalCardCount),
                    unchangedCount = pushResult.unchangedCount,
                    cardCount = finalCardCount,
                    refreshedAtEpochMs = System.currentTimeMillis()
                )

                AppPreferences.markSessionHealthy(context)
                AppPreferences.saveSnapshotRefreshStatus(context, refreshStatus)
                if (pushResult.serverNow.isNotBlank()) {
                    AppPreferences.saveLastSyncAt(context, pushResult.serverNow)
                }
                OfflineSnapshotResult(cardCount = finalCardCount, deckUid = OFFLINE_SNAPSHOT_DECK_UID)
            } catch (exc: IOException) {
                throw handleSyncException(context, exc)
            }
        }
    }

    fun syncPendingAndRefreshLastDeck(context: Context): OfflineManualSyncResult {
        ensureSessionReadyForSync(context)
        return withSyncGate {
            val store = OfflineStore(context.filesDir)
            val pending = store.loadPendingEvents()
            val localCards = store.loadCards()
            val cookieHeader = AppPreferences.loadCookieHeader(context)
                ?: throw IOException(context.getString(R.string.offline_need_online_login))
            val lastSyncAt = AppPreferences.loadLastSyncAt(context)

            try {
                val pushResult = OfflineApiClient.syncPush(
                    baseUrl = BuildConfig.BASE_URL,
                    cookieHeader = cookieHeader,
                    deckUid = OFFLINE_SNAPSHOT_DECK_UID,
                    events = pending,
                    lastSyncAt = if (localCards.isEmpty()) null else lastSyncAt,
                    localCardCount = localCards.size,
                )

                // Mark all pending events as synced (errors stay as orphans but
                // server already recorded them as non-existent cards)
                if (pending.isNotEmpty()) {
                    store.markEventsSynced(pending.map { it.eventId }.toSet())
                }

                // Apply card delta
                if (localCards.isEmpty() && pushResult.upsertCards.isNotEmpty()) {
                    store.saveCards(pushResult.upsertCards)
                } else {
                    store.applyPushDelta(pushResult.upsertCards, pushResult.serverCardUids.toSet())
                }

                store.saveSchedulerProfile(pushResult.schedulerProfile)
                store.saveDeckCatalog(pushResult.deckSummaries)

                val finalCardCount = store.loadCards().size
                val refreshStatus = SnapshotRefreshStatus(
                    incremental = localCards.isNotEmpty(),
                    upsertCount = pushResult.upsertCards.size,
                    removedCount = maxOf(0, localCards.size + pushResult.upsertCards.size - finalCardCount),
                    unchangedCount = pushResult.unchangedCount,
                    cardCount = finalCardCount,
                    refreshedAtEpochMs = System.currentTimeMillis()
                )

                AppPreferences.markSessionHealthy(context)
                AppPreferences.saveSnapshotRefreshStatus(context, refreshStatus)
                if (pushResult.serverNow.isNotBlank()) {
                    AppPreferences.saveLastSyncAt(context, pushResult.serverNow)
                }

                OfflineManualSyncResult(
                    accepted = pushResult.accepted,
                    duplicates = pushResult.duplicates,
                    errorCount = pushResult.errorCount,
                    cardCount = finalCardCount,
                    deckUid = OFFLINE_SNAPSHOT_DECK_UID
                )
            } catch (exc: IOException) {
                throw handleSyncException(context, exc)
            }
        }
    }

    fun clearOfflineData(context: Context) {
        OfflineStore(context.filesDir).clearAllData()
        AppPreferences.saveLastDeckUid(context, "all")
        AppPreferences.clearSnapshotRefreshStatus(context)
        AppPreferences.clearLastSyncAt(context)
    }

    private fun ensureSessionReadyForSync(context: Context) {
        if (AppPreferences.isSessionReauthRequired(context)) {
            throw IOException(context.getString(R.string.settings_session_reauth_click_message))
        }
    }

    private fun handleSyncException(context: Context, exc: IOException): IOException {
        if (exc.message == SESSION_REAUTH_REQUIRED_ERROR) {
            AppPreferences.markSessionReauthRequired(context)
            return IOException(context.getString(R.string.settings_session_reauth_click_message))
        }
        return exc
    }

    private fun stageSyncException(
        context: Context,
        exc: IOException,
        stage: SyncStage,
    ): IOException {
        val normalized = handleSyncException(context, exc)
        if (normalized.message == context.getString(R.string.settings_session_reauth_click_message)) {
            return normalized
        }

        val timeoutLike = normalized is SocketTimeoutException ||
            normalized.message.orEmpty().contains("timed out", ignoreCase = true) ||
            normalized.message.orEmpty().contains("timeout", ignoreCase = true)

        return if (timeoutLike) {
            IOException(
                when (stage) {
                    SyncStage.IMPORT -> context.getString(R.string.offline_sync_timeout_import)
                    SyncStage.REFRESH -> context.getString(R.string.offline_sync_timeout_refresh)
                },
                normalized,
            )
        } else {
            IOException(
                when (stage) {
                    SyncStage.IMPORT -> context.getString(R.string.offline_sync_stage_import_failed)
                    SyncStage.REFRESH -> context.getString(R.string.offline_sync_stage_refresh_failed)
                },
                normalized,
            )
        }
    }

    private enum class SyncStage {
        IMPORT,
        REFRESH,
    }

    private fun <T> withSyncGate(block: () -> T): T {
        if (!SyncExecutionGate.tryAcquire()) {
            throw IOException("sync_in_progress")
        }

        try {
            return block()
        } finally {
            SyncExecutionGate.release()
        }
    }
}
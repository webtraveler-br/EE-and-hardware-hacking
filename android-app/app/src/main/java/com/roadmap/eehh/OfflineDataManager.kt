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
            try {
                val refreshStatus = if (localCards.isEmpty()) {
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
                        localCards = localCards
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
                AppPreferences.markSessionHealthy(context)
                AppPreferences.saveSnapshotRefreshStatus(context, refreshStatus)
                OfflineSnapshotResult(cardCount = refreshStatus.cardCount, deckUid = OFFLINE_SNAPSHOT_DECK_UID)
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
            val cookieHeader = AppPreferences.loadCookieHeader(context)
                ?: throw IOException(context.getString(R.string.offline_need_online_login))

            try {
                val importResult = try {
                    OfflineApiClient.importPendingEvents(
                        baseUrl = BuildConfig.BASE_URL,
                        cookieHeader = cookieHeader,
                        events = pending
                    )
                } catch (exc: IOException) {
                    throw stageSyncException(context, exc, SyncStage.IMPORT)
                }
                store.markEventsSynced(importResult.syncedEventIds)
                if (importResult.deckSummaries.isNotEmpty()) {
                    store.saveDeckCatalog(importResult.deckSummaries)
                }

                val localCards = store.loadCards()
                val refreshStatus = try {
                    if (localCards.isEmpty()) {
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
                            localCards = localCards
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
                } catch (exc: IOException) {
                    throw stageSyncException(context, exc, SyncStage.REFRESH)
                }
                AppPreferences.markSessionHealthy(context)
                AppPreferences.saveSnapshotRefreshStatus(context, refreshStatus)

                OfflineManualSyncResult(
                    accepted = importResult.accepted,
                    duplicates = importResult.duplicates,
                    errorCount = importResult.errorCount,
                    cardCount = refreshStatus.cardCount,
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
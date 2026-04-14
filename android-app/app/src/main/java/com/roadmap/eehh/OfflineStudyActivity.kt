package com.roadmap.eehh

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import io.noties.markwon.Markwon
import io.noties.markwon.inlineparser.MarkwonInlineParserPlugin
import io.noties.markwon.ext.latex.JLatexMathPlugin
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

private const val FSRS_STATE_LEARNING = 1
private const val FSRS_STATE_REVIEW = 2
private const val FSRS_STATE_RELEARNING = 3

data class DeckSummary(
    val deckUid: String,
    val title: String,
    val cardCount: Int,
    val dueCount: Int,
    val newCount: Int,
    val learningCount: Int,
) {
    fun toJson(): JSONObject {
        return JSONObject()
            .put("deck_uid", deckUid)
            .put("title", title)
            .put("card_count", cardCount)
            .put("due_count", dueCount)
            .put("new_count", newCount)
            .put("learning_count", learningCount)
    }

    companion object {
        fun fromJson(item: JSONObject): DeckSummary {
            return DeckSummary(
                deckUid = item.optString("deck_uid"),
                title = item.optString("title"),
                cardCount = item.optInt("card_count", 0),
                dueCount = item.optInt("due_count", 0),
                newCount = item.optInt("new_count", 0),
                learningCount = item.optInt("learning_count", 0),
            )
        }
    }
}

data class OfflineCard(
    val cardUid: String,
    val deckUid: String,
    val deckTitle: String,
    val questionMd: String,
    val answerMd: String,
    val contentHash: String,
    val position: Int,
    var dueAtEpochMs: Long?,
    var reps: Int,
    var lapses: Int,
    var isNew: Boolean,
    var fsrsState: Int?,
    var fsrsStep: Int?,
    var fsrsStability: Double?,
    var fsrsDifficulty: Double?,
    var lastReviewEpochMs: Long?,
) {
    fun toJson(): JSONObject {
        return JSONObject()
            .put("card_uid", cardUid)
            .put("deck_uid", deckUid)
            .put("deck_title", deckTitle)
            .put("question_md", questionMd)
            .put("answer_md", answerMd)
            .put("content_hash", contentHash)
            .put("position", position)
            .put("due_at_epoch_ms", dueAtEpochMs)
            .put("reps", reps)
            .put("lapses", lapses)
            .put("is_new", isNew)
            .put("fsrs_state", fsrsState)
            .put("fsrs_step", fsrsStep)
            .put("fsrs_stability", fsrsStability)
            .put("fsrs_difficulty", fsrsDifficulty)
            .put("last_review_epoch_ms", lastReviewEpochMs)
    }

    companion object {
        fun fromJson(item: JSONObject): OfflineCard {
            val due = if (item.isNull("due_at_epoch_ms")) null else item.optLong("due_at_epoch_ms")
            val fsrsState = if (item.isNull("fsrs_state")) null else item.optInt("fsrs_state")
            val fsrsStep = if (item.isNull("fsrs_step")) null else item.optInt("fsrs_step")
            val fsrsStability = if (item.isNull("fsrs_stability")) null else item.optDouble("fsrs_stability")
            val fsrsDifficulty = if (item.isNull("fsrs_difficulty")) null else item.optDouble("fsrs_difficulty")
            val lastReviewEpochMs = if (item.isNull("last_review_epoch_ms")) null else item.optLong("last_review_epoch_ms")
            return OfflineCard(
                cardUid = item.optString("card_uid"),
                deckUid = item.optString("deck_uid"),
                deckTitle = item.optString("deck_title"),
                questionMd = item.optString("question_md"),
                answerMd = item.optString("answer_md"),
                contentHash = item.optString("content_hash"),
                position = item.optInt("position", 0),
                dueAtEpochMs = due,
                reps = item.optInt("reps", 0),
                lapses = item.optInt("lapses", 0),
                isNew = item.optBoolean("is_new", true),
                fsrsState = fsrsState,
                fsrsStep = fsrsStep,
                fsrsStability = fsrsStability,
                fsrsDifficulty = fsrsDifficulty,
                lastReviewEpochMs = lastReviewEpochMs,
            )
        }
    }
}

data class PendingReviewEvent(
    val eventId: String,
    val cardUid: String,
    val rating: Int,
    val durationMs: Int?,
    val reviewedAt: String
) {
    fun toJson(): JSONObject {
        return JSONObject()
            .put("event_id", eventId)
            .put("card_uid", cardUid)
            .put("rating", rating)
            .put("duration_ms", durationMs)
            .put("reviewed_at", reviewedAt)
    }

    companion object {
        fun fromJson(item: JSONObject): PendingReviewEvent {
            val duration = if (item.isNull("duration_ms")) null else item.optInt("duration_ms")
            return PendingReviewEvent(
                eventId = item.optString("event_id"),
                cardUid = item.optString("card_uid"),
                rating = item.optInt("rating", 3),
                durationMs = duration,
                reviewedAt = item.optString("reviewed_at")
            )
        }
    }
}

data class SyncImportResult(
    val syncedEventIds: Set<String>,
    val accepted: Int,
    val duplicates: Int,
    val errorCount: Int,
    val deckSummaries: List<DeckSummary>,
)

data class SyncExportResult(
    val cards: List<OfflineCard>,
    val schedulerProfile: OfflineSchedulerProfile,
    val deckSummaries: List<DeckSummary>,
)

data class SyncDeltaResult(
    val upsertCards: List<OfflineCard>,
    val removedCardUids: Set<String>,
    val unchangedCount: Int,
    val schedulerProfile: OfflineSchedulerProfile,
    val deckSummaries: List<DeckSummary>,
)

internal class OfflineStore(private val rootDir: File) {
    private val cardsFile = File(rootDir, "offline_cards_v1.json")
    private val pendingFile = File(rootDir, "offline_pending_reviews_v1.json")
    private val schedulerProfileFile = File(rootDir, "offline_scheduler_profile_v1.json")
    private val deckCatalogFile = File(rootDir, "offline_deck_catalog_v1.json")

    fun loadCards(): MutableList<OfflineCard> {
        val items = mutableListOf<OfflineCard>()
        val array = readArray(cardsFile)
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            items.add(OfflineCard.fromJson(item))
        }
        return items
    }

    fun saveCards(cards: List<OfflineCard>) {
        val array = JSONArray()
        cards.forEach { array.put(it.toJson()) }
        writeArray(cardsFile, array)
    }

    fun loadDeckCatalog(): List<DeckSummary> {
        val items = mutableListOf<DeckSummary>()
        val array = readArray(deckCatalogFile)
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            items.add(DeckSummary.fromJson(item))
        }
        return items
    }

    fun saveDeckCatalog(catalog: List<DeckSummary>) {
        val array = JSONArray()
        catalog.forEach { array.put(it.toJson()) }
        writeArray(deckCatalogFile, array)
    }

    fun applyDelta(upsertCards: List<OfflineCard>, removedCardUids: Set<String>) {
        val current = loadCards()
        val byUid = linkedMapOf<String, OfflineCard>()
        current.forEach { card ->
            if (!removedCardUids.contains(card.cardUid)) {
                byUid[card.cardUid] = card
            }
        }
        upsertCards.forEach { card ->
            byUid[card.cardUid] = card
        }

        val merged = byUid.values.sortedWith(
            compareBy<OfflineCard>({ it.deckUid.lowercase(Locale.ROOT) }, { it.position }, { it.cardUid })
        )
        saveCards(merged)
    }

    fun loadSchedulerProfile(): OfflineSchedulerProfile {
        if (!schedulerProfileFile.exists()) {
            return DEFAULT_OFFLINE_SCHEDULER_PROFILE
        }

        val raw = schedulerProfileFile.readText().trim()
        if (raw.isBlank()) {
            return DEFAULT_OFFLINE_SCHEDULER_PROFILE
        }

        return try {
            OfflineSchedulerProfile.fromJson(JSONObject(raw))
        } catch (_: Exception) {
            DEFAULT_OFFLINE_SCHEDULER_PROFILE
        }
    }

    fun saveSchedulerProfile(profile: OfflineSchedulerProfile) {
        schedulerProfileFile.writeText(profile.toJson().toString())
    }

    fun loadPendingEvents(): MutableList<PendingReviewEvent> {
        val items = mutableListOf<PendingReviewEvent>()
        val array = readArray(pendingFile)
        for (i in 0 until array.length()) {
            val item = array.optJSONObject(i) ?: continue
            items.add(PendingReviewEvent.fromJson(item))
        }
        return items
    }

    fun appendPendingEvent(event: PendingReviewEvent) {
        val array = readArray(pendingFile)
        array.put(event.toJson())
        writeArray(pendingFile, array)
    }

    fun markEventsSynced(eventIds: Set<String>) {
        if (eventIds.isEmpty()) {
            return
        }

        val source = readArray(pendingFile)
        val target = JSONArray()
        for (i in 0 until source.length()) {
            val item = source.optJSONObject(i) ?: continue
            val eventId = item.optString("event_id")
            if (!eventIds.contains(eventId)) {
                target.put(item)
            }
        }
        writeArray(pendingFile, target)
    }

    fun clearAllData() {
        writeArray(cardsFile, JSONArray())
        writeArray(pendingFile, JSONArray())
        if (schedulerProfileFile.exists()) {
            schedulerProfileFile.delete()
        }
        if (deckCatalogFile.exists()) {
            deckCatalogFile.delete()
        }
    }

    private fun readArray(file: File): JSONArray {
        if (!file.exists()) {
            return JSONArray()
        }
        val raw = file.readText().trim()
        if (raw.isBlank()) {
            return JSONArray()
        }
        return try {
            JSONArray(raw)
        } catch (_: Exception) {
            JSONArray()
        }
    }

    private fun writeArray(file: File, array: JSONArray) {
        file.writeText(array.toString())
    }
}

internal object OfflineApiClient {
    private const val CONNECT_TIMEOUT_MS = 15000
    private const val DEFAULT_READ_TIMEOUT_MS = 20000
    private const val SYNC_READ_TIMEOUT_MS = 60000

    fun fetchDeckCatalog(baseUrl: String, cookieHeader: String): List<DeckSummary> {
        val endpoint = "${normalizeBaseUrl(baseUrl)}api/decks"
        val connection = openConnection(
            endpoint,
            "GET",
            cookieHeader,
            readTimeoutMs = DEFAULT_READ_TIMEOUT_MS,
        )

        try {
            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                if (code == 401 || code == 403) {
                    throw IOException(SESSION_REAUTH_REQUIRED_ERROR)
                }
                throw IOException("Falha ao carregar catalogo de decks ($code)")
            }

            val root = JSONObject(body)
            return parseDeckSummaries(root)
        } catch (exc: Exception) {
            throw NetworkErrorSupport.wrap(baseUrl, exc)
        } finally {
            connection.disconnect()
        }
    }

    fun exportCards(baseUrl: String, cookieHeader: String, deckUid: String): SyncExportResult {
        val encodedDeck = URLEncoder.encode(deckUid, StandardCharsets.UTF_8.toString())
        val endpoint = "${normalizeBaseUrl(baseUrl)}api/sync/export?deck_uid=$encodedDeck"
        val connection = openConnection(
            endpoint,
            "GET",
            cookieHeader,
            readTimeoutMs = SYNC_READ_TIMEOUT_MS,
        )

        try {
            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                if (code == 401 || code == 403) {
                    throw IOException(SESSION_REAUTH_REQUIRED_ERROR)
                }
                throw IOException("Falha ao baixar cards offline ($code)")
            }

            val root = JSONObject(body)
            val cardsArray = requireArray(root, "cards")
            val cards = mutableListOf<OfflineCard>()
            for (i in 0 until cardsArray.length()) {
                cards.add(parseOfflineCardPayload(requireObject(cardsArray, i)))
            }
            return SyncExportResult(
                cards = cards,
                schedulerProfile = OfflineSchedulerProfile.fromJson(
                    root.optJSONObject("scheduler_profile")
                ),
                deckSummaries = parseDeckSummaries(root),
            )
        } catch (exc: Exception) {
            throw NetworkErrorSupport.wrap(baseUrl, exc)
        } finally {
            connection.disconnect()
        }
    }

    fun exportCardsDelta(
        baseUrl: String,
        cookieHeader: String,
        deckUid: String,
        localCards: List<OfflineCard>
    ): SyncDeltaResult {
        val payload = JSONObject()
            .put("deck_uid", deckUid)
        val cardsArray = JSONArray()
        localCards.forEach { card ->
            cardsArray.put(
                JSONObject()
                    .put("card_uid", card.cardUid)
                    .put("content_hash", card.contentHash)
                    .put("deck_uid", card.deckUid)
                    .put("position", card.position)
                    .put("due_at", card.dueAtEpochMs?.let(::formatIsoUtcValue))
                    .put("reps", card.reps)
                    .put("lapses", card.lapses)
                    .put("is_new", card.isNew)
                    .put("fsrs_state", card.fsrsState)
                    .put("fsrs_step", card.fsrsStep)
                    .put("fsrs_stability", card.fsrsStability)
                    .put("fsrs_difficulty", card.fsrsDifficulty)
                    .put("last_review_at", card.lastReviewEpochMs?.let(::formatIsoUtcValue))
            )
        }
        payload.put("cards", cardsArray)

        val endpoint = "${normalizeBaseUrl(baseUrl)}api/sync/export-delta"
        val connection = openConnection(
            endpoint,
            "POST",
            cookieHeader,
            readTimeoutMs = SYNC_READ_TIMEOUT_MS,
        )
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            connection.outputStream.use { stream ->
                stream.write(payload.toString().toByteArray(StandardCharsets.UTF_8))
            }

            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                if (code == 401 || code == 403) {
                    throw IOException(SESSION_REAUTH_REQUIRED_ERROR)
                }
                throw IOException("Falha ao baixar delta offline ($code)")
            }

            val root = JSONObject(body)
            val upsertArray = requireArray(root, "upserts")
            val removedArray = requireArray(root, "removed_card_uids")
            val cards = mutableListOf<OfflineCard>()
            for (i in 0 until upsertArray.length()) {
                cards.add(parseOfflineCardPayload(requireObject(upsertArray, i)))
            }

            val removedCardUids = mutableSetOf<String>()
            for (i in 0 until removedArray.length()) {
                val item = removedArray.getString(i).trim()
                if (item.isNotEmpty()) {
                    removedCardUids.add(item)
                }
            }

            return SyncDeltaResult(
                upsertCards = cards,
                removedCardUids = removedCardUids,
                unchangedCount = root.optInt("unchanged_count", 0),
                schedulerProfile = OfflineSchedulerProfile.fromJson(
                    root.optJSONObject("scheduler_profile")
                ),
                deckSummaries = parseDeckSummaries(root),
            )
        } catch (exc: Exception) {
            throw NetworkErrorSupport.wrap(baseUrl, exc)
        } finally {
            connection.disconnect()
        }
    }

    fun importPendingEvents(
        baseUrl: String,
        cookieHeader: String,
        events: List<PendingReviewEvent>
    ): SyncImportResult {
        if (events.isEmpty()) {
            return SyncImportResult(emptySet(), 0, 0, 0, emptyList())
        }

        val payload = JSONObject()
        val eventsArray = JSONArray()
        events.forEach { eventsArray.put(it.toJson()) }
        payload.put("events", eventsArray)

        val endpoint = "${normalizeBaseUrl(baseUrl)}api/sync/import"
        val connection = openConnection(
            endpoint,
            "POST",
            cookieHeader,
            readTimeoutMs = SYNC_READ_TIMEOUT_MS,
        )
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            connection.outputStream.use { stream ->
                stream.write(payload.toString().toByteArray(StandardCharsets.UTF_8))
            }

            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                if (code == 401 || code == 403) {
                    throw IOException(SESSION_REAUTH_REQUIRED_ERROR)
                }
                throw IOException("Falha ao sincronizar ($code)")
            }

            val root = JSONObject(body)
            val errors = requireArray(root, "errors")
            val deckSummaries = parseDeckSummaries(root)
            val errorIds = mutableSetOf<String>()
            for (i in 0 until errors.length()) {
                val item = requireObject(errors, i)
                val eventId = item.optString("event_id", "").trim()
                if (eventId.isNotBlank()) {
                    errorIds.add(eventId)
                }
            }

            val synced = events.map { it.eventId }.toSet() - errorIds
            return SyncImportResult(
                syncedEventIds = synced,
                accepted = root.optInt("accepted", 0),
                duplicates = root.optInt("duplicates", 0),
                errorCount = errors.length(),
                deckSummaries = deckSummaries,
            )
        } catch (exc: Exception) {
            throw NetworkErrorSupport.wrap(baseUrl, exc)
        } finally {
            connection.disconnect()
        }
    }

    private fun openConnection(
        url: String,
        method: String,
        cookieHeader: String,
        readTimeoutMs: Int,
    ): HttpURLConnection {
        val connection = (URL(url).openConnection() as HttpURLConnection)
        connection.requestMethod = method
        connection.connectTimeout = CONNECT_TIMEOUT_MS
        connection.readTimeout = readTimeoutMs
        connection.setRequestProperty("Accept", "application/json")
        connection.setRequestProperty("Cookie", cookieHeader)
        connection.instanceFollowRedirects = true
        return connection
    }

    private fun readBody(connection: HttpURLConnection): String {
        val stream = when {
            connection.responseCode in 200..299 -> connection.inputStream
            connection.errorStream != null -> connection.errorStream
            else -> connection.inputStream
        }
        return stream.bufferedReader().use { it.readText() }
    }

    private fun normalizeBaseUrl(baseUrl: String): String {
        val trimmed = baseUrl.trim()
        return if (trimmed.endsWith("/")) trimmed else "$trimmed/"
    }

    private fun optNullableString(obj: JSONObject, key: String): String? {
        if (!obj.has(key) || obj.isNull(key)) {
            return null
        }
        val raw = obj.optString(key, "")
        return raw.ifBlank { null }
    }

    private fun requireArray(obj: JSONObject, key: String): JSONArray {
        if (!obj.has(key) || obj.isNull(key)) {
            throw IOException("Resposta invalida do servidor: campo '$key' ausente.")
        }
        return obj.getJSONArray(key)
    }

    private fun requireObject(array: JSONArray, index: Int): JSONObject {
        return array.getJSONObject(index)
    }

    private fun parseDeckSummaries(root: JSONObject): List<DeckSummary> {
        val deckArray = requireArray(root, "decks")
        val decks = mutableListOf<DeckSummary>()
        for (i in 0 until deckArray.length()) {
            decks.add(parseDeckSummary(requireObject(deckArray, i)))
        }
        return decks
    }

    private fun parseDeckSummary(item: JSONObject): DeckSummary {
        return DeckSummary(
            deckUid = requireString(item, "deck_uid"),
            title = requireString(item, "title"),
            cardCount = requireNonNegativeInt(item, "card_count"),
            dueCount = requireNonNegativeInt(item, "due_count"),
            newCount = requireNonNegativeInt(item, "new_count"),
            learningCount = if (item.has("learning_count") && !item.isNull("learning_count")) {
                requireNonNegativeInt(item, "learning_count")
            } else {
                0
            },
        )
    }

    private fun parseOfflineCardPayload(item: JSONObject): OfflineCard {
        val state = item.getJSONObject("state")
        val dueRaw = optNullableString(state, "due_at")
        val lastReviewRaw = optNullableString(state, "last_review_at")
        return OfflineCard(
            cardUid = requireString(item, "card_uid"),
            deckUid = requireString(item, "deck_uid"),
            deckTitle = requireString(item, "deck_title"),
            questionMd = requireString(item, "question_md"),
            answerMd = requireString(item, "answer_md"),
            contentHash = requireString(item, "content_hash"),
            position = requireNonNegativeInt(item, "position"),
            dueAtEpochMs = parseIsoToEpochMillis(dueRaw),
            reps = requireNonNegativeInt(state, "reps"),
            lapses = requireNonNegativeInt(state, "lapses"),
            isNew = requireBoolean(state, "is_new"),
            fsrsState = if (state.has("fsrs_state") && !state.isNull("fsrs_state")) state.getInt("fsrs_state") else null,
            fsrsStep = if (state.has("fsrs_step") && !state.isNull("fsrs_step")) state.getInt("fsrs_step") else null,
            fsrsStability = if (state.has("fsrs_stability") && !state.isNull("fsrs_stability")) state.getDouble("fsrs_stability") else null,
            fsrsDifficulty = if (state.has("fsrs_difficulty") && !state.isNull("fsrs_difficulty")) state.getDouble("fsrs_difficulty") else null,
            lastReviewEpochMs = parseIsoToEpochMillis(lastReviewRaw),
        )
    }

    private fun requireString(obj: JSONObject, key: String): String {
        if (!obj.has(key) || obj.isNull(key)) {
            throw IOException("Resposta invalida do servidor: campo '$key' ausente.")
        }
        val value = obj.getString(key).trim()
        if (value.isEmpty()) {
            throw IOException("Resposta invalida do servidor: campo '$key' vazio.")
        }
        return value
    }

    private fun requireBoolean(obj: JSONObject, key: String): Boolean {
        if (!obj.has(key) || obj.isNull(key)) {
            throw IOException("Resposta invalida do servidor: campo '$key' ausente.")
        }
        return obj.getBoolean(key)
    }

    private fun requireNonNegativeInt(obj: JSONObject, key: String): Int {
        if (!obj.has(key) || obj.isNull(key)) {
            throw IOException("Resposta invalida do servidor: campo '$key' ausente.")
        }
        val value = obj.getInt(key)
        if (value < 0) {
            throw IOException("Resposta invalida do servidor: campo '$key' invalido.")
        }
        return value
    }
}

class OfflineStudyActivity : AppCompatActivity() {

    private lateinit var store: OfflineStore
    private val cards = mutableListOf<OfflineCard>()
    private var serverDeckCatalog: List<DeckSummary> = emptyList()
    private var deckOptions: List<DeckSummary> = emptyList()
    private var selectedDeckUid: String = "all"
    private var suppressDeckSelection = false
    private var schedulerProfile: OfflineSchedulerProfile = DEFAULT_OFFLINE_SCHEDULER_PROFILE
    private var reviewMode = false

    private var currentCard: OfflineCard? = null
    private var revealStartedAtMs: Long = 0
    private var busy = false

    private lateinit var deckSpinner: Spinner
    private lateinit var statusView: TextView
    private lateinit var dueCountView: TextView
    private lateinit var newCountView: TextView
    private lateinit var wrongQueueCountView: TextView
    private lateinit var syncStatusView: TextView
    private lateinit var reviewIntroView: TextView
    private lateinit var reviewEntryGroup: View
    private lateinit var reviewContentGroup: View
    private lateinit var questionView: TextView
    private lateinit var answerView: TextView
    private lateinit var ratingGroup: View
    private lateinit var settingsButton: ImageButton
    private lateinit var markwon: Markwon
    private var latexRenderingEnabled = true

    private lateinit var startReviewButton: Button
    private lateinit var showAnswerButton: Button
    private lateinit var rate1Button: Button
    private lateinit var rate2Button: Button
    private lateinit var rate3Button: Button
    private lateinit var rate4Button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!AppPreferences.hasActiveSession(this)) {
            redirectToLogin()
            return
        }
        setContentView(R.layout.activity_offline_study)

        store = OfflineStore(filesDir)
        selectedDeckUid = AppPreferences.loadLastDeckUid(this)
        schedulerProfile = store.loadSchedulerProfile()
        serverDeckCatalog = store.loadDeckCatalog()
        cards.addAll(store.loadCards())

        deckSpinner = findViewById(R.id.offlineDeckSpinner)
        statusView = findViewById(R.id.offlineStatus)
        dueCountView = findViewById(R.id.offlineDueCountValue)
        newCountView = findViewById(R.id.offlineNewCountValue)
        wrongQueueCountView = findViewById(R.id.offlineWrongQueueCountValue)
        syncStatusView = findViewById(R.id.offlineSyncStatus)
        reviewIntroView = findViewById(R.id.offlineReviewIntro)
        reviewEntryGroup = findViewById(R.id.offlineReviewEntryGroup)
        reviewContentGroup = findViewById(R.id.offlineReviewContentGroup)
        questionView = findViewById(R.id.offlineQuestion)
        answerView = findViewById(R.id.offlineAnswer)
        ratingGroup = findViewById(R.id.offlineRatingGroup)
        settingsButton = findViewById(R.id.offlineSettingsButton)

        initMarkdownRenderer()

        startReviewButton = findViewById(R.id.offlineStartReviewButton)
        showAnswerButton = findViewById(R.id.offlineShowAnswerButton)
        rate1Button = findViewById(R.id.offlineRate1)
        rate2Button = findViewById(R.id.offlineRate2)
        rate3Button = findViewById(R.id.offlineRate3)
        rate4Button = findViewById(R.id.offlineRate4)

        configureDeckSelector()
        wireActions()
        refreshDeckOptions(keepSelection = false)
        fetchDeckCatalogAsync()
        renderState()
    }

    override fun onResume() {
        super.onResume()
        if (!AppPreferences.hasActiveSession(this)) {
            redirectToLogin()
            return
        }
        if (!busy) {
            reloadCardsFromStore()
            fetchDeckCatalogAsync()
        }
    }

    private fun configureDeckSelector() {
        deckSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (suppressDeckSelection) {
                    return
                }

                val option = deckOptions.getOrNull(position) ?: return
                if (selectedDeckUid == option.deckUid && !reviewMode) {
                    return
                }

                selectedDeckUid = option.deckUid
                AppPreferences.saveLastDeckUid(this@OfflineStudyActivity, selectedDeckUid)
                reviewMode = false
                currentCard = null
                answerView.visibility = View.GONE
                renderState()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Ignore.
            }
        }
    }

    private fun wireActions() {
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        startReviewButton.setOnClickListener {
            reviewMode = true
            currentCard = null
            answerView.visibility = View.GONE
            renderState()
        }

        showAnswerButton.setOnClickListener {
            revealAnswer()
        }

        rate1Button.setOnClickListener { rateCurrentCard(1) }
        rate2Button.setOnClickListener { rateCurrentCard(2) }
        rate3Button.setOnClickListener { rateCurrentCard(3) }
        rate4Button.setOnClickListener { rateCurrentCard(4) }
    }

    private fun reloadCardsFromStore() {
        schedulerProfile = store.loadSchedulerProfile()
        serverDeckCatalog = store.loadDeckCatalog()
        val storedCards = store.loadCards()
        cards.clear()
        cards.addAll(storedCards)
        refreshDeckOptions(keepSelection = true)
        if (!isCardInScope(currentCard, cardsForSelectedDeck())) {
            currentCard = null
            answerView.visibility = View.GONE
            reviewMode = false
        }
        renderState()
    }

    private fun fetchDeckCatalogAsync() {
        Thread {
            try {
                val cookieHeader = AppPreferences.loadCookieHeader(this) ?: return@Thread
                val catalog = OfflineApiClient.fetchDeckCatalog(
                    baseUrl = BuildConfig.BASE_URL,
                    cookieHeader = cookieHeader
                )

                runOnUiThread {
                    if (catalog.isNotEmpty()) {
                        store.saveDeckCatalog(catalog)
                        serverDeckCatalog = catalog
                        refreshDeckOptions(keepSelection = true)
                        renderState()
                    }
                }
            } catch (exc: Exception) {
                if (exc.message == SESSION_REAUTH_REQUIRED_ERROR) {
                    AppPreferences.markSessionReauthRequired(this)
                }
                // Best effort only.
            }
        }.start()
    }

    private fun refreshDeckOptions(keepSelection: Boolean) {
        val previousDeckUid = if (keepSelection) {
            selectedDeckUid
        } else {
            AppPreferences.loadLastDeckUid(this)
        }
        val hasPendingLocalChanges = store.loadPendingEvents().isNotEmpty()

        val options = when {
            serverDeckCatalog.isNotEmpty() && !hasPendingLocalChanges -> buildDeckOptionsFromCatalog(serverDeckCatalog)
            cards.isNotEmpty() -> buildDeckOptionsFromCards(cards)
            serverDeckCatalog.isNotEmpty() -> buildDeckOptionsFromCatalog(serverDeckCatalog)
            else -> listOf(
                DeckSummary(
                    deckUid = "all",
                    title = getString(R.string.all_decks),
                    cardCount = 0,
                    dueCount = 0,
                    newCount = 0,
                    learningCount = 0,
                )
            )
        }

        deckOptions = options
        val labels = options.map { option ->
            option.title
        }

        val adapter = ArrayAdapter(this, R.layout.spinner_item, labels)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        suppressDeckSelection = true
        deckSpinner.adapter = adapter

        val selectedIndex = options.indexOfFirst { it.deckUid == previousDeckUid }
            .takeIf { it >= 0 }
            ?: 0
        selectedDeckUid = options[selectedIndex].deckUid
        AppPreferences.saveLastDeckUid(this, selectedDeckUid)
        deckSpinner.setSelection(selectedIndex, false)
        suppressDeckSelection = false
    }

    private fun buildDeckOptionsFromCards(source: List<OfflineCard>): List<DeckSummary> {
        val now = System.currentTimeMillis()
        val perDeck = source
            .groupBy { it.deckUid }
            .map { (deckUid, deckCards) ->
                DeckSummary(
                    deckUid = deckUid,
                    title = deckCards.firstOrNull()?.deckTitle ?: deckUid,
                    cardCount = deckCards.size,
                    dueCount = deckCards.count { isDueCard(it, now) },
                    newCount = deckCards.count { it.isNew },
                    learningCount = countLearningCards(deckCards),
                )
            }
            .sortedBy { it.title.lowercase(Locale.ROOT) }

        val allSummary = DeckSummary(
            deckUid = "all",
            title = getString(R.string.all_decks),
            cardCount = source.size,
            dueCount = source.count { isDueCard(it, now) },
            newCount = source.count { it.isNew },
            learningCount = countLearningCards(source),
        )

        return listOf(allSummary) + perDeck
    }

    private fun buildDeckOptionsFromCatalog(catalog: List<DeckSummary>): List<DeckSummary> {
        val sorted = catalog.sortedBy { it.title.lowercase(Locale.ROOT) }
        val allSummary = DeckSummary(
            deckUid = "all",
            title = getString(R.string.all_decks),
            cardCount = sorted.sumOf { it.cardCount },
            dueCount = sorted.sumOf { it.dueCount },
            newCount = sorted.sumOf { it.newCount },
            learningCount = sorted.sumOf { it.learningCount },
        )

        return listOf(allSummary) + sorted
    }

    private fun countLearningCards(source: List<OfflineCard>): Int {
        return source.count { card ->
            !card.isNew &&
                card.dueAtEpochMs != null &&
                (effectiveFsrsState(card) == FSRS_STATE_LEARNING || effectiveFsrsState(card) == FSRS_STATE_RELEARNING)
        }
    }

    private fun effectiveFsrsState(card: OfflineCard): Int {
        return if (card.isNew) {
            FSRS_STATE_LEARNING
        } else {
            card.fsrsState ?: FSRS_STATE_REVIEW
        }
    }

    private fun computeNextInterval(card: OfflineCard, rating: Int): Long {
        val now = System.currentTimeMillis()
        val projection = OfflineFsrsScheduler.project(card, rating, schedulerProfile, now)
        return (projection.dueAtEpochMs - now).coerceAtLeast(0L)
    }

    private fun formatInterval(ms: Long): String = when {
        ms < 60L * 1000 -> "<1m"
        ms < 60L * 60 * 1000 -> "${ms / (60L * 1000)}m"
        ms < 24L * 60 * 60 * 1000 -> "${ms / (60L * 60 * 1000)}h"
        else -> "${ms / (24L * 60 * 60 * 1000)}d"
    }

    private fun rateCurrentCard(rating: Int) {
        val card = currentCard ?: run {
            toast("Nenhum card disponível.")
            return
        }

        val now = System.currentTimeMillis()
        val elapsed = (now - revealStartedAtMs).coerceIn(0L, 600_000L).toInt()
        val previousState = effectiveFsrsState(card)
        val projection = OfflineFsrsScheduler.project(card, rating, schedulerProfile, now)

        card.fsrsState = projection.state
        card.fsrsStep = projection.step
        card.fsrsStability = projection.stability
        card.fsrsDifficulty = projection.difficulty
        card.lastReviewEpochMs = projection.lastReviewEpochMs
        card.dueAtEpochMs = projection.dueAtEpochMs

        card.isNew = false
        card.reps += 1
        if (previousState == FSRS_STATE_REVIEW && rating == 1) {
            card.lapses += 1
        }

        val event = PendingReviewEvent(
            eventId = UUID.randomUUID().toString(),
            cardUid = card.cardUid,
            rating = rating,
            durationMs = elapsed,
            reviewedAt = formatIsoUtcValue(now)
        )

        store.appendPendingEvent(event)
        store.saveCards(cards)
        refreshDeckOptions(keepSelection = true)
        answerView.visibility = View.GONE
        currentCard = null

        renderState()
    }

    private fun renderState() {
        updateCounters()

        val scopedCards = cardsForSelectedDeck()
        val nextCard = pickNextCard(scopedCards)

        if (cards.isEmpty()) {
            reviewMode = false
            statusView.setText(R.string.offline_status_empty)
            renderReviewEntry(getString(R.string.offline_review_empty), false)
            resetReviewCardViews()
            return
        }

        if (scopedCards.isEmpty()) {
            reviewMode = false
            statusView.setText(R.string.offline_status_deck_empty)
            renderReviewEntry(getString(R.string.offline_review_deck_empty), false)
            resetReviewCardViews()
            return
        }

        if (!reviewMode) {
            statusView.text = buildSelectedDeckStatus(scopedCards.size)
            renderReviewEntry(buildReviewIntro(nextCard, scopedCards.size), nextCard != null)
            resetReviewCardViews()
            return
        }

        if (currentCard == null || !isCardInScope(currentCard, scopedCards)) {
            currentCard = nextCard
            revealStartedAtMs = System.currentTimeMillis()
        }

        val card = currentCard
        if (card == null) {
            reviewMode = false
            statusView.setText(R.string.offline_status_no_eligible)
            renderReviewEntry(getString(R.string.offline_review_no_eligible), false)
            resetReviewCardViews()
            return
        }

        reviewEntryGroup.visibility = View.GONE
        reviewContentGroup.visibility = View.VISIBLE
        statusView.text = buildStatus(card, scopedCards.size)
        renderMarkdown(questionView, card.questionMd)
        renderMarkdown(answerView, card.answerMd)
        answerView.visibility = View.GONE
        showAnswerButton.visibility = View.VISIBLE
        ratingGroup.visibility = View.GONE
        answerView.alpha = 1f
        answerView.translationY = 0f
        answerView.scaleX = 1f
        answerView.scaleY = 1f
    }

    private fun renderReviewEntry(message: String, enabled: Boolean) {
        reviewEntryGroup.visibility = View.VISIBLE
        reviewContentGroup.visibility = View.GONE
        reviewIntroView.text = message
        startReviewButton.isEnabled = enabled
        startReviewButton.text = if (selectedDeckUid == "all") {
            getString(R.string.offline_start_review_all)
        } else {
            getString(R.string.offline_start_review_selected)
        }
    }

    private fun resetReviewCardViews() {
        renderMarkdown(questionView, "-")
        renderMarkdown(answerView, "-")
        answerView.visibility = View.GONE
        showAnswerButton.visibility = View.GONE
        ratingGroup.visibility = View.GONE
        answerView.alpha = 1f
        answerView.translationY = 0f
        answerView.scaleX = 1f
        answerView.scaleY = 1f
    }

    private fun cardsForSelectedDeck(): List<OfflineCard> {
        return if (selectedDeckUid == "all") {
            cards
        } else {
            cards.filter { it.deckUid == selectedDeckUid }
        }
    }

    private fun isCardInScope(card: OfflineCard?, scopedCards: List<OfflineCard>): Boolean {
        if (card == null) {
            return false
        }
        return scopedCards.any { it.cardUid == card.cardUid }
    }

    private fun pickNextCard(sourceCards: List<OfflineCard>): OfflineCard? {
        if (sourceCards.isEmpty()) {
            return null
        }

        val now = System.currentTimeMillis()
        val dueCards = sourceCards.filter { isDueCard(it, now) }
        if (dueCards.isNotEmpty()) {
            return dueCards.minWithOrNull(
                compareBy<OfflineCard>(
                    { it.dueAtEpochMs ?: Long.MAX_VALUE },
                    { it.position },
                    { it.cardUid }
                )
            )
        }

        val newCards = sourceCards.filter { it.isNew }
        return newCards.minWithOrNull(
            compareBy<OfflineCard>(
                { it.position },
                { it.cardUid }
            )
        )
    }

    private fun isDueCard(card: OfflineCard, now: Long): Boolean {
        val dueAt = card.dueAtEpochMs ?: return false
        return !card.isNew && dueAt <= now
    }

    private fun buildStatus(card: OfflineCard, deckCardCount: Int): String {
        val dueAt = card.dueAtEpochMs
        val due = if (dueAt == null) "agora" else formatHumanDate(dueAt)
        val cardType = when {
            card.isNew -> "novo"
            effectiveFsrsState(card) == FSRS_STATE_LEARNING -> "aprendendo"
            effectiveFsrsState(card) == FSRS_STATE_RELEARNING -> "reaprendendo"
            else -> "revisao"
        }
        return "${card.deckTitle} - $cardType - volta $due - $deckCardCount cards"
    }

    private fun buildSelectedDeckStatus(deckCardCount: Int): String {
        val deckTitle = selectedDeckSummary()?.title ?: getString(R.string.all_decks)
        return "$deckTitle - $deckCardCount cards offline"
    }

    private fun buildReviewIntro(nextCard: OfflineCard?, deckCardCount: Int): String {
        if (nextCard == null) {
            return getString(R.string.offline_review_no_eligible)
        }

        val dueLabel = when {
            nextCard.isNew -> getString(R.string.offline_review_intro_new)
            effectiveFsrsState(nextCard) == FSRS_STATE_LEARNING -> getString(R.string.offline_review_intro_learning)
            effectiveFsrsState(nextCard) == FSRS_STATE_RELEARNING -> getString(R.string.offline_review_intro_relearning)
            else -> getString(R.string.offline_review_intro_due)
        }
        return "$dueLabel Proximo card em ${nextCard.deckTitle}. $deckCardCount cards no escopo."
    }

    private fun updateCounters() {
        val selectedSummary = selectedDeckSummary()
        val pendingCount = store.loadPendingEvents().size
        val localSnapshotCount = cards.size

        dueCountView.text = String.format(Locale.getDefault(), "%d", selectedSummary?.dueCount ?: 0)
        newCountView.text = String.format(Locale.getDefault(), "%d", selectedSummary?.newCount ?: 0)
        wrongQueueCountView.text = String.format(
            Locale.getDefault(),
            "%d",
            selectedSummary?.learningCount ?: wrongQueueCountForSelectedDeck(),
        )
        syncStatusView.text = "Sync: ${pendingCount} pendentes · ${localSnapshotCount} cards no cache"
    }

    private fun selectedDeckSummary(): DeckSummary? {
        return deckOptions.firstOrNull { it.deckUid == selectedDeckUid }
    }

    private fun wrongQueueCountForSelectedDeck(): Int {
        return countLearningCards(cardsForSelectedDeck())
    }

    private fun setBusy(enabled: Boolean, status: String?) {
        busy = enabled
        deckSpinner.isEnabled = !enabled
        settingsButton.isEnabled = !enabled
        startReviewButton.isEnabled = !enabled
        showAnswerButton.isEnabled = !enabled
        rate1Button.isEnabled = !enabled
        rate2Button.isEnabled = !enabled
        rate3Button.isEnabled = !enabled
        rate4Button.isEnabled = !enabled

        if (enabled && status != null) {
            statusView.text = status
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun initMarkdownRenderer() {
        markwon = try {
            Markwon.builder(this)
                .usePlugin(MarkwonInlineParserPlugin.create())
                .usePlugin(JLatexMathPlugin.create(questionView.textSize) { builder ->
                    builder.inlinesEnabled(true)
                    builder.theme().textColor(ContextCompat.getColor(this, R.color.eehh_ink))
                })
                .build()
        } catch (_: Throwable) {
            latexRenderingEnabled = false
            Markwon.create(this)
        }
    }

    private fun renderMarkdown(target: TextView, source: String) {
        if (source.isBlank() || source == "-") {
            target.text = "-"
            return
        }

        val normalized = if (latexRenderingEnabled) normalizeLatexDelimiters(source) else source

        try {
            markwon.setMarkdown(target, normalized)
        } catch (_: Throwable) {
            target.text = normalized
        }
    }

    private fun revealAnswer() {
        if (answerView.visibility == View.VISIBLE) {
            return
        }

        val card = currentCard
        if (card != null) {
            rate1Button.text = "Errei\n${formatInterval(computeNextInterval(card, 1))}"
            rate2Button.text = "Difícil\n${formatInterval(computeNextInterval(card, 2))}"
            rate3Button.text = "Bom\n${formatInterval(computeNextInterval(card, 3))}"
            rate4Button.text = "Fácil\n${formatInterval(computeNextInterval(card, 4))}"
        }

        answerView.visibility = View.VISIBLE
        answerView.alpha = 0f
        answerView.translationY = 16f
        answerView.scaleX = 0.98f
        answerView.scaleY = 0.98f
        answerView.animate()
            .alpha(1f)
            .translationY(0f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(260L)
            .setInterpolator(DecelerateInterpolator())
            .start()

        showAnswerButton.visibility = View.GONE
        ratingGroup.visibility = View.VISIBLE
        ratingGroup.alpha = 0f
        ratingGroup.translationY = 10f
        ratingGroup.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(220L)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun formatHumanDate(epochMs: Long): String {
        val format = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
        return format.format(Date(epochMs))
    }

}

private fun formatIsoUtcValue(epochMs: Long): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
    format.timeZone = TimeZone.getTimeZone("UTC")
    return format.format(Date(epochMs))
}

private fun normalizeLatexDelimiters(source: String): String {
    val normalizedBrackets = source
        .replace("\\(", "$$")
        .replace("\\)", "$$")
        .replace("\\[", "$$")
        .replace("\\]", "$$")

    val singleDollarPositions = mutableListOf<Int>()
    for (i in normalizedBrackets.indices) {
        if (normalizedBrackets[i] != '$') {
            continue
        }

        val prev = if (i > 0) normalizedBrackets[i - 1] else '\u0000'
        val next = if (i + 1 < normalizedBrackets.length) normalizedBrackets[i + 1] else '\u0000'
        val isEscaped = prev == '\\'
        val isDouble = prev == '$' || next == '$'

        if (!isEscaped && !isDouble) {
            singleDollarPositions.add(i)
        }
    }

    val toConvert = HashSet<Int>()
    var pairIndex = 0
    while (pairIndex + 1 < singleDollarPositions.size) {
        toConvert.add(singleDollarPositions[pairIndex])
        toConvert.add(singleDollarPositions[pairIndex + 1])
        pairIndex += 2
    }

    val out = StringBuilder(normalizedBrackets.length + toConvert.size)
    for (i in normalizedBrackets.indices) {
        val c = normalizedBrackets[i]
        if (c == '$' && toConvert.contains(i)) {
            out.append("$$")
        } else {
            out.append(c)
        }
    }

    return out.toString()
}

private fun parseIsoToEpochMillis(raw: String?): Long? {
    if (raw.isNullOrBlank()) {
        return null
    }

    val normalized = normalizeIso(raw)
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
        "yyyy-MM-dd'T'HH:mm:ssXXX"
    )

    for (pattern in patterns) {
        try {
            val format = SimpleDateFormat(pattern, Locale.US)
            val parsed = format.parse(normalized)
            if (parsed != null) {
                return parsed.time
            }
        } catch (_: Exception) {
            // Keep trying the next pattern.
        }
    }

    return null
}

private fun normalizeIso(raw: String): String {
    var value = raw.trim()
    if (value.endsWith("Z")) {
        value = "${value.dropLast(1)}+00:00"
    }

    val dotIndex = value.indexOf('.')
    if (dotIndex == -1) {
        return value
    }

    val plusIndex = value.indexOf('+', dotIndex)
    val minusIndex = value.indexOf('-', dotIndex + 1)
    val tzIndex = when {
        plusIndex != -1 -> plusIndex
        minusIndex != -1 -> minusIndex
        else -> -1
    }
    if (tzIndex == -1) {
        return value
    }

    val fraction = value.substring(dotIndex + 1, tzIndex)
    val normalizedFraction = when {
        fraction.length >= 3 -> fraction.substring(0, 3)
        else -> fraction.padEnd(3, '0')
    }

    return value.substring(0, dotIndex + 1) + normalizedFraction + value.substring(tzIndex)
}

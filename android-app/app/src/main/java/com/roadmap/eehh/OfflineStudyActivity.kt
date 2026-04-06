package com.roadmap.eehh

import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.webkit.CookieManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
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

data class DeckSummary(
    val deckUid: String,
    val title: String,
    val cardCount: Int,
    val dueCount: Int,
    val newCount: Int
)

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
    var isNew: Boolean
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
    }

    companion object {
        fun fromJson(item: JSONObject): OfflineCard {
            val due = if (item.isNull("due_at_epoch_ms")) null else item.optLong("due_at_epoch_ms")
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
                isNew = item.optBoolean("is_new", true)
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
    val errorCount: Int
)

private class OfflineStore(private val rootDir: File) {
    private val cardsFile = File(rootDir, "offline_cards_v1.json")
    private val pendingFile = File(rootDir, "offline_pending_reviews_v1.json")

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

private object OfflineApiClient {
    private const val CONNECT_TIMEOUT_MS = 15000
    private const val READ_TIMEOUT_MS = 20000

    fun fetchDeckCatalog(baseUrl: String, cookieHeader: String): List<DeckSummary> {
        val endpoint = "${normalizeBaseUrl(baseUrl)}api/decks"
        val connection = openConnection(endpoint, "GET", cookieHeader)

        try {
            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                throw IOException("Falha ao carregar catalogo de decks ($code)")
            }

            val root = JSONObject(body)
            val deckArray = root.optJSONArray("decks") ?: JSONArray()
            val decks = mutableListOf<DeckSummary>()
            for (i in 0 until deckArray.length()) {
                val item = deckArray.optJSONObject(i) ?: continue
                decks.add(
                    DeckSummary(
                        deckUid = item.optString("deck_uid"),
                        title = item.optString("title"),
                        cardCount = item.optInt("card_count", 0),
                        dueCount = item.optInt("due_count", 0),
                        newCount = item.optInt("new_count", 0)
                    )
                )
            }
            return decks
        } finally {
            connection.disconnect()
        }
    }

    fun exportCards(baseUrl: String, cookieHeader: String, deckUid: String): List<OfflineCard> {
        val encodedDeck = URLEncoder.encode(deckUid, StandardCharsets.UTF_8.toString())
        val endpoint = "${normalizeBaseUrl(baseUrl)}api/sync/export?deck_uid=$encodedDeck"
        val connection = openConnection(endpoint, "GET", cookieHeader)

        try {
            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                throw IOException("Falha ao baixar cards offline ($code)")
            }

            val root = JSONObject(body)
            val cardsArray = root.optJSONArray("cards") ?: JSONArray()
            val cards = mutableListOf<OfflineCard>()
            for (i in 0 until cardsArray.length()) {
                val item = cardsArray.optJSONObject(i) ?: continue
                val state = item.optJSONObject("state")
                val dueRaw = state?.let { optNullableString(it, "due_at") }
                cards.add(
                    OfflineCard(
                        cardUid = item.optString("card_uid"),
                        deckUid = item.optString("deck_uid"),
                        deckTitle = item.optString("deck_title"),
                        questionMd = item.optString("question_md"),
                        answerMd = item.optString("answer_md"),
                        contentHash = item.optString("content_hash"),
                        position = item.optInt("position", 0),
                        dueAtEpochMs = parseIsoToEpochMillis(dueRaw),
                        reps = state?.optInt("reps", 0) ?: 0,
                        lapses = state?.optInt("lapses", 0) ?: 0,
                        isNew = state?.optBoolean("is_new", true) ?: true
                    )
                )
            }
            return cards
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
            return SyncImportResult(emptySet(), 0, 0, 0)
        }

        val payload = JSONObject()
        val eventsArray = JSONArray()
        events.forEach { eventsArray.put(it.toJson()) }
        payload.put("events", eventsArray)

        val endpoint = "${normalizeBaseUrl(baseUrl)}api/sync/import"
        val connection = openConnection(endpoint, "POST", cookieHeader)
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            connection.outputStream.use { stream ->
                stream.write(payload.toString().toByteArray(StandardCharsets.UTF_8))
            }

            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                throw IOException("Falha ao sincronizar ($code)")
            }

            val root = JSONObject(body)
            val errors = root.optJSONArray("errors") ?: JSONArray()
            val errorIds = mutableSetOf<String>()
            for (i in 0 until errors.length()) {
                val item = errors.optJSONObject(i) ?: continue
                val eventId = item.optString("event_id", "")
                if (eventId.isNotBlank()) {
                    errorIds.add(eventId)
                }
            }

            val synced = events.map { it.eventId }.toSet() - errorIds
            return SyncImportResult(
                syncedEventIds = synced,
                accepted = root.optInt("accepted", 0),
                duplicates = root.optInt("duplicates", 0),
                errorCount = errors.length()
            )
        } finally {
            connection.disconnect()
        }
    }

    private fun openConnection(url: String, method: String, cookieHeader: String): HttpURLConnection {
        val connection = (URL(url).openConnection() as HttpURLConnection)
        connection.requestMethod = method
        connection.connectTimeout = CONNECT_TIMEOUT_MS
        connection.readTimeout = READ_TIMEOUT_MS
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
}

class OfflineStudyActivity : AppCompatActivity() {

    private lateinit var store: OfflineStore
    private val cards = mutableListOf<OfflineCard>()
    private var serverDeckCatalog: List<DeckSummary> = emptyList()
    private var deckOptions: List<DeckSummary> = emptyList()
    private var selectedDeckUid: String = "all"
    private var suppressDeckSelection = false

    private var currentCard: OfflineCard? = null
    private var revealStartedAtMs: Long = 0
    private var busy = false

    private lateinit var deckSpinner: Spinner
    private lateinit var statusView: TextView
    private lateinit var pendingView: TextView
    private lateinit var cardsCountView: TextView
    private lateinit var questionView: TextView
    private lateinit var answerView: TextView
    private lateinit var ratingGroup: View
    private lateinit var markwon: Markwon
    private var latexRenderingEnabled = true

    private lateinit var downloadButton: Button
    private lateinit var syncButton: Button
    private lateinit var showAnswerButton: Button
    private lateinit var rate1Button: Button
    private lateinit var rate2Button: Button
    private lateinit var rate3Button: Button
    private lateinit var rate4Button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offline_study)

        store = OfflineStore(filesDir)
        cards.addAll(store.loadCards())

        deckSpinner = findViewById(R.id.offlineDeckSpinner)
        statusView = findViewById(R.id.offlineStatus)
        pendingView = findViewById(R.id.offlinePendingCount)
        cardsCountView = findViewById(R.id.offlineCardsCount)
        questionView = findViewById(R.id.offlineQuestion)
        answerView = findViewById(R.id.offlineAnswer)
        ratingGroup = findViewById(R.id.offlineRatingGroup)

        initMarkdownRenderer()

        downloadButton = findViewById(R.id.offlineDownloadButton)
        syncButton = findViewById(R.id.offlineSyncButton)
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

    private fun configureDeckSelector() {
        deckSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (suppressDeckSelection) {
                    return
                }

                val option = deckOptions.getOrNull(position) ?: return
                if (selectedDeckUid == option.deckUid && currentCard != null) {
                    return
                }

                selectedDeckUid = option.deckUid
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
        downloadButton.setOnClickListener { downloadSnapshot() }
        syncButton.setOnClickListener { syncPendingReviews() }

        showAnswerButton.setOnClickListener {
            revealAnswer()
        }

        rate1Button.setOnClickListener { rateCurrentCard(1) }
        rate2Button.setOnClickListener { rateCurrentCard(2) }
        rate3Button.setOnClickListener { rateCurrentCard(3) }
        rate4Button.setOnClickListener { rateCurrentCard(4) }
    }

    private fun fetchDeckCatalogAsync() {
        Thread {
            try {
                val cookieHeader = CookieManager.getInstance().getCookie(BuildConfig.BASE_URL) ?: return@Thread
                val catalog = OfflineApiClient.fetchDeckCatalog(
                    baseUrl = BuildConfig.BASE_URL,
                    cookieHeader = cookieHeader
                )

                runOnUiThread {
                    if (catalog.isNotEmpty()) {
                        serverDeckCatalog = catalog
                        if (cards.isEmpty()) {
                            refreshDeckOptions(keepSelection = true)
                            renderState()
                        }
                    }
                }
            } catch (_: Exception) {
                // Best effort only.
            }
        }.start()
    }

    private fun refreshDeckOptions(keepSelection: Boolean) {
        val previousDeckUid = if (keepSelection) selectedDeckUid else "all"

        val options = when {
            cards.isNotEmpty() -> buildDeckOptionsFromCards(cards)
            serverDeckCatalog.isNotEmpty() -> buildDeckOptionsFromCatalog(serverDeckCatalog)
            else -> listOf(
                DeckSummary(
                    deckUid = "all",
                    title = getString(R.string.all_decks),
                    cardCount = 0,
                    dueCount = 0,
                    newCount = 0
                )
            )
        }

        deckOptions = options
        val labels = options.map { option ->
            getString(
                R.string.offline_deck_option,
                option.title,
                option.dueCount,
                option.newCount,
                option.cardCount
            )
        }

        val adapter = ArrayAdapter(this, R.layout.spinner_item, labels)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)

        suppressDeckSelection = true
        deckSpinner.adapter = adapter

        val selectedIndex = options.indexOfFirst { it.deckUid == previousDeckUid }
            .takeIf { it >= 0 }
            ?: 0
        selectedDeckUid = options[selectedIndex].deckUid
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
                    dueCount = deckCards.count { it.dueAtEpochMs == null || it.dueAtEpochMs!! <= now },
                    newCount = deckCards.count { it.isNew }
                )
            }
            .sortedBy { it.title.lowercase(Locale.ROOT) }

        val allSummary = DeckSummary(
            deckUid = "all",
            title = getString(R.string.all_decks),
            cardCount = source.size,
            dueCount = source.count { it.dueAtEpochMs == null || it.dueAtEpochMs!! <= now },
            newCount = source.count { it.isNew }
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
            newCount = sorted.sumOf { it.newCount }
        )

        return listOf(allSummary) + sorted
    }

    private fun downloadSnapshot() {
        if (busy) {
            return
        }

        val deckUidForDownload = selectedDeckUid.ifBlank { "all" }
        setBusy(true, "Baixando cards para uso offline...")
        Thread {
            try {
                val cookieHeader = CookieManager.getInstance().getCookie(BuildConfig.BASE_URL)
                    ?: throw IOException(getString(R.string.offline_need_online_login))

                val exportedCards = OfflineApiClient.exportCards(
                    baseUrl = BuildConfig.BASE_URL,
                    cookieHeader = cookieHeader,
                    deckUid = deckUidForDownload
                )
                store.saveCards(exportedCards)

                runOnUiThread {
                    cards.clear()
                    cards.addAll(exportedCards)
                    refreshDeckOptions(keepSelection = true)
                    currentCard = null
                    answerView.visibility = View.GONE
                    toast(getString(R.string.offline_download_done, cards.size))
                    renderState()
                }
            } catch (exc: Exception) {
                runOnUiThread {
                    toast(exc.message ?: "Falha ao baixar dados offline.")
                }
            } finally {
                runOnUiThread {
                    setBusy(false, null)
                }
            }
        }.start()
    }

    private fun syncPendingReviews() {
        if (busy) {
            return
        }

        val pending = store.loadPendingEvents()
        if (pending.isEmpty()) {
            toast("Nada pendente para sincronizar.")
            updateCounters()
            return
        }

        setBusy(true, "Sincronizando revisões pendentes...")
        Thread {
            try {
                val cookieHeader = CookieManager.getInstance().getCookie(BuildConfig.BASE_URL)
                    ?: throw IOException(getString(R.string.offline_need_online_login))

                val importResult = OfflineApiClient.importPendingEvents(
                    baseUrl = BuildConfig.BASE_URL,
                    cookieHeader = cookieHeader,
                    events = pending
                )
                store.markEventsSynced(importResult.syncedEventIds)

                val deckUidForRefresh = selectedDeckUid.ifBlank { "all" }

                val refreshedCards = OfflineApiClient.exportCards(
                    baseUrl = BuildConfig.BASE_URL,
                    cookieHeader = cookieHeader,
                    deckUid = deckUidForRefresh
                )
                store.saveCards(refreshedCards)

                val refreshedCatalog = OfflineApiClient.fetchDeckCatalog(
                    baseUrl = BuildConfig.BASE_URL,
                    cookieHeader = cookieHeader
                )

                runOnUiThread {
                    serverDeckCatalog = refreshedCatalog
                    cards.clear()
                    cards.addAll(refreshedCards)
                    refreshDeckOptions(keepSelection = true)
                    currentCard = null
                    answerView.visibility = View.GONE
                    toast(getString(
                        R.string.offline_sync_done,
                        importResult.accepted,
                        importResult.duplicates,
                        importResult.errorCount
                    ))
                    renderState()
                }
            } catch (exc: Exception) {
                runOnUiThread {
                    toast(exc.message ?: "Falha ao sincronizar revisões.")
                }
            } finally {
                runOnUiThread {
                    setBusy(false, null)
                }
            }
        }.start()
    }

    private fun rateCurrentCard(rating: Int) {
        val card = currentCard ?: run {
            toast("Nenhum card disponível.")
            return
        }

        val now = System.currentTimeMillis()
        val elapsed = (now - revealStartedAtMs).coerceIn(0L, 600_000L).toInt()

        card.isNew = false
        card.reps += 1
        if (rating == 1) {
            card.lapses += 1
        }
        card.dueAtEpochMs = now + dueOffsetMillis(rating)

        val event = PendingReviewEvent(
            eventId = UUID.randomUUID().toString(),
            cardUid = card.cardUid,
            rating = rating,
            durationMs = elapsed,
            reviewedAt = formatIsoUtc(now)
        )

        store.appendPendingEvent(event)
        store.saveCards(cards)
        answerView.visibility = View.GONE
        currentCard = null

        renderState()
    }

    private fun renderState() {
        updateCounters()

        if (cards.isEmpty()) {
            statusView.setText(R.string.offline_status_empty)
            renderMarkdown(questionView, "-")
            renderMarkdown(answerView, "-")
            answerView.visibility = View.GONE
            showAnswerButton.visibility = View.GONE
            ratingGroup.visibility = View.GONE
            return
        }

        val scopedCards = cardsForSelectedDeck()
        if (scopedCards.isEmpty()) {
            statusView.setText(R.string.offline_status_deck_empty)
            renderMarkdown(questionView, "-")
            renderMarkdown(answerView, "-")
            answerView.visibility = View.GONE
            showAnswerButton.visibility = View.GONE
            ratingGroup.visibility = View.GONE
            return
        }

        if (currentCard == null || !isCardInScope(currentCard, scopedCards)) {
            currentCard = pickNextCard(scopedCards)
            revealStartedAtMs = System.currentTimeMillis()
        }

        val card = currentCard
        if (card == null) {
            statusView.setText(R.string.offline_status_no_eligible)
            renderMarkdown(questionView, "-")
            renderMarkdown(answerView, "-")
            answerView.visibility = View.GONE
            showAnswerButton.visibility = View.GONE
            ratingGroup.visibility = View.GONE
            return
        }

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
        val dueCards = sourceCards.filter { it.dueAtEpochMs == null || it.dueAtEpochMs!! <= now }
        val source = if (dueCards.isNotEmpty()) dueCards else sourceCards

        return source.minWithOrNull(
            compareBy<OfflineCard>(
                { it.dueAtEpochMs ?: Long.MIN_VALUE },
                { it.position },
                { it.cardUid }
            )
        )
    }

    private fun buildStatus(card: OfflineCard, deckCardCount: Int): String {
        val dueAt = card.dueAtEpochMs
        val due = if (dueAt == null) "agora" else formatHumanDate(dueAt)
        val cardType = if (card.isNew) "novo" else "revisao"
        return "${card.deckTitle} - $cardType - due: $due - cards: $deckCardCount"
    }

    private fun updateCounters() {
        val pendingCount = store.loadPendingEvents().size
        pendingView.text = getString(R.string.offline_pending_count, pendingCount)

        val scopedCount = cardsForSelectedDeck().size
        cardsCountView.text = getString(R.string.offline_cards_count, scopedCount)
    }

    private fun setBusy(enabled: Boolean, status: String?) {
        busy = enabled
        deckSpinner.isEnabled = !enabled
        downloadButton.isEnabled = !enabled
        syncButton.isEnabled = !enabled
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

    private fun dueOffsetMillis(rating: Int): Long {
        return when (rating) {
            1 -> 10L * 60L * 1000L
            2 -> 6L * 60L * 60L * 1000L
            3 -> 24L * 60L * 60L * 1000L
            4 -> 3L * 24L * 60L * 60L * 1000L
            else -> 24L * 60L * 60L * 1000L
        }
    }

    private fun formatHumanDate(epochMs: Long): String {
        val format = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
        return format.format(Date(epochMs))
    }

    private fun formatIsoUtc(epochMs: Long): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.format(Date(epochMs))
    }
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

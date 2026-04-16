package com.roadmap.eehh

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

data class SessionStatus(
    val authenticated: Boolean,
    val username: String? = null
)

data class LoginResult(
    val session: SessionStatus,
    val cookieHeader: String
)

data class SnapshotRefreshStatus(
    val incremental: Boolean,
    val upsertCount: Int,
    val removedCount: Int,
    val unchangedCount: Int,
    val cardCount: Int,
    val refreshedAtEpochMs: Long
)

internal const val SESSION_REAUTH_REQUIRED_ERROR = "session_reauth_required"
private const val ALL_DECK_UID = "all"

private data class PowChallenge(
    val challengeId: String,
    val prefix: String,
    val difficulty: Int
)

internal object AppPreferences {
    private const val SESSION_PREFS = "roadmap_session_secure"
    private const val COOKIE_HEADER_KEY = "cookie_header"
    private const val APP_PREFS = "roadmap_native_app"
    private const val USERNAME_KEY = "username"
    private const val SESSION_ACTIVE_KEY = "session_active"
    private const val SESSION_HEALTH_KEY = "session_health"
    private const val LAST_DECK_UID_KEY = "last_deck_uid"
    private const val REMINDER_ENABLED_KEY = "reminder_enabled"
    private const val REMINDER_MINUTE_KEY = "reminder_minute_of_day"
    private const val AUTO_SYNC_ENABLED_KEY = "auto_sync_enabled"
    private const val AUTO_REFRESH_SNAPSHOT_KEY = "auto_refresh_snapshot_key"
    private const val SNAPSHOT_REFRESH_MODE_KEY = "snapshot_refresh_mode"
    private const val SNAPSHOT_REFRESH_UPSERTS_KEY = "snapshot_refresh_upserts"
    private const val SNAPSHOT_REFRESH_REMOVED_KEY = "snapshot_refresh_removed"
    private const val SNAPSHOT_REFRESH_UNCHANGED_KEY = "snapshot_refresh_unchanged"
    private const val SNAPSHOT_REFRESH_CARD_COUNT_KEY = "snapshot_refresh_card_count"
    private const val SNAPSHOT_REFRESH_AT_KEY = "snapshot_refresh_at"
    private const val LAST_SYNC_AT_KEY = "last_sync_at"
    private const val DEFAULT_REMINDER_MINUTE = 20 * 60
    private const val SESSION_HEALTH_OK = "ok"
    private const val SESSION_HEALTH_REAUTH_REQUIRED = "reauth_required"
    private const val SNAPSHOT_REFRESH_MODE_FULL = "full"
    private const val SNAPSHOT_REFRESH_MODE_INCREMENTAL = "incremental"
    private val masterKeyAlias by lazy { MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC) }

    private fun sessionPrefs(context: Context) = EncryptedSharedPreferences.create(
        SESSION_PREFS,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private fun appPrefs(context: Context) =
        context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE)

    fun loadCookieHeader(context: Context): String? {
        if (!hasActiveSession(context)) {
            return null
        }
        return sessionPrefs(context)
            .getString(COOKIE_HEADER_KEY, null)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
    }

    fun hasActiveSession(context: Context): Boolean {
        val sessionActive = sessionPrefs(context)
            .getBoolean(SESSION_ACTIVE_KEY, false)
        val cookie = sessionPrefs(context)
            .getString(COOKIE_HEADER_KEY, null)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
        return sessionActive && cookie != null
    }

    fun saveSession(context: Context, cookieHeader: String, username: String?) {
        sessionPrefs(context)
            .edit()
            .putString(COOKIE_HEADER_KEY, cookieHeader.trim())
            .putString(USERNAME_KEY, username?.trim()?.takeIf { it.isNotEmpty() })
            .putBoolean(SESSION_ACTIVE_KEY, true)
            .putString(SESSION_HEALTH_KEY, SESSION_HEALTH_OK)
            .apply()
    }

    fun clearSession(context: Context) {
        sessionPrefs(context)
            .edit()
            .remove(COOKIE_HEADER_KEY)
            .remove(USERNAME_KEY)
            .remove(SESSION_ACTIVE_KEY)
            .remove(SESSION_HEALTH_KEY)
            .apply()
    }

    fun isSessionReauthRequired(context: Context): Boolean {
        return sessionPrefs(context)
            .getString(SESSION_HEALTH_KEY, SESSION_HEALTH_OK) == SESSION_HEALTH_REAUTH_REQUIRED
    }

    fun markSessionHealthy(context: Context) {
        sessionPrefs(context)
            .edit()
            .putString(SESSION_HEALTH_KEY, SESSION_HEALTH_OK)
            .apply()
    }

    fun markSessionReauthRequired(context: Context) {
        sessionPrefs(context)
            .edit()
            .putString(SESSION_HEALTH_KEY, SESSION_HEALTH_REAUTH_REQUIRED)
            .apply()
    }

    fun loadUsername(context: Context): String? {
        return sessionPrefs(context)
            .getString(USERNAME_KEY, null)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
    }

    fun loadLastDeckUid(context: Context): String {
        return appPrefs(context)
            .getString(LAST_DECK_UID_KEY, ALL_DECK_UID)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: ALL_DECK_UID
    }

    fun saveLastDeckUid(context: Context, deckUid: String) {
        val normalized = deckUid.trim().ifBlank { ALL_DECK_UID }
        appPrefs(context)
            .edit()
            .putString(LAST_DECK_UID_KEY, normalized)
            .apply()
    }

    fun isReminderEnabled(context: Context): Boolean {
        return appPrefs(context)
            .getBoolean(REMINDER_ENABLED_KEY, false)
    }

    fun setReminderEnabled(context: Context, enabled: Boolean) {
        appPrefs(context)
            .edit()
            .putBoolean(REMINDER_ENABLED_KEY, enabled)
            .apply()
    }

    fun loadReminderMinute(context: Context): Int {
        return appPrefs(context)
            .getInt(REMINDER_MINUTE_KEY, DEFAULT_REMINDER_MINUTE)
            .coerceIn(0, 23 * 60 + 59)
    }

    fun saveReminderMinute(context: Context, minuteOfDay: Int) {
        appPrefs(context)
            .edit()
            .putInt(REMINDER_MINUTE_KEY, minuteOfDay.coerceIn(0, 23 * 60 + 59))
            .apply()
    }

    fun isAutoSyncEnabled(context: Context): Boolean {
        return appPrefs(context)
            .getBoolean(AUTO_SYNC_ENABLED_KEY, true)
    }

    fun setAutoSyncEnabled(context: Context, enabled: Boolean) {
        appPrefs(context)
            .edit()
            .putBoolean(AUTO_SYNC_ENABLED_KEY, enabled)
            .apply()
    }

    fun shouldRefreshSnapshotOnLaunch(context: Context): Boolean {
        return appPrefs(context)
            .getBoolean(AUTO_REFRESH_SNAPSHOT_KEY, true)
    }

    fun setAutoRefreshSnapshotOnLaunch(context: Context, enabled: Boolean) {
        appPrefs(context)
            .edit()
            .putBoolean(AUTO_REFRESH_SNAPSHOT_KEY, enabled)
            .apply()
    }

    fun saveSnapshotRefreshStatus(context: Context, status: SnapshotRefreshStatus) {
        appPrefs(context)
            .edit()
            .putString(
                SNAPSHOT_REFRESH_MODE_KEY,
                if (status.incremental) SNAPSHOT_REFRESH_MODE_INCREMENTAL else SNAPSHOT_REFRESH_MODE_FULL
            )
            .putInt(SNAPSHOT_REFRESH_UPSERTS_KEY, status.upsertCount)
            .putInt(SNAPSHOT_REFRESH_REMOVED_KEY, status.removedCount)
            .putInt(SNAPSHOT_REFRESH_UNCHANGED_KEY, status.unchangedCount)
            .putInt(SNAPSHOT_REFRESH_CARD_COUNT_KEY, status.cardCount)
            .putLong(SNAPSHOT_REFRESH_AT_KEY, status.refreshedAtEpochMs)
            .apply()
    }

    fun loadSnapshotRefreshStatus(context: Context): SnapshotRefreshStatus? {
        val prefs = appPrefs(context)
        val refreshedAt = prefs.getLong(SNAPSHOT_REFRESH_AT_KEY, 0L)
        if (refreshedAt <= 0L) {
            return null
        }

        val mode = prefs.getString(SNAPSHOT_REFRESH_MODE_KEY, SNAPSHOT_REFRESH_MODE_FULL)
        return SnapshotRefreshStatus(
            incremental = mode == SNAPSHOT_REFRESH_MODE_INCREMENTAL,
            upsertCount = prefs.getInt(SNAPSHOT_REFRESH_UPSERTS_KEY, 0),
            removedCount = prefs.getInt(SNAPSHOT_REFRESH_REMOVED_KEY, 0),
            unchangedCount = prefs.getInt(SNAPSHOT_REFRESH_UNCHANGED_KEY, 0),
            cardCount = prefs.getInt(SNAPSHOT_REFRESH_CARD_COUNT_KEY, 0),
            refreshedAtEpochMs = refreshedAt
        )
    }

    fun clearSnapshotRefreshStatus(context: Context) {
        appPrefs(context)
            .edit()
            .remove(SNAPSHOT_REFRESH_MODE_KEY)
            .remove(SNAPSHOT_REFRESH_UPSERTS_KEY)
            .remove(SNAPSHOT_REFRESH_REMOVED_KEY)
            .remove(SNAPSHOT_REFRESH_UNCHANGED_KEY)
            .remove(SNAPSHOT_REFRESH_CARD_COUNT_KEY)
            .remove(SNAPSHOT_REFRESH_AT_KEY)
            .apply()
    }

    fun loadLastSyncAt(context: Context): String? {
        return appPrefs(context)
            .getString(LAST_SYNC_AT_KEY, null)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
    }

    fun saveLastSyncAt(context: Context, serverNow: String) {
        appPrefs(context)
            .edit()
            .putString(LAST_SYNC_AT_KEY, serverNow.trim())
            .apply()
    }

    fun clearLastSyncAt(context: Context) {
        appPrefs(context)
            .edit()
            .remove(LAST_SYNC_AT_KEY)
            .apply()
    }
}

internal object AuthApiClient {
    private const val CONNECT_TIMEOUT_MS = 15000
    private const val READ_TIMEOUT_MS = 20000
    private val HEX_DIGITS = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

    fun getSessionStatus(baseUrl: String, cookieHeader: String?): SessionStatus {
        if (cookieHeader.isNullOrBlank()) {
            return SessionStatus(authenticated = false)
        }

        val endpoint = "${normalizeBaseUrl(baseUrl)}api/me"
        val connection = openConnection(endpoint, "GET", cookieHeader)

        try {
            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                return SessionStatus(authenticated = false)
            }

            val root = JSONObject(body)
            return SessionStatus(
                authenticated = requireBoolean(root, "authenticated"),
                username = root.optString("username").trim().ifEmpty { null }
            )
        } catch (exc: Exception) {
            throw NetworkErrorSupport.wrap(baseUrl, exc)
        } finally {
            connection.disconnect()
        }
    }

    fun login(baseUrl: String, login: String, password: String): LoginResult {
        return loginWithRetry(baseUrl, login.trim(), password, retriesRemaining = 1)
    }

    fun logout(baseUrl: String, cookieHeader: String?) {
        val endpoint = "${normalizeBaseUrl(baseUrl)}api/auth/logout"
        val connection = openConnection(endpoint, "POST", cookieHeader)
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            connection.outputStream.use { stream ->
                stream.write("{}".toByteArray(StandardCharsets.UTF_8))
            }

            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                throw IOException(extractErrorMessage(body, "Falha ao encerrar a sessao."))
            }
        } catch (exc: Exception) {
            throw NetworkErrorSupport.wrap(baseUrl, exc)
        } finally {
            connection.disconnect()
        }
    }

    private fun fetchChallenge(baseUrl: String): PowChallenge {
        val endpoint = "${normalizeBaseUrl(baseUrl)}api/auth/challenge"
        val connection = openConnection(endpoint, "POST", null)
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            connection.outputStream.use { stream ->
                stream.write("{}".toByteArray(StandardCharsets.UTF_8))
            }

            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                throw IOException(extractErrorMessage(body, "Falha ao obter desafio anti-bot."))
            }

            val root = JSONObject(body)
            return PowChallenge(
                challengeId = requireString(root, "challenge_id"),
                prefix = requireString(root, "prefix"),
                difficulty = requirePositiveInt(root, "difficulty")
            )
        } catch (exc: Exception) {
            throw NetworkErrorSupport.wrap(baseUrl, exc)
        } finally {
            connection.disconnect()
        }
    }

    private fun loginWithRetry(
        baseUrl: String,
        login: String,
        password: String,
        retriesRemaining: Int,
    ): LoginResult {
        val challenge = fetchChallenge(baseUrl)
        val nonce = solvePow(challenge.prefix, challenge.difficulty)

        val payload = JSONObject()
            .put("login", login)
            .put("password", password)
            .put("challenge_id", challenge.challengeId)
            .put("nonce", nonce)

        val endpoint = "${normalizeBaseUrl(baseUrl)}api/auth/login"
        val connection = openConnection(endpoint, "POST", null)
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        try {
            connection.outputStream.use { stream ->
                stream.write(payload.toString().toByteArray(StandardCharsets.UTF_8))
            }

            val code = connection.responseCode
            val body = readBody(connection)
            if (code !in 200..299) {
                val detail = extractErrorDetail(body)
                if (retriesRemaining > 0 && shouldRetryAuth(detail)) {
                    return loginWithRetry(baseUrl, login, password, retriesRemaining - 1)
                }
                throw IOException(humanizeAuthError(detail, "Falha no login."))
            }

            val root = JSONObject(body)
            val username = root.optString("username").ifBlank { login }
            val cookieHeader = extractCookieHeader(connection)
                ?: throw IOException("Servidor nao retornou sessao valida.")

            return LoginResult(
                session = SessionStatus(authenticated = true, username = username),
                cookieHeader = cookieHeader
            )
        } catch (exc: Exception) {
            throw NetworkErrorSupport.wrap(baseUrl, exc)
        } finally {
            connection.disconnect()
        }
    }

    private fun solvePow(prefix: String, difficulty: Int): String {
        val target = "0".repeat(difficulty.coerceAtLeast(0))
        var nonce = 0L

        while (true) {
            val digest = sha256Hex("$prefix:$nonce")
            if (digest.startsWith(target)) {
                return nonce.toString()
            }
            nonce += 1L
        }
    }

    private fun sha256Hex(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(input.toByteArray(StandardCharsets.UTF_8))
        val out = StringBuilder(bytes.size * 2)
        for (byte in bytes) {
            val value = byte.toInt() and 0xff
            out.append(HEX_DIGITS[value ushr 4])
            out.append(HEX_DIGITS[value and 0x0f])
        }
        return out.toString()
    }

    private fun extractCookieHeader(connection: HttpURLConnection): String? {
        val rawCookies = connection.headerFields
            .filterKeys { key -> key != null && key.equals("Set-Cookie", ignoreCase = true) }
            .values
            .flatten()
            .mapNotNull { header ->
                header.substringBefore(';').trim().takeIf { it.contains('=') }
            }
            .distinct()

        return rawCookies.joinToString(separator = "; ").ifBlank { null }
    }

    private fun openConnection(
        url: String,
        method: String,
        cookieHeader: String?
    ): HttpURLConnection {
        val connection = (URL(url).openConnection() as HttpURLConnection)
        connection.requestMethod = method
        connection.connectTimeout = CONNECT_TIMEOUT_MS
        connection.readTimeout = READ_TIMEOUT_MS
        connection.instanceFollowRedirects = true
        connection.setRequestProperty("Accept", "application/json")
        if (!cookieHeader.isNullOrBlank()) {
            connection.setRequestProperty("Cookie", cookieHeader)
        }
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

    private fun extractErrorMessage(body: String, fallback: String): String {
        return humanizeAuthError(extractErrorDetail(body), fallback)
    }

    private fun extractErrorDetail(body: String): String? {
        if (body.isBlank()) {
            return null
        }

        return try {
            JSONObject(body).optString("detail").ifBlank { null }
        } catch (_: Exception) {
            null
        }
    }

    private fun shouldRetryAuth(detail: String?): Boolean {
        return detail == "challenge_expirado" ||
            detail == "challenge_inexistente" ||
            detail == "challenge_ja_utilizado" ||
            detail == "pow_invalido"
    }

    private fun humanizeAuthError(detail: String?, fallback: String): String {
        if (detail.isNullOrBlank()) {
            return fallback
        }
        if (detail == "credenciais_invalidas") {
            return "Usuario/e-mail ou senha invalidos."
        }
        if (detail.startsWith("limite_excedido_tente_em_")) {
            val seconds = detail.substringAfterLast('_', "alguns")
            return "Muitas tentativas. Tente novamente em ${seconds}s."
        }
        if (shouldRetryAuth(detail)) {
            return "Falha temporaria ao validar o desafio de login. Tente novamente."
        }
        return detail
    }

    private fun normalizeBaseUrl(baseUrl: String): String {
        val trimmed = baseUrl.trim()
        return if (trimmed.endsWith('/')) trimmed else "$trimmed/"
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

    private fun requirePositiveInt(obj: JSONObject, key: String): Int {
        if (!obj.has(key) || obj.isNull(key)) {
            throw IOException("Resposta invalida do servidor: campo '$key' ausente.")
        }
        val value = obj.getInt(key)
        if (value <= 0) {
            throw IOException("Resposta invalida do servidor: campo '$key' invalido.")
        }
        return value
    }
}
package com.roadmap.eehh

import android.content.Context
import java.util.concurrent.atomic.AtomicBoolean

internal object AppSessionRefreshManager {
    private val running = AtomicBoolean(false)

    fun requestRefresh(context: Context) {
        val appContext = context.applicationContext
        val cookieHeader = AppPreferences.loadCookieHeader(appContext) ?: return
        if (!running.compareAndSet(false, true)) {
            return
        }

        Thread {
            try {
                val sessionStatus = AuthApiClient.getSessionStatus(BuildConfig.BASE_URL, cookieHeader)
                if (sessionStatus.authenticated) {
                    AppPreferences.saveSession(appContext, cookieHeader, sessionStatus.username)
                } else {
                    AppPreferences.markSessionReauthRequired(appContext)
                }
            } catch (_: Exception) {
                // Best effort only. Keep cached offline access intact.
            } finally {
                running.set(false)
            }
        }.start()
    }
}
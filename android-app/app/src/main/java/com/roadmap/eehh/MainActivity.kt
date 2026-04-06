package com.roadmap.eehh

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.webkit.CookieManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import java.util.Locale

class MainActivity : AppCompatActivity() {

    companion object {
        private const val COOKIE_PREFS = "roadmap_webview"
        private const val COOKIE_HEADER_KEY = "cookie_header"
    }

    private lateinit var webView: WebView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var offlineModeButton: Button
    private lateinit var offlineOverlay: View
    private lateinit var offlineTitle: TextView
    private lateinit var offlineMessage: TextView
    private lateinit var retryButton: Button
    private lateinit var offlineOverlayButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.mainWebView)
        loadingIndicator = findViewById(R.id.mainProgress)
        offlineModeButton = findViewById(R.id.offlineModeButton)
        offlineOverlay = findViewById(R.id.offlineOverlay)
        offlineTitle = findViewById(R.id.mainOfflineTitle)
        offlineMessage = findViewById(R.id.mainOfflineMessage)
        retryButton = findViewById(R.id.mainRetryButton)
        offlineOverlayButton = findViewById(R.id.mainOfflineModeButton)

        configureWebView()
        configureBackNavigation()
        configureActions()

        if (savedInstanceState == null) {
            if (hasInternetConnection()) {
                webView.loadUrl(BuildConfig.BASE_URL)
            } else {
                showOfflineUnavailable(getString(R.string.online_unavailable_message))
            }
        } else {
            webView.restoreState(savedInstanceState)
            if (!hasInternetConnection()) {
                showOfflineUnavailable(getString(R.string.online_unavailable_message))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        persistCookies(webView.url)
        super.onPause()
    }

    @Suppress("DEPRECATION")
    override fun onDestroy() {
        webView.stopLoading()
        webView.destroy()
        super.onDestroy()
    }

    private fun configureWebView() {
        val cookies = CookieManager.getInstance()
        cookies.setAcceptCookie(true)
        cookies.setAcceptThirdPartyCookies(webView, true)
        restorePersistedCookies()

        with(webView.settings) {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            loadsImagesAutomatically = true
            useWideViewPort = true
            loadWithOverviewMode = true
            javaScriptCanOpenWindowsAutomatically = false
            mixedContentMode = WebSettings.MIXED_CONTENT_NEVER_ALLOW
            allowFileAccess = false
            allowContentAccess = false
            setSupportZoom(true)
            builtInZoomControls = false
            displayZoomControls = false
            userAgentString = "$userAgentString RoadmapAndroid/1.0"
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: android.graphics.Bitmap?) {
                loadingIndicator.isVisible = true
                hideOfflineUnavailable()
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                loadingIndicator.isVisible = false
                hideOfflineUnavailable()
                persistCookies(url)
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                if (request?.isForMainFrame == true) {
                    loadingIndicator.isVisible = false
                    showOfflineUnavailable(getString(R.string.online_unavailable_message))
                }
                super.onReceivedError(view, request, error)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url ?: return false
                return when (url.scheme?.lowercase(Locale.ROOT)) {
                    "http", "https" -> false
                    else -> openExternally(url)
                }
            }
        }
    }

    private fun configureActions() {
        offlineModeButton.setOnClickListener {
            startActivity(Intent(this, OfflineStudyActivity::class.java))
        }

        retryButton.setOnClickListener {
            if (!hasInternetConnection()) {
                showOfflineUnavailable(getString(R.string.online_unavailable_message))
                return@setOnClickListener
            }

            hideOfflineUnavailable()
            loadingIndicator.isVisible = true
            val currentUrl = webView.url
            if (currentUrl.isNullOrBlank()) {
                webView.loadUrl(BuildConfig.BASE_URL)
            } else {
                webView.reload()
            }
        }

        offlineOverlayButton.setOnClickListener {
            startActivity(Intent(this, OfflineStudyActivity::class.java))
        }
    }

    private fun configureBackNavigation() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }

    private fun openExternally(uri: Uri): Boolean {
        return try {
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            true
        } catch (_: ActivityNotFoundException) {
            false
        }
    }

    private fun persistCookies(url: String?) {
        val targetUrl = url ?: BuildConfig.BASE_URL
        val header = CookieManager.getInstance().getCookie(targetUrl)
        val prefs = getSharedPreferences(COOKIE_PREFS, MODE_PRIVATE)

        if (header.isNullOrBlank()) {
            prefs.edit().remove(COOKIE_HEADER_KEY).apply()
        } else {
            prefs.edit().putString(COOKIE_HEADER_KEY, header).apply()
        }

        CookieManager.getInstance().flush()
    }

    private fun restorePersistedCookies() {
        val header = getSharedPreferences(COOKIE_PREFS, MODE_PRIVATE)
            .getString(COOKIE_HEADER_KEY, null)
            ?: return

        val cookieManager = CookieManager.getInstance()
        header.split(";")
            .map { it.trim() }
            .filter { it.contains("=") }
            .forEach { cookieManager.setCookie(BuildConfig.BASE_URL, it) }

        cookieManager.flush()
    }

    private fun hasInternetConnection(): Boolean {
        return try {
            val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = manager.activeNetwork ?: return false
            val capabilities = manager.getNetworkCapabilities(activeNetwork) ?: return false

            val hasNetworkTransport = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)

            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                (capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) || hasNetworkTransport)
        } catch (_: Exception) {
            false
        }
    }

    private fun showOfflineUnavailable(message: String) {
        offlineTitle.setText(R.string.online_unavailable_title)
        offlineMessage.text = message
        loadingIndicator.isVisible = false
        offlineOverlay.isVisible = true
    }

    private fun hideOfflineUnavailable() {
        offlineOverlay.isVisible = false
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

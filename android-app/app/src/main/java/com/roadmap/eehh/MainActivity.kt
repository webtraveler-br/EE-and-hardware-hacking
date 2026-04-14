package com.roadmap.eehh

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var statusView: TextView
    private lateinit var baseUrlView: TextView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var loginFields: View
    private lateinit var loginInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (AppPreferences.hasActiveSession(this)) {
            launchStudy()
            return
        }
        setContentView(R.layout.activity_main)

        statusView = findViewById(R.id.mainSessionStatus)
        baseUrlView = findViewById(R.id.mainBaseUrl)
        loadingIndicator = findViewById(R.id.mainProgress)
        loginFields = findViewById(R.id.mainLoginFields)
        loginInput = findViewById(R.id.mainLoginInput)
        passwordInput = findViewById(R.id.mainPasswordInput)
        loginButton = findViewById(R.id.mainLoginButton)

        baseUrlView.text = getString(R.string.main_server_url, BuildConfig.BASE_URL)

        wireActions()
        showLoginState()
    }

    private fun wireActions() {
        loginButton.setOnClickListener {
            submitLogin()
        }
    }

    private fun showLoginState() {
        statusView.text = getString(R.string.main_status_login_required)
        loginFields.visibility = View.VISIBLE
        loginButton.visibility = View.VISIBLE
    }

    private fun launchStudy() {
        startActivity(Intent(this, OfflineStudyActivity::class.java))
        finish()
    }

    private fun submitLogin() {
        if (loadingIndicator.isVisible) {
            return
        }

        val login = loginInput.text?.toString()?.trim().orEmpty()
        val password = passwordInput.text?.toString().orEmpty()
        if (login.isBlank() || password.isBlank()) {
            toast(getString(R.string.main_login_missing_fields))
            return
        }

        setBusy(true, getString(R.string.main_status_logging_in))
        Thread {
            try {
                val result = AuthApiClient.login(BuildConfig.BASE_URL, login, password)
                runOnUiThread {
                    AppPreferences.saveSession(this, result.cookieHeader, result.session.username)
                    passwordInput.text?.clear()
                    AppAutoSyncManager.requestForegroundSync(applicationContext)
                    toast(getString(R.string.main_login_success))
                    launchStudy()
                }
            } catch (exc: Exception) {
                runOnUiThread {
                    showLoginState()
                    setBusy(false, null)
                    toast(exc.message ?: getString(R.string.main_login_failed))
                }
            }
        }.start()
    }

    private fun setBusy(enabled: Boolean, statusMessage: String?) {
        loadingIndicator.isVisible = enabled
        loginInput.isEnabled = !enabled
        passwordInput.isEnabled = !enabled
        loginButton.isEnabled = !enabled
        if (statusMessage != null) {
            statusView.text = statusMessage
        } else {
            showLoginState()
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

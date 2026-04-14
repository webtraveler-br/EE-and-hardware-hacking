package com.roadmap.eehh

import android.Manifest
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var loadingView: ProgressBar
    private lateinit var accountStatusView: TextView
    private lateinit var serverUrlView: TextView
    private lateinit var offlineSummaryView: TextView
    private lateinit var snapshotIndicatorView: TextView
    private lateinit var syncWarningView: TextView
    private lateinit var autoSyncSwitch: SwitchCompat
    private lateinit var autoRefreshSwitch: SwitchCompat
    private lateinit var reminderSwitch: SwitchCompat
    private lateinit var reminderTimeButton: Button
    private lateinit var syncNowButton: Button
    private lateinit var refreshSnapshotButton: Button
    private lateinit var clearDataButton: Button
    private lateinit var logoutButton: Button

    private var busy = false
    private var suppressSwitchListeners = false

    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            AppPreferences.setReminderEnabled(this, true)
            DailyReminderScheduler.sync(this)
        } else {
            AppPreferences.setReminderEnabled(this, false)
            DailyReminderScheduler.cancel(this)
            toast(getString(R.string.reminder_permission_denied))
        }
        bindPreferenceState()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!AppPreferences.hasActiveSession(this)) {
            redirectToLogin()
            return
        }

        setContentView(R.layout.activity_settings)

        backButton = findViewById(R.id.settingsBackButton)
        loadingView = findViewById(R.id.settingsProgress)
        accountStatusView = findViewById(R.id.settingsAccountStatus)
        serverUrlView = findViewById(R.id.settingsServerUrl)
        offlineSummaryView = findViewById(R.id.settingsOfflineSummary)
        snapshotIndicatorView = findViewById(R.id.settingsSnapshotIndicator)
        syncWarningView = findViewById(R.id.settingsSyncWarning)
        autoSyncSwitch = findViewById(R.id.settingsAutoSyncSwitch)
        autoRefreshSwitch = findViewById(R.id.settingsRefreshSnapshotSwitch)
        reminderSwitch = findViewById(R.id.settingsReminderSwitch)
        reminderTimeButton = findViewById(R.id.settingsReminderTimeButton)
        syncNowButton = findViewById(R.id.settingsSyncNowButton)
        refreshSnapshotButton = findViewById(R.id.settingsRefreshSnapshotButton)
        clearDataButton = findViewById(R.id.settingsClearDataButton)
        logoutButton = findViewById(R.id.settingsLogoutButton)

        wireActions()
        renderAccountState()
        bindPreferenceState()
        refreshOfflineSummary()
    }

    override fun onResume() {
        super.onResume()
        if (!AppPreferences.hasActiveSession(this)) {
            redirectToLogin()
            return
        }
        bindPreferenceState()
        refreshOfflineSummary()
    }

    private fun wireActions() {
        backButton.setOnClickListener { finish() }

        autoSyncSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (suppressSwitchListeners) {
                return@setOnCheckedChangeListener
            }
            AppPreferences.setAutoSyncEnabled(this, isChecked)
            if (!isChecked) {
                AppPreferences.setAutoRefreshSnapshotOnLaunch(this, false)
            }
            bindPreferenceState()
        }

        autoRefreshSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (suppressSwitchListeners) {
                return@setOnCheckedChangeListener
            }
            if (!AppPreferences.isAutoSyncEnabled(this) && isChecked) {
                AppPreferences.setAutoSyncEnabled(this, true)
            }
            AppPreferences.setAutoRefreshSnapshotOnLaunch(this, isChecked)
            bindPreferenceState()
        }

        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (suppressSwitchListeners) {
                return@setOnCheckedChangeListener
            }
            if (isChecked) {
                enableReminder()
            } else {
                AppPreferences.setReminderEnabled(this, false)
                DailyReminderScheduler.cancel(this)
                bindPreferenceState()
            }
        }

        reminderTimeButton.setOnClickListener { showReminderTimePicker() }
        syncNowButton.setOnClickListener { syncNow() }
        refreshSnapshotButton.setOnClickListener { refreshSnapshotNow() }
        clearDataButton.setOnClickListener { confirmClearOfflineData() }
        logoutButton.setOnClickListener { logout() }
    }

    private fun renderAccountState() {
        val username = AppPreferences.loadUsername(this) ?: getString(R.string.main_saved_session)
        accountStatusView.text = getString(R.string.settings_account_value, username)
        serverUrlView.text = getString(R.string.settings_server_value, BuildConfig.BASE_URL)
    }

    private fun bindPreferenceState() {
        val reminderEnabled = AppPreferences.isReminderEnabled(this)
        val reminderMinute = AppPreferences.loadReminderMinute(this)
        val reminderTime = formatMinuteOfDay(reminderMinute)
        val autoSyncEnabled = AppPreferences.isAutoSyncEnabled(this)
        val refreshSnapshotEnabled = AppPreferences.shouldRefreshSnapshotOnLaunch(this)
        val sessionNeedsReauth = AppPreferences.isSessionReauthRequired(this)

        suppressSwitchListeners = true
        autoSyncSwitch.isChecked = autoSyncEnabled
        autoRefreshSwitch.isChecked = refreshSnapshotEnabled
        reminderSwitch.isChecked = reminderEnabled
        suppressSwitchListeners = false

        autoRefreshSwitch.isEnabled = autoSyncEnabled && !busy
        reminderTimeButton.text = getString(R.string.reminder_time_button, reminderTime)
        reminderTimeButton.isEnabled = !busy && reminderEnabled
        syncWarningView.visibility = if (sessionNeedsReauth) View.VISIBLE else View.GONE
    }

    private fun refreshOfflineSummary() {
        val summary = OfflineDataManager.readLocalSummary(this)
        val lastDeckLabel = if (summary.lastDeckUid == "all") {
            getString(R.string.all_decks)
        } else {
            summary.lastDeckUid
        }
        offlineSummaryView.text = getString(
            R.string.settings_offline_summary_value,
            summary.cardCount,
            summary.pendingCount,
            lastDeckLabel
        )
        renderSnapshotIndicator()
    }

    private fun renderSnapshotIndicator() {
        val status = AppPreferences.loadSnapshotRefreshStatus(this)
        if (status == null) {
            snapshotIndicatorView.visibility = View.GONE
            return
        }

        val refreshedAt = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
            .format(Date(status.refreshedAtEpochMs))

        snapshotIndicatorView.text = if (status.incremental) {
            getString(
                R.string.settings_snapshot_indicator_incremental,
                status.upsertCount,
                status.removedCount,
                status.unchangedCount,
                status.cardCount,
                refreshedAt
            )
        } else {
            getString(
                R.string.settings_snapshot_indicator_full,
                status.cardCount,
                refreshedAt
            )
        }
        snapshotIndicatorView.visibility = View.VISIBLE
    }

    private fun enableReminder() {
        if (hasNotificationPermission()) {
            AppPreferences.setReminderEnabled(this, true)
            DailyReminderScheduler.sync(this)
            bindPreferenceState()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            AppPreferences.setReminderEnabled(this, true)
            DailyReminderScheduler.sync(this)
            bindPreferenceState()
        }
    }

    private fun showReminderTimePicker() {
        val minuteOfDay = AppPreferences.loadReminderMinute(this)
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                AppPreferences.saveReminderMinute(this, hourOfDay * 60 + minute)
                if (AppPreferences.isReminderEnabled(this)) {
                    DailyReminderScheduler.sync(this)
                }
                bindPreferenceState()
            },
            minuteOfDay / 60,
            minuteOfDay % 60,
            true
        ).show()
    }

    private fun syncNow() {
        if (busy) {
            return
        }
        if (AppPreferences.isSessionReauthRequired(this)) {
            toast(getString(R.string.settings_session_reauth_click_message))
            return
        }
        setBusy(true)
        Thread {
            try {
                val result = OfflineDataManager.syncPendingAndRefreshLastDeck(this)
                runOnUiThread {
                    refreshOfflineSummary()
                    toast(
                        getString(
                            R.string.offline_sync_done,
                            result.accepted,
                            result.duplicates,
                            result.errorCount
                        )
                    )
                }
            } catch (exc: Exception) {
                runOnUiThread {
                    val fallback = if (exc.message == "sync_in_progress") {
                        getString(R.string.offline_sync_running)
                    } else {
                        getString(R.string.settings_sync_failed)
                    }
                    toast(exc.message ?: fallback)
                }
            } finally {
                runOnUiThread { setBusy(false) }
            }
        }.start()
    }

    private fun refreshSnapshotNow() {
        if (busy) {
            return
        }
        if (AppPreferences.isSessionReauthRequired(this)) {
            toast(getString(R.string.settings_session_reauth_click_message))
            return
        }
        setBusy(true)
        Thread {
            try {
                val result = OfflineDataManager.downloadSnapshotForLastDeck(this)
                runOnUiThread {
                    refreshOfflineSummary()
                    toast(getString(R.string.offline_download_done, result.cardCount))
                }
            } catch (exc: Exception) {
                runOnUiThread {
                    val fallback = if (exc.message == "sync_in_progress") {
                        getString(R.string.offline_sync_running)
                    } else {
                        getString(R.string.settings_snapshot_failed)
                    }
                    toast(exc.message ?: fallback)
                }
            } finally {
                runOnUiThread { setBusy(false) }
            }
        }.start()
    }

    private fun confirmClearOfflineData() {
        if (busy) {
            return
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.settings_clear_data_title)
            .setMessage(R.string.settings_clear_data_message)
            .setNegativeButton(R.string.settings_cancel, null)
            .setPositiveButton(R.string.settings_clear_data_confirm) { _, _ ->
                clearOfflineData()
            }
            .show()
    }

    private fun clearOfflineData() {
        if (busy) {
            return
        }
        setBusy(true)
        Thread {
            try {
                OfflineDataManager.clearOfflineData(this)
                runOnUiThread {
                    refreshOfflineSummary()
                    toast(getString(R.string.settings_clear_data_done))
                }
            } finally {
                runOnUiThread { setBusy(false) }
            }
        }.start()
    }

    private fun logout() {
        if (busy) {
            return
        }
        setBusy(true)
        Thread {
            try {
                AuthApiClient.logout(BuildConfig.BASE_URL, AppPreferences.loadCookieHeader(this))
            } catch (_: Exception) {
                // Best effort.
            } finally {
                AppPreferences.clearSession(this)
                runOnUiThread {
                    val intent = Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    private fun setBusy(enabled: Boolean) {
        busy = enabled
        loadingView.visibility = if (enabled) View.VISIBLE else View.GONE
        backButton.isEnabled = !enabled
        autoSyncSwitch.isEnabled = !enabled
        autoRefreshSwitch.isEnabled = !enabled && AppPreferences.isAutoSyncEnabled(this)
        reminderSwitch.isEnabled = !enabled
        reminderTimeButton.isEnabled = !enabled && AppPreferences.isReminderEnabled(this)
        syncNowButton.isEnabled = !enabled
        refreshSnapshotButton.isEnabled = !enabled
        clearDataButton.isEnabled = !enabled
        logoutButton.isEnabled = !enabled
    }

    private fun redirectToLogin() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private fun hasNotificationPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }
        return checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
    }

    private fun formatMinuteOfDay(minuteOfDay: Int): String {
        val safeMinute = minuteOfDay.coerceIn(0, 23 * 60 + 59)
        return String.format(Locale.getDefault(), "%02d:%02d", safeMinute / 60, safeMinute % 60)
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
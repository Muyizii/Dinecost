package muyizii.s.dinecost

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object MainSettingKeys {
    val ONGOING_NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
    val AUTO_RECORDER_MODE = stringPreferencesKey("auto_recorder_mode")
}
package muyizii.s.dinecost.helpers

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import muyizii.s.dinecost.MainSettingKeys
import muyizii.s.dinecost.SettingState

class SettingHelper(
    private val settingDataStore: DataStore<Preferences>
) {
    private val _settingState = MutableStateFlow(SettingState())
    val settingState: StateFlow<SettingState> = _settingState.asStateFlow()

    suspend fun getOnGoingNotificationEnabled() {
        settingDataStore.data
            .map { preferences ->
                preferences[MainSettingKeys.ONGOING_NOTIFICATION_ENABLED] ?: false
            }
            .collect { enabled ->
                _settingState.update { currentState ->
                    currentState.copy(onGoingNotificationEnabled = enabled)
                }
            }
    }

    suspend fun getAutoRecorderMode(){
        settingDataStore.data
            .map { preferences ->
                preferences[MainSettingKeys.AUTO_RECORDER_MODE] ?: "None"
            }
            .collect { mode ->
                _settingState.update { currentState ->
                    currentState.copy(autoRecorderMode = mode)
                }
            }
    }

    suspend fun changeOnGoingNotificationEnabled(enable: Boolean) {
        settingDataStore.edit { mainSetting ->
            mainSetting[MainSettingKeys.ONGOING_NOTIFICATION_ENABLED] = enable
        }
        _settingState.update { currentState ->
            currentState.copy(onGoingNotificationEnabled = enable)
        }
        Log.d("SettingHelper", "发送常驻通知功能被：" + if (enable) "启用" else "停用")
    }

    suspend fun changeAutoRecorderMode(mode: String){
        settingDataStore.edit { mainSetting ->
            mainSetting[MainSettingKeys.AUTO_RECORDER_MODE] = mode
        }
        _settingState.update { currentState ->
            currentState.copy(autoRecorderMode = mode)
        }
        Log.d("SettingHelper", "自动记账模式被设定为：$mode")
    }
}
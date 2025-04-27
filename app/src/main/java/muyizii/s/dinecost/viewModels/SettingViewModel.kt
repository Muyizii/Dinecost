package muyizii.s.dinecost.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import muyizii.s.dinecost.helpers.DatabaseHelper
import muyizii.s.dinecost.helpers.NotificationHelper
import muyizii.s.dinecost.helpers.SettingHelper
import muyizii.s.dinecost.SettingState
import muyizii.s.dinecost.helpers.PermissionHelper

class SettingViewModel(
    private val databaseHelper: DatabaseHelper,
    private val settingHelper: SettingHelper,
    private val notificationHelper: NotificationHelper,
    private val permissionHelper: PermissionHelper
) : ViewModel() {
    val settingState: StateFlow<SettingState> = settingHelper.settingState

    private val _blockDialog = MutableStateFlow(false)
    val blockDialog: StateFlow<Boolean> = _blockDialog.asStateFlow()

    init {
        getOnGoingNotificationEnabled()
        getAutoRecorderMode()
        if (settingState.value.onGoingNotificationEnabled) {
            changeOnGoingNotificationEnabled(true)
        }
    }

    fun getOnGoingNotificationEnabled() = viewModelScope.launch {
        settingHelper.getOnGoingNotificationEnabled()
    }

    fun getAutoRecorderMode() = viewModelScope.launch {
        settingHelper.getAutoRecorderMode()
    }

    fun changeOnGoingNotificationEnabled(enable: Boolean): String {
        if (enable) {
            Log.d("SettingViewModel", "尝试切换持久通知状态")
            if (!permissionHelper.isNotificationPermissionGranted()) {
                Log.d("SettingViewModel", "通知权限未授予")
                return "PermissionDie"
            }
            if (!notificationHelper.areAppNotificationsEnabled()) {
                Log.d("SettingViewModel", "通知开关被关闭")
                return "SettingDie"
            }
            if (!notificationHelper.isNotificationChannelEnabled("1")) {
                Log.d("SettingViewModel", "余额提醒通知通道被关闭")
                return "ChannelDie"
            }
            viewModelScope.launch {
                settingHelper.changeOnGoingNotificationEnabled(enable)
            }
            Log.d("SettingViewModel", "权限开关通道都可行")
            return "Able"
        } else {
            viewModelScope.launch {
                settingHelper.changeOnGoingNotificationEnabled(enable)
            }
            return "Disable"
        }
    }

    fun changeAutoRecorderMode(mode: String): String {
        if (mode == "Sms") {
            Log.d("SettingViewModel", "尝试改变自动记录模式到短信模式")
            if (!permissionHelper.isNotificationPermissionGranted()) {
                Log.d("SettingViewModel", "通知权限未授予")
                return "PermissionDie"
            }
            if (!notificationHelper.areAppNotificationsEnabled()) {
                Log.d("SettingViewModel", "通知开关被关闭")
                return "SettingDie"
            }
            if (!notificationHelper.isNotificationChannelEnabled("3")) {
                Log.d("SettingViewModel", "短信自动记账提醒通知通道被关闭")
                return "ChannelDie"
            }
            if (!permissionHelper.checkSmsPermission()) {
                Log.d("SettingViewModel", "短信权限未授予")
                return "SmsPermissionDie"
            }
            viewModelScope.launch {
                settingHelper.changeAutoRecorderMode(mode)
            }

            return "Sms"
        } else if (mode == "A11y") {
            Log.d("SettingViewModel", "尝试改变自动记录模式到无障碍模式")
            if (!permissionHelper.isNotificationPermissionGranted()) {
                Log.d("SettingViewModel", "通知权限未授予")
                return "PermissionDie"
            }
            if (!notificationHelper.areAppNotificationsEnabled()) {
                Log.d("SettingViewModel", "通知开关被关闭")
                return "SettingDie"
            }
            if (!notificationHelper.isNotificationChannelEnabled("4")) {
                Log.d("SettingViewModel", "无障碍自动记账提醒通知通道被关闭")
                return "ChannelDie"
            }
            if (!permissionHelper.checkWindowPermission()) {
                Log.d("SettingViewModel", "悬浮窗权限未授予")
                return "PermissionWindowDie"
            }
            viewModelScope.launch {
                settingHelper.changeAutoRecorderMode(mode)
            }

            return "A11y"
        } else {
            viewModelScope.launch {
                settingHelper.changeAutoRecorderMode(mode)
            }
            return "None"
        }
    }

    fun reGenerateTotalInOut() {
        viewModelScope.launch {
            _blockDialog.update { currentState ->
                true
            }
            val blockScreen = databaseHelper.reFlushDatabaseByDetailRecord()
            delay(2000)
            _blockDialog.update { currentState ->
                blockScreen
            }
        }
    }
}
package muyizii.s.dinecost.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import muyizii.s.dinecost.R
import muyizii.s.dinecost.viewModels.SettingViewModel

@Composable
fun AutoRecorderManageScreen(
    settingViewModel: SettingViewModel,
    navController: NavHostController
) {
    val settingState by settingViewModel.settingState.collectAsState()

    var autoRecorderModeState by remember { mutableStateOf("") }

    var showSmsPermissionDialog by remember { mutableStateOf(false) }
    var showWindowPermissionDialog by remember { mutableStateOf(false) }
    var showChangeAutoModeToSmsDialog by remember { mutableStateOf(false) }
    var showChangeAutoModeToAccessDialog by remember { mutableStateOf(false) }
    var showNotificationPermissionDialog by remember { mutableStateOf(false) }
    var showAppNotificationDisableDialog by remember { mutableStateOf(false) }
    var showChannelDisableDialog by remember { mutableStateOf(false) }

    val options = listOf("无", "短信模式", "无障碍模式")

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = "设置自动记账模式",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (
                                        if (option == "无") settingState.autoRecorderMode == "None"
                                        else if (option == "短信模式") settingState.autoRecorderMode == "Sms"
                                        else settingState.autoRecorderMode == "A11y"
                                        ),
                                onClick = {
                                    when (option) {
                                        "短信模式" -> {
                                            autoRecorderModeState = settingViewModel.changeAutoRecorderMode("Sms")
                                            when (autoRecorderModeState) {
                                                "Sms" -> showChangeAutoModeToSmsDialog = true

                                                "SmsPermissionDie" -> showSmsPermissionDialog = true

                                                "PermissionDie" -> showNotificationPermissionDialog = true

                                                "SettingDie" -> showAppNotificationDisableDialog = true

                                                "ChannelDie" -> showChannelDisableDialog = true
                                            }
                                        }

                                        "无障碍模式" -> {
                                            autoRecorderModeState = settingViewModel.changeAutoRecorderMode("A11y")
                                            when (autoRecorderModeState) {
                                                "A11y" -> showChangeAutoModeToAccessDialog = true

                                                "PermissionDie" -> showNotificationPermissionDialog = true

                                                "PermissionWindowDie" -> showWindowPermissionDialog = true

                                                "SettingDie" -> showAppNotificationDisableDialog = true

                                                "ChannelDie" -> showChannelDisableDialog = true
                                            }
                                        }

                                        "无" -> {
                                            autoRecorderModeState = settingViewModel.changeAutoRecorderMode("None")
                                        }
                                    }
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                    ) {
                        RadioButton(
                            selected = (
                                    if (option == "无") settingState.autoRecorderMode == "None"
                                    else if (option == "短信模式") settingState.autoRecorderMode == "Sms"
                                    else settingState.autoRecorderMode == "A11y"
                                    ),
                            onClick = null
                        )
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        if (option == "短信模式") {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                contentDescription = "短信模式信息",
                                modifier = Modifier.padding(start = 16.dp).clickable {
                                    navController.navigate("SHOW_AUTO_RECORDER_MODE_SMS_INFO_SCREEN")
                                }
                            )
                        } else if (option == "无障碍模式") {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_info),
                                contentDescription = "无障碍模式信息",
                                modifier = Modifier.padding(start = 16.dp).clickable {
                                    navController.navigate("SHOW_AUTO_RECORDER_MODE_A11Y_INFO_SCREEN")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showWindowPermissionDialog) {
        ShowWindowPermissionDisableDialog(
            onDismissRequest = { showWindowPermissionDialog = false }
        )
    }
    if (showNotificationPermissionDialog) {
        ShowNotificationPermissionDisableDialog(
            onDismissRequest = { showNotificationPermissionDialog = false }
        )
    }
    if (showAppNotificationDisableDialog) {
        ShowAppNotificationClosedDialog(
            onDismissRequest = { showAppNotificationDisableDialog = false }
        )
    }
    if (showChannelDisableDialog) {
        ShowChannelDisableDialog(
            onDismissRequest = { showChannelDisableDialog = false }
        )
    }
    if (showSmsPermissionDialog) {
        ShowSmsPermissionDisableDialog(
            onDismissRequest = { showSmsPermissionDialog = false }
        )
    }
    if (showChangeAutoModeToSmsDialog) {
        ShowChangeAutoRecordToSms(
            onDismissRequest = { showChangeAutoModeToSmsDialog = false }
        )
    }
    if (showChangeAutoModeToAccessDialog) {
        ShowChangeAutoRecordToAccess(
            onDismissRequest = { showChangeAutoModeToAccessDialog = false }
        )
    }
}
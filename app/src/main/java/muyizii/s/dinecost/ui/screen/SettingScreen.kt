package muyizii.s.dinecost.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import muyizii.s.dinecost.R
import muyizii.s.dinecost.viewModels.SettingViewModel

@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel,
    navController: NavHostController
) {
    val settingState by settingViewModel.settingState.collectAsState()

    val blockDialog by settingViewModel.blockDialog.collectAsState()

    var checkedOfOnGoingNotification by remember { mutableStateOf(settingState.onGoingNotificationEnabled) }

    var ongoingNotificationState by remember { mutableStateOf("") }

    var showNotificationPermissionDialog by remember { mutableStateOf(false) }
    var showAppNotificationDisableDialog by remember { mutableStateOf(false) }
    var showChannelDisableDialog by remember { mutableStateOf(false) }
    var showCommitReGenerateTotalInOutDialog by remember { mutableStateOf(false) }

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
                text = "设置",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(0.90f),
                content = {
                    item {
                        Text(
                            text = "通知",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "发送常驻通知以辅助记账"
                                )
                                Text(
                                    text = "需要通知权限",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Switch(
                                checked = checkedOfOnGoingNotification,
                                onCheckedChange = {
                                    ongoingNotificationState =
                                        settingViewModel.changeOnGoingNotificationEnabled(!checkedOfOnGoingNotification)
                                    when (ongoingNotificationState) {
                                        "Disable" -> {
                                            checkedOfOnGoingNotification = false
                                        }

                                        "Able" -> {
                                            checkedOfOnGoingNotification = true
                                        }

                                        "PermissionDie" -> {
                                            checkedOfOnGoingNotification = false
                                            showNotificationPermissionDialog = true
                                        }

                                        "SettingDie" -> {
                                            checkedOfOnGoingNotification = false
                                            showAppNotificationDisableDialog = true
                                        }

                                        "ChannelDie" -> {
                                            checkedOfOnGoingNotification = false
                                            showChannelDisableDialog = true
                                        }
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(
                            text = "数据",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "根据具体记录重置余额"
                            )
                            Button(
                                onClick = {
                                    showCommitReGenerateTotalInOutDialog = true
                                }
                            ) { Text(text = "重置") }
                        }
                        Spacer(modifier = Modifier.padding(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "自动记账模式"
                            )
                            Row(
                                modifier = Modifier.clickable { navController.navigate("AUTO_RECORDER_MANAGE_SCREEN") }
                            ) {
                                Text(
                                    text = when (settingState.autoRecorderMode) {
                                        "None" -> "无"
                                        "Sms" -> "短信模式"
                                        "A11y" -> "无障碍模式"
                                        else -> "出错了"
                                    }
                                )
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_arrow_right),
                                    contentDescription = "进入",
                                )
                            }
                        }

                        Spacer(modifier = Modifier.padding(10.dp))

                        Text(
                            text = "其它",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.padding(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "关于"
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right),
                                contentDescription = "进入",
                                modifier = Modifier.clickable { navController.navigate("ABOUT_SCREEN") }
                            )
                        }
                    }
                }
            )
        }
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
    if (showCommitReGenerateTotalInOutDialog) {
        ShowCommitReGenTotInOutDialog(
            onDismissRequest = { showCommitReGenerateTotalInOutDialog = false },
            settingViewModel = settingViewModel
        )
    }

    if (blockDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,  // 禁止返回键关闭
                dismissOnClickOutside = false // 禁止点击外部关闭
            )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(200.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(Modifier.height(16.dp))
                    Text("数据库操作中。请勿离开此页面。")
                }
            }
        }
    }
}
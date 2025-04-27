package muyizii.s.dinecost.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import muyizii.s.dinecost.R


@Composable
fun ShowAutoRecorderModeA11yInfoScreen(
    navController: NavHostController
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "回退",
                modifier = Modifier.padding(20.dp).clickable { navController.popBackStack() }
            )
            Text(
                modifier = Modifier,
                text = "关于自动记账-无障碍模式",
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
                            text = "工作原理\n" +
                                    "通过系统无障碍服务实时检测屏幕内容（注：本应用不会存储/上传任何屏幕信息），当检测到指定应用的支付完成页面时，自动提取交易信息并利用悬浮窗确认是否需要记录。\n\n" +
                                    "所需权限\n" +
                                    "• 无障碍权限\n" +
                                    "• 悬浮窗权限\n" +
                                    "• 通知权限\n\n" +
                                    "系统设置建议\n" +
                                    "1. 前往「系统设置-电池」为本应用开启无限制模式\n" +
                                    "2. 部分国产设备需额外开启：\n" +
                                    "   - 自启动权限\n" +
                                    "   - 后台弹出界面权限\n\n" +
                                    "支持软件\n" +
                                    "微信\n" +
                                    "支付宝\n\n" +
                                    "重要提醒\n" +
                                    "• 启用后将显示常驻通知（系统强制要求，关闭会导致功能失效）\n" +
                                    "• 需手动到「系统设置-无障碍」中启用应用的无障碍服务\n" +
                                    "• 部分EMUI/MIUI系统需单独授权「显示在其他应用上层」\n" +
                                    "• 随着支付软件的更新，该模式可能会失效，请尝试获取最新版本软件\n",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            )
        }
    }
}

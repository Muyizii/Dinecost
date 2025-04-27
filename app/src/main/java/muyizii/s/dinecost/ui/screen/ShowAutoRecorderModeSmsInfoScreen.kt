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
fun ShowAutoRecorderModeSmsInfoScreen(
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
                text = "关于自动记账-短信模式",
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
                                    "通过监听系统短信广播，自动解析新收到短信的金额与信息（注：本应用不会存储/上传您的原始短信内容），适用于所有消费均有银行短信通知的场景。\n\n" +
                                    "所需权限\n" +
                                    "• 短信接受/读取权限\n" +
                                    "• 通知权限\n\n" +
                                    "系统设置建议\n" +
                                    "为保证后台持续运行，建议：\n" +
                                    "1. 在系统电池设置中为本应用开启「无限制」电量优化\n" +
                                    "2. 对于部分国产机型，需额外开启自启动/后台运行权限\n\n" +
                                    "重要提醒\n" +
                                    "• 启用后将显示常驻通知（系统强制要求，关闭会导致功能失效）\n" +
                                    "• 仅支持简体中文短信模板\n" +
                                    "• 若您的短信格式未被识别，请尝试联系开发者进行适配，并提供短信内容（隐藏敏感信息）\n" +
                                    "• 首次使用建议验证解析准确性\n",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            )
        }
    }
}
package muyizii.s.dinecost.a11y

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun FloatingWindowContent(
    onClose: () -> Unit,
    onCommit: () -> Unit,
    amount: String,
    packageName: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.28f)
                .background(MaterialTheme.colorScheme.background)
                .graphicsLayer { clip = true }
                .clip(RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "检测到了支付信息，是否记录如下数据？",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = "金额 $amount",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "来自" + if (packageName == "com.tencent.mm") "微信" else "支付宝",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.padding(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onClose) {
                        Text("取消")
                    }
                    TextButton(onClick = onCommit) {
                        Text("记录")
                    }
                }
            }
        }
    }
}
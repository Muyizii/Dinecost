package muyizii.s.dinecost.a11y

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import muyizii.s.dinecost.MainApplication
import muyizii.s.dinecost.data.detailRecord.DetailRecord
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class FloatingWindowActivity : ComponentActivity() {
    private var amount: String = ""
    private var packageName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 获取Intent中的数据
        amount = intent.getStringExtra("amount") ?: "NONE"
        packageName = intent.getStringExtra("packageName") ?: "NONE"

        if (amount == "NONE" || packageName == "NONE") {
            Log.e("FloatingWindowActivity", "悬浮窗没有接受到对的值 $amount $packageName")
            finish()
            return
        }

        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val availableWidth = rect.width()
        val availableHeight = rect.height()
        val windowWidth = availableWidth * 0.7
        val windowHeight = availableHeight * 0.3

        window.setBackgroundDrawableResource(android.R.color.transparent)

        // 设置窗口属性
        window.apply {
            setLayout(availableWidth.toInt(), availableHeight.toInt())

            attributes.gravity = Gravity.TOP or Gravity.START
            attributes.x = ((availableWidth - windowWidth) / 2).toInt()
            attributes.y = ((availableHeight - windowHeight) / 2).toInt()
        }

        setContent {
            FloatingWindowContent(
                onClose = { finish() },
                onCommit = { onCommitAutoData() },
                amount = amount,
                packageName = packageName
            )
        }
    }

    private fun onCommitAutoData() {
        val application = applicationContext as? MainApplication
        application?.let {
            CoroutineScope(Dispatchers.IO).launch {
                application.container.databaseHelper.addDetailRecord(
                    DetailRecord(
                        amount = BigDecimal(amount.toDouble()).setScale(2, RoundingMode.HALF_UP).toDouble(),
                        isIncome = false,
                        isPatch = true,
                        date = LocalDate.now(),
                        type = "等待添加",
                        subType = "",
                        detail = "来自" +
                                when (packageName) {
                                    "com.tencent.mm" -> "微信"
                                    "com.eg.android.AlipayGphone" -> "支付宝"
                                    else -> "错误"
                                } + "的自动记录"
                    )
                )
                val totalIn = application.container.databaseHelper.getFirstDateIn()
                val totalOut = application.container.databaseHelper.getFirstDateOut()
                val todaySpend = application.container.databaseHelper.getDateOutByDate(LocalDate.now())

                application.container.notificationHelper.sendNewAutoNotification()

                if(totalIn != null && totalOut != null && todaySpend != null) {
                    application.container.notificationHelper.sendOngoingNotification(
                        todaySpend = (todaySpend.amount.roundToDecimal(2)).toString(),
                        totalHave = (totalIn.amount - totalOut.amount).roundToDecimal(2).toString()
                    )
                }
                finish() // 记录完成后关闭窗口
            }
        }
    }

    private fun Double.roundToDecimal(decimals: Int): Double {
        return BigDecimal(this).setScale(decimals, RoundingMode.HALF_UP).toDouble()
    }
}
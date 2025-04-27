package muyizii.s.dinecost.sms

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import muyizii.s.dinecost.helpers.DatabaseHelper
import muyizii.s.dinecost.helpers.NotificationHelper
import muyizii.s.dinecost.data.detailRecord.DetailRecord
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

class SmsRepositoryImpl(
    private val context: Context,
    private val databaseHelper: DatabaseHelper,
    private val notificationHelper: NotificationHelper
) : SmsRepository {
    private val _latestMessages = MutableStateFlow<List<SmsMessage>>(emptyList())
    override val latestMessages: Flow<List<SmsMessage>> = _latestMessages

    override suspend fun processSmsMessage(message: SmsMessage) {
        Log.d("SmsRepository", "接收到新短信")
        val currentList = _latestMessages.value.toMutableList()
        currentList.add(0, message)
        _latestMessages.value = currentList

        val (isBank, isPayment, amount) = parseSms(message.content)
        if (isBank && isPayment && amount != null) {
            Log.d("SmsRepository", "新短信的支出记录")
            databaseHelper.addDetailRecord(
                DetailRecord(
                    amount = BigDecimal(amount.toDouble()).setScale(2, RoundingMode.HALF_UP).toDouble(),
                    isIncome = false,
                    isPatch = true,
                    date = LocalDate.now(),
                    type = "等待添加",
                    subType = "",
                    detail = "来自短信的自动记录"
                )
            )

            val totalIn = databaseHelper.getFirstDateIn()
            val totalOut = databaseHelper.getFirstDateOut()
            val todaySpend = databaseHelper.getDateOutByDate(LocalDate.now())

            notificationHelper.sendNewAutoNotification()

            if (totalIn != null && totalOut != null && todaySpend != null) {
                notificationHelper.sendOngoingNotification(
                    todaySpend = (todaySpend.amount.roundToDecimal(2)).toString(),
                    totalHave = (totalIn.amount - totalOut.amount).roundToDecimal(2).toString()
                )
            }
        }
    }

    private fun parseSms(sms: String): Triple<Boolean, Boolean, Double?> {
        // 检查是否是银行短信
        val isBank = sms.contains(Regex("【.*?银行】"))

        // 判断是否为支付/支取/支出的短信
        val paymentKeywords = listOf("支出", "支取", "消费", "付款", "支付")
        val isPayment = paymentKeywords.any { sms.contains(it) }

        // 使用正则提取非“余额”字段的金额（避免提取余额）
        // 匹配 "[非余额前缀] + 金额 + 元"，支持整数和小数
        val amountRegex = Regex("""(?<!余额)(?<!交易后余额)(?<!余额为)(?<!余额)(?<!余)([\d,]+\.\d+|\d+)元""")

        // 第一个出现的金额通常是支付金额（因为余额在后面）
        val amountMatch = amountRegex.find(sms)
        val amount = amountMatch?.value
            ?.replace("元", "")
            ?.replace(",", "")
            ?.toDoubleOrNull()

        return Triple(isBank, isPayment, amount)
    }

    private fun Double.roundToDecimal(decimals: Int): Double {
        return BigDecimal(this).setScale(decimals, RoundingMode.HALF_UP).toDouble()
    }
}
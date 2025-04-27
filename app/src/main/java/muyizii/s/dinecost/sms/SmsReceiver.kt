package muyizii.s.dinecost.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import muyizii.s.dinecost.MainApplication
import android.util.Log

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("SmsReceiver", "接收到广播")
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            messages.forEach { smsMessage ->
                val sender = smsMessage.displayOriginatingAddress
                val content = smsMessage.displayMessageBody
                val timestamp = System.currentTimeMillis()

                val message = SmsMessage(
                    sender = sender,
                    content = content,
                    timestamp = timestamp
                )

                // 使用Application的container处理短信
                val appContainer = (context.applicationContext as MainApplication).container
                CoroutineScope(Dispatchers.IO).launch {
                    appContainer.smsRepository.processSmsMessage(message)
                }
            }
        }
    }
}
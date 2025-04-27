package muyizii.s.dinecost.sms

data class SmsMessage(
    val id: Long = 0,
    val sender: String,
    val content: String,
    val timestamp: Long,
    val isRead: Boolean = false
)
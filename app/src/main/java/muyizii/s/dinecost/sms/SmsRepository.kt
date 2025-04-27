package muyizii.s.dinecost.sms

import kotlinx.coroutines.flow.Flow

interface SmsRepository {
    val latestMessages: Flow<List<SmsMessage>>
    suspend fun processSmsMessage(message: SmsMessage)
}
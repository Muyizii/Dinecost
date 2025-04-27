package muyizii.s.dinecost.sms

import android.Manifest
import android.app.Notification
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import muyizii.s.dinecost.R

class SmsForegroundService : Service() {
    private var smsReceiver: BroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()

        Log.d("SmsForegroundService", "SmsForegroundService被创建")
        val filter = IntentFilter("android.provider.Telephony.SMS_RECEIVED").apply {
            priority = 999
        }
        smsReceiver = SmsReceiver()
        registerReceiver(
            smsReceiver,
            filter,
            Manifest.permission.BROADCAST_SMS,
            null
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        return START_STICKY
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "3")
            .setContentTitle("自动记录运行中")
            .setContentText("短信模式")
            .setSmallIcon(R.drawable.ic_list)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SmsForegroundService", "SmsForegroundService被销毁")
        smsReceiver?.let { unregisterReceiver(it) }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val NOTIFICATION_ID = 1001
    }
}
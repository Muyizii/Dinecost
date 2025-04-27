package muyizii.s.dinecost.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import muyizii.s.dinecost.MainActivity
import muyizii.s.dinecost.R

class NotificationHelper(
    private val context: Context
) {
    companion object {
        const val ONGOING_CHANNEL_ID = "1"
        const val ONGOING_CHANNEL_NAME = "余额提醒"
        const val NEW_AUTO_CHANNEL_ID = "2"
        const val NEW_AUTO_CHANNEL_NAME = "出现新的自动记账提醒"
        const val SMS_AUTO_CHANNEL_ID = "3"
        const val SMS_AUTO_CHANNEL_NAME = "短信自动记账服务"
        const val ACC_AUTO_CHANNEL_ID = "4"
        const val ACC_AUTO_CHANNEL_NAME = "无障碍自动记账服务"
    }

    private val notificationManager: NotificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    // 创建通知渠道
    fun createNotificationChannel() {
        val channel = NotificationChannel(
            ONGOING_CHANNEL_ID,
            ONGOING_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "用于提醒余额并且快速记账"
        }
        val newAutoChannel = NotificationChannel(
            NEW_AUTO_CHANNEL_ID,
            NEW_AUTO_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply{
            description = "用于提醒新的自动记账"
        }
        val smsAutoChannel = NotificationChannel(
            SMS_AUTO_CHANNEL_ID,
            SMS_AUTO_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply{
            description = "用于短信自动记账服务"
        }
        val accAutoChannel = NotificationChannel(
            ACC_AUTO_CHANNEL_ID,
            ACC_AUTO_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply{
            description = "用于无障碍自动记账服务"
        }
        notificationManager.createNotificationChannel(channel)
        notificationManager.createNotificationChannel(newAutoChannel)
        notificationManager.createNotificationChannel(smsAutoChannel)
        notificationManager.createNotificationChannel(accAutoChannel)
    }

    // 发送持久通知的方法
    fun sendOngoingNotification(totalHave: String, todaySpend: String) {
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "dine_cost://add_cash_screen".toUri(),
            context,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val title = "今日已支出：" + todaySpend + "元"
        val content = "余额:" + totalHave + "元    点击添加新的记录"

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            deepLinkIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, ONGOING_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_list)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOngoing(true)

        notificationManager.notify(1, builder.build())
    }

    // 发送新的自动记录被通知
    fun sendNewAutoNotification(){
        val deepLinkIntent = Intent(
            Intent.ACTION_VIEW,
            "dine_cost://detail_record_list_screen".toUri(),
            context,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            deepLinkIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, NEW_AUTO_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_list)
            .setContentTitle("有一项新的自动记账")
            .setContentText("点此查看今日记录")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(2, builder.build())
    }

    // 发送无障碍自动记账模式被下线
    fun sendA11yOfflineNotification(){
        val builder = NotificationCompat.Builder(context, ACC_AUTO_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_list)
            .setContentTitle("无障碍服务已下线")
            .setContentText("自动记帐模式被切换为其他模式或必需的权限被收回")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)

        notificationManager.notify(4, builder.build())
    }

    // 检测应用通知总开关
    fun areAppNotificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    // 检测特定通知频道状态
    fun isNotificationChannelEnabled(channelId: String): Boolean {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = manager.getNotificationChannel(channelId)

        return (channel != null && channel.importance != NotificationManager.IMPORTANCE_NONE)
    }
}
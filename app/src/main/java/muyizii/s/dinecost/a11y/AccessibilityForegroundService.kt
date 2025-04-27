package muyizii.s.dinecost.a11y

import android.accessibilityservice.AccessibilityService
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import muyizii.s.dinecost.MainApplication
import muyizii.s.dinecost.R

class AccessibilityForegroundService : AccessibilityService() {

    private var lastEventTime = 0L
    private var debounceInterval = 500L // 防抖间隔

    override fun onCreate() {
        super.onCreate()
        startSelfAsForeground()
    }

    override fun onServiceConnected() {
        val application = applicationContext as? MainApplication
        application?.let {
            CoroutineScope(Dispatchers.IO).launch {
                if (application.container.settingHelper.settingState.value.autoRecorderMode != "A11y") {
                    Log.d("A11yFS", "模式并非无障碍，关闭服务")
                    application.container.notificationHelper.sendA11yOfflineNotification()
                    stopSelf()
                }
            }
        }

        super.onServiceConnected()
        startSelfAsForeground()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        val application = applicationContext as? MainApplication
        application?.let {
            CoroutineScope(Dispatchers.IO).launch {
                if (application.container.settingHelper.settingState.value.autoRecorderMode != "A11y") {
                    Log.d("A11yFS", "模式并非无障碍，关闭服务")
                    application.container.notificationHelper.sendA11yOfflineNotification()
                    stopSelf()
                }
            }
        }

        if (event != null && event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            if (isDebounced()) return // 防抖检查
            Log.d("A11yFS", "防抖通过")

            debounceInterval = 500L

            if (event.source == null) return

            val packageName = event.packageName?.toString() ?: return

            val rootNode = rootInActiveWindow ?: return


            when (packageName) {
                "com.tencent.mm" -> {
                    Log.d("A11yFS", "判断为微信页面")

                    val xmlOutput = nodeToXmlString(rootNode)
                    Log.d("A11yFS_dump", xmlOutput)

                    val amount = isWeChatPaymentSuccess(xmlOutput)
                    if (amount != "NONE") {
                        onPaymentDetected(
                            amount = amount,
                            packageName = packageName
                        )
                    }
                }

                "com.eg.android.AlipayGphone" -> {
                    Log.d("A11yFS", "判断为支付宝页面")

                    val xmlOutput = nodeToXmlString(rootNode)
                    Log.d("A11yFS_dump", xmlOutput)

                    val amount = isAliPayPaymentSuccess(xmlOutput)
                    if (amount != "NONE") {
                        onPaymentDetected(
                            amount = amount,
                            packageName = packageName
                        )
                    }
                }
            }
            lastEventTime = System.currentTimeMillis() // 更新最后触发时间

        }
    }

    override fun onInterrupt() {}

    private fun startSelfAsForeground() {
        val notification = createNotification()
        startForeground(4, notification)
    }

    private fun createNotification(): Notification {
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            packageManager.getLaunchIntentForPackage(packageName),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, "4")
            .setContentTitle("自动记录运行中")
            .setContentText("无障碍模式")
            .setSmallIcon(R.drawable.ic_list)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()
    }

    private fun isDebounced(): Boolean {
        return (System.currentTimeMillis() - lastEventTime) < debounceInterval
    }

    private fun isWeChatPaymentSuccess(xmlContent: String): String {
        val hasContentDesc = "content-desc=\"支付成功\"".toRegex().containsMatchIn(xmlContent)

        val amountPattern = "text=\"￥(\\d+\\.\\d{2})\"".toRegex()
        val amount = amountPattern.find(xmlContent)?.groupValues?.get(1)

        if (!hasContentDesc || amount == null) {
            Log.d("A11yFS", "判断并非为微信支付成功")
            return "NONE"
        }

        Log.d("A11yFS", "判断为微信支付成功")
        return amount
    }

    private fun isAliPayPaymentSuccess(xmlContent: String): String {
        // 扫码支付成功检测
        val pattern = Regex("支付成功[^\\d￥¥]*[￥¥]\\s*(\\d+(?:\\.\\d+)?)")

        val amountNormal = pattern.find(xmlContent)?.groupValues?.get(1)
        if (amountNormal != null) {
            Log.d("A11yFS", "判断为支付宝扫码支付成功")
            return amountNormal.trim()
        }

        // 碰一下支付成功检测
        // 主要匹配：text="2.89" resource-id="com.alipay.mobile.ucdp:id/summary_amount_text"
        val regex = Regex("""text="(\d+\.?\d*)"\s+resource-id="com\.alipay\.mobile\.ucdp:id/summary_amount_text"""")

        val amountNFC = regex.find(xmlContent)?.groupValues?.get(1)
        if (amountNFC != null) {
            Log.d("A11yFS", "判断为支付宝碰一碰支付成功")
            return amountNFC.trim()
        }

        Log.d("A11yFS", "判断并非为支付宝支付成功")
        return "NONE"
    }

    private fun onPaymentDetected(amount: String, packageName: String) {
        Log.d("A11yFS", "接受到了金额为 $amount $packageName 的记录")
        Log.d("A11yFS", "启动悬浮窗服务")

        val application = applicationContext as? MainApplication
        application?.let {
            CoroutineScope(Dispatchers.IO).launch {
                if (!application.container.permissionHelper.checkWindowPermission()) {
                    Log.d("A11yFS", "用户关闭了悬浮窗权限,停止服务")
                    application.container.notificationHelper.sendA11yOfflineNotification()
                    stopSelf()
                }
            }
        }


        val intent = Intent(this, FloatingWindowActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("amount", amount)
            putExtra("packageName", packageName)
        }
        startActivity(intent)

        debounceInterval = 6000L // 在这里将防抖暂时延长到6s
    }
}
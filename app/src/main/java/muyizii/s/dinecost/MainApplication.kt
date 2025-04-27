package muyizii.s.dinecost

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import muyizii.s.dinecost.sms.SmsForegroundService

class MainApplication : Application() {
    lateinit var container: MainAppContainer
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        container = MainAppDataContainer(this)
        container.notificationHelper.createNotificationChannel()

        appScope.launch {
            container.settingHelper.settingState
                .map { it.autoRecorderMode }
                .distinctUntilChanged()
                .collect { mode ->
                    if (mode == "Sms") {
                        startSmsService()
                    } else {
                        stopSmsService()
                    }
                }
        }

        Log.d("MainApplication", "MainApplication被实例化")
    }

    override fun onTerminate() {
        appScope.cancel()
        super.onTerminate()
    }

    private fun startSmsService() {
        val serviceIntent = Intent(this, SmsForegroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }

    private fun stopSmsService() {
        val stopIntent = Intent(this, SmsForegroundService::class.java)
        stopService(stopIntent)
    }
}
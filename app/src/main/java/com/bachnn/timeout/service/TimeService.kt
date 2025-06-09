package com.bachnn.timeout.service

import android.app.ActivityManager
import android.app.ForegroundServiceStartNotAllowedException
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.bachnn.timeout.R
import com.bachnn.timeout.utilities.CHANNEL_HOME_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

class TimeService : Service() {

    private var job: Job? = null


    private var packageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null) {
                Log.e("TimeServiceTimeService", intent.action.toString())
                if (action == Intent.ACTION_PACKAGE_ADDED) {
                    // Handle package added
                    val packageName = intent.data!!.schemeSpecificPart
                    // Check if it's your target app
                    if (packageName == "com.example.targetapp") {
                        // Log or display message
                    }
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        registerReceiver(packageChangeReceiver, intentFilter)
        startLoopingFunction()
        return START_STICKY
    }

    private fun startForeground() {

        try {
            createNotificationChannel(this)
            val notification = NotificationCompat.Builder(this, CHANNEL_HOME_ID)
                // Create the notification to display while the service is running
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.app_name))
                .setContentTitle(getString(R.string.app_running))
                .build()
            ServiceCompat.startForeground(
                this,
                100,
                notification,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                } else {
                    0
                },
            )
        } catch (e: Exception) {
            Log.e("TimeServiceTimeService", e.toString())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                e is ForegroundServiceStartNotAllowedException
            ) {
            }
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_home_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_HOME_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startLoopingFunction() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(1000) // 5 seconds interval
            }
        }
    }

    override fun onDestroy() {
        job?.cancel()
        unregisterReceiver(packageChangeReceiver)
        super.onDestroy()
    }

}
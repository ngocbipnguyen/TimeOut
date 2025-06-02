package com.bachnn.timeout.service

import android.app.ActivityManager
import android.app.ForegroundServiceStartNotAllowedException
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.inputmethodservice.InputMethodService
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


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground()
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

    fun showLog() {
        try {
            var mLogcatProc: Process? = null
            var reader: BufferedReader? = null
            mLogcatProc = Runtime.getRuntime().exec(arrayOf("logcat", "-d"))
            reader = BufferedReader(InputStreamReader(mLogcatProc.inputStream))
            var line: String?
            val log = StringBuilder()
            val separator = System.getProperty("line.separator")
            while (reader.readLine().also { line = it } != null) {
                log.append(line)
                log.append(separator)
            }
            val w = log.toString()
            Log.e("TimeServiceTimeService", w)
        } catch (e: Exception) {
            Log.e("TimeServiceTimeService", e.message.toString())
        }
    }


    private fun startLoopingFunction() {
        val context: Context = this
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                openApp()
//                val packageName: String? = getForegroundApp(context)!!
//                Log.e("TimeServiceTimeService", "getForegroundApp $packageName")
                delay(1000) // 5 seconds interval
            }
        }
    }

    private fun openApp() {
        val am: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessInfo: List<ActivityManager.RunningAppProcessInfo> =
            am.runningAppProcesses;

//        for (int i = 0; i < runningAppProcessInfo.size(); i++) {
//            if(runningAppProcessInfo.get(i).processName.equals("com.the.app.you.are.looking.for") {
//                    // Do you stuff
//                }
//        }
//
        runningAppProcessInfo.forEach { it ->
            Log.e("TimeServiceTimeService", it.processName)
        }
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }


    private fun getForegroundApp(context: Context): String? {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val time = System.currentTimeMillis()
        val stats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            time - 1000 * 10,
            time
        )

        if (stats.isNullOrEmpty()) return null

        val recentStat = stats.maxByOrNull { it.lastTimeUsed }
        return recentStat?.packageName
    }


}
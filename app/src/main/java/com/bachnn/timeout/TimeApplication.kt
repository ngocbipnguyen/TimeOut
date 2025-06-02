package com.bachnn.timeout

import android.app.Application
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.bachnn.timeout.manager.PackageManager
import com.bachnn.timeout.service.TimeService
import dagger.hilt.android.HiltAndroidApp
import java.io.BufferedReader
import java.io.InputStreamReader


@HiltAndroidApp
class TimeApplication : Application(),LifeCycleDelegate {

    var packageManager: PackageManager? = null
    override fun onCreate() {
        super.onCreate()

        val lifeCycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifeCycleHandler)

        packageManager = PackageManager(applicationContext)

        val intent = Intent(this, TimeService::class.java) // Build the intent for the service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }

    }

    private fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }


    override fun onAppBackgrounded() {
        TODO("Not yet implemented")
    }

    override fun onAppForegrounded() {
        TODO("Not yet implemented")
    }

}
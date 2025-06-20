package com.bachnn.timeout

import android.app.Application
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.bachnn.timeout.manager.PackageManager
import com.bachnn.timeout.service.TimeService
import com.bachnn.timeout.worker.DailyResetWorker
import dagger.hilt.android.HiltAndroidApp
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

@HiltAndroidApp
class TimeApplication : Application(), Configuration.Provider {

    var packageManager: PackageManager? = null
    override fun onCreate() {
        super.onCreate()
        packageManager = PackageManager(applicationContext)

        // Schedule daily reset
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!DailyResetWorker.isWorkManagerRunning(this)) {
                DailyResetWorker.scheduleDailyReset(this)
            }
        }
    }
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}
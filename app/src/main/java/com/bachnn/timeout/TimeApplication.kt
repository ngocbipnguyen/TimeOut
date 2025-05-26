package com.bachnn.timeout

import android.app.Application
import com.bachnn.timeout.manager.PackageManager
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class TimeApplication : Application() {

    private var packageManager: PackageManager? = null
    override fun onCreate() {
        super.onCreate()
        packageManager = PackageManager(applicationContext)
    }

}
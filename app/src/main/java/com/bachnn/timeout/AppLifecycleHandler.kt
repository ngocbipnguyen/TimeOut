package com.bachnn.timeout

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log

class AppLifecycleHandler(private val lifeCycleDelegate: LifeCycleDelegate) : Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private var appInForeground = false

    override fun onConfigurationChanged(newConfig: Configuration) {
    }

    override fun onLowMemory() {}


    override fun onTrimMemory(level: Int) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            appInForeground = false
            lifeCycleDelegate.onAppBackgrounded()
        }
    }
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.e("AppLifecycleHandler","onActivityCreated")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.e("AppLifecycleHandler","onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.e("AppLifecycleHandler","onActivityResumed")
        if (!appInForeground) {
            appInForeground = true
            lifeCycleDelegate.onAppForegrounded()
        }
    }

    override fun onActivityPaused(activity: Activity) {
        Log.e("AppLifecycleHandler","onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.e("AppLifecycleHandler","onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.e("AppLifecycleHandler","onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity) {
        Log.e("AppLifecycleHandler","onActivityDestroyed")
    }

}

interface LifeCycleDelegate {
    fun onAppBackgrounded()
    fun onAppForegrounded()
}
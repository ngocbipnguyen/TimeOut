package com.bachnn.timeout.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.bachnn.timeout.data.source.local.LocalDataSource
import com.bachnn.timeout.utilities.INTERVAL_TIME_TO_LOOP
import com.bachnn.timeout.utilities.INTERVAL_TIME_TO_SAVE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppLaunchDetectorService : AccessibilityService() {

    @Inject
    lateinit var localDataSource: LocalDataSource

    private var job: Job? = null

    private var packageNameRunning: String? = null

    private var totalTime: Long = 0

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val packageName = event.packageName?.toString()
            val packageNameSelected = localDataSource.getAllAppInfo()

            if (packageNameSelected.any { it.packageName == packageName }) {
                if (packageNameRunning != null) {
                    if (packageNameRunning != packageName) {
                        if (job != null) {
                            job?.cancel()
                            updateTimestamp(totalTime.toInt())
                            totalTime = 0
                        }
                        startLoopingFunction()
                        packageNameRunning = packageName
                    }
                } else {
                    startLoopingFunction()
                    packageNameRunning = packageName
                }
            } else {
                if (job != null) {
                    job?.cancel()
                    updateTimestamp(totalTime.toInt())
                    totalTime = 0
                }
                packageNameRunning = null
            }
        }
    }

    override fun onInterrupt() {
        // Required override, but can be left empty
    }

    private fun startLoopingFunction() {
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                totalTime += INTERVAL_TIME_TO_LOOP
                if (totalTime > INTERVAL_TIME_TO_SAVE) {
                    totalTime -= INTERVAL_TIME_TO_SAVE
                    updateTimestamp(INTERVAL_TIME_TO_SAVE)
                }
                delay(INTERVAL_TIME_TO_LOOP)
            }
        }
    }

    private fun updateTimestamp(intervalTimeSave: Int) {
        if (packageNameRunning != null) {
            var timestampFromDB : Long? =  localDataSource.getTimestampByPackageName(packageNameRunning!!)
            timestampFromDB = timestampFromDB?.plus(intervalTimeSave)
            if (timestampFromDB != null) {
                localDataSource.updateTimestamp(packageNameRunning!!,timestampFromDB)
            }
        }
    }

}

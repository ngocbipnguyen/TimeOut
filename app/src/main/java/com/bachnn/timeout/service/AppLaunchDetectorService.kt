package com.bachnn.timeout.service

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.bachnn.timeout.R
import com.bachnn.timeout.data.source.local.LocalDataSource
import com.bachnn.timeout.notification.PushNotification
import com.bachnn.timeout.utilities.COUNT_TIME
import com.bachnn.timeout.utilities.END_NOTIFICATION
import com.bachnn.timeout.utilities.FIRST_NOTIFICATION
import com.bachnn.timeout.utilities.INTERVAL_TIME_TO_LOOP
import com.bachnn.timeout.utilities.INTERVAL_TIME_TO_SAVE
import com.bachnn.timeout.utilities.NOTHING_NOTIFICATION
import com.bachnn.timeout.utilities.SECOND_NOTIFICATION
import com.bachnn.timeout.utilities.SharedPreferenceUnit
import com.bachnn.timeout.utilities.THREE_HOURS
import com.bachnn.timeout.utilities.TWO_HOURS
import com.bachnn.timeout.utilities.TWO_HOURS_30_MINUTES
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
                checkOverTime()
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

    private fun checkOverTime() {
        if (packageNameRunning != null) {
            var timestamp: Long? = localDataSource.getTimestampByPackageName(packageNameRunning!!)
            if (timestamp != null) {
                if (timestamp > THREE_HOURS) {

                    if (SharedPreferenceUnit.getInt(this,COUNT_TIME, NOTHING_NOTIFICATION) == END_NOTIFICATION) {
                        // todo: handle app when out off time.
                        Log.e("checkOverTime", "END_NOTIFICATION END_NOTIFICATION ${timestamp}")
                    }

                    if (SharedPreferenceUnit.getInt(this,COUNT_TIME, NOTHING_NOTIFICATION) == SECOND_NOTIFICATION) {
                        SharedPreferenceUnit.setInt(this,COUNT_TIME, END_NOTIFICATION)
                        PushNotification.showNotification(this, getString(R.string.used_three_hours), packageNameRunning!!, true)
                    }
                } else if (timestamp > TWO_HOURS_30_MINUTES) {
                    if (SharedPreferenceUnit.getInt(this,COUNT_TIME, NOTHING_NOTIFICATION) == FIRST_NOTIFICATION) {
                        SharedPreferenceUnit.setInt(this,COUNT_TIME, SECOND_NOTIFICATION)
                        PushNotification.showNotification(this, getString(R.string.used_two_hours_30_minutes),packageNameRunning!!, false)
                    }
                } else if (timestamp > TWO_HOURS) {
                    if (SharedPreferenceUnit.getInt(this,COUNT_TIME, NOTHING_NOTIFICATION) == NOTHING_NOTIFICATION) {
                        SharedPreferenceUnit.setInt(this,COUNT_TIME, FIRST_NOTIFICATION)
                        PushNotification.showNotification(this, getString(R.string.used_two_hours),packageNameRunning!!, false)
                    }
                } else {
                    if (SharedPreferenceUnit.getInt(this,COUNT_TIME, NOTHING_NOTIFICATION) != NOTHING_NOTIFICATION) {
                        SharedPreferenceUnit.setInt(this,COUNT_TIME, NOTHING_NOTIFICATION)
                    }
                }
            }
        }
    }


}

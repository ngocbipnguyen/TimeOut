package com.bachnn.timeout.worker

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.bachnn.timeout.data.source.local.LocalDataSource
import com.bachnn.timeout.utilities.RESET_DB_TAG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.Calendar
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

@HiltWorker
class DailyResetWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val localDataSource: LocalDataSource
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            Log.d("DailyResetWorker", "Starting daily reset work")
            
            // Get all app info
            val allApps = localDataSource.getAllAppInfo()
            
            // Reset timestamps for all apps
            allApps.forEach { appInfo ->
                localDataSource.updateTimestamp(appInfo.packageName, 0)
            }
            
            Log.d("DailyResetWorker", "Daily reset completed successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("DailyResetWorker", "Error during daily reset", e)
            Result.failure()
        }
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun scheduleDailyReset(context: Context) {
            val workManager = androidx.work.WorkManager.getInstance(context)
            
            // Calculate initial delay until next midnight (12 AM)
            val calendar = Calendar.getInstance()
            val now = System.currentTimeMillis()
            
            // Set target time to midnight (12 AM)
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            
            // If it's already past midnight, schedule for next day
            if (calendar.timeInMillis <= now) {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            }
            
            val initialDelay = calendar.timeInMillis - now
            
            // Create a periodic work request that runs daily at midnight
            val dailyResetRequest = androidx.work.PeriodicWorkRequestBuilder<DailyResetWorker>(
                java.time.Duration.ofDays(1)
            )
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .addTag(RESET_DB_TAG)
            .build()

            // Enqueue the work request
            workManager.enqueue(
                dailyResetRequest
            )
            
            Log.d("DailyResetWorker", "Scheduled next reset at: ${calendar.time}")
        }


        fun cancelResetTokenSchedule(context: Context?) {
            WorkManager.getInstance(context!!)
                .cancelAllWorkByTag(RESET_DB_TAG)
        }

        fun isWorkManagerRunning(context: Context?): Boolean {
            val instance = WorkManager.getInstance(context!!)
            val statuses = instance.getWorkInfosByTag(RESET_DB_TAG)
            return try {
                var running = false
                val workInfoList = statuses.get()

                for (workInfo in workInfoList) {
                    val state = workInfo.state
                    running =
                        (state == WorkInfo.State.RUNNING) or (state == WorkInfo.State.ENQUEUED)
                }
                running
            } catch (e: ExecutionException) {
                e.printStackTrace()
                false
            } catch (e: InterruptedException) {
                e.printStackTrace()
                false
            }
        }

    }
} 
package com.bachnn.timeout.utilities

import android.content.Context

const val CHANNEL_HOME_ID = "TimeOut"

const val DATABASE_NAME = "TimeOut_DB"

const val RESET_DB_TAG = "reset_db"

const val INTERVAL_TIME_TO_SAVE = 60 * 5 * 1000 // 5 minutes

const val INTERVAL_TIME_TO_LOOP : Long = 5 * 1000 // 5 seconds

const val CHANNEL_ID = "alert_notification"

const val ALERT_NOTIFICATION = 112

// SharedPreference save time.
const val COUNT_TIME = "count_time"

const val  NOTHING_NOTIFICATION = 0

const val  FIRST_NOTIFICATION = 1

const val  SECOND_NOTIFICATION = 2

const val  END_NOTIFICATION = 3

// TIME_PERIOD
const val TWO_HOURS : Long = 1000 * 60 * 60 * 2

const val TWO_HOURS_30_MINUTES : Long =  1000 * 60 * 60 * 2 + 1000 * 60 * 30

const val THREE_HOURS : Long =  1000 * 60 * 60 * 3

fun isDatabaseExists(context: Context, dbName: String): Boolean {
    val dbFile = context.getDatabasePath(dbName)
    return dbFile.exists()
}

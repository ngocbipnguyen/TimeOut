package com.bachnn.timeout.utilities

import android.content.Context

const val CHANNEL_HOME_ID = "TimeOut"

const val DATABASE_NAME = "TimeOut_DB"

const val INTERVAL_TIME_TO_SAVE = 60 * 5 * 1000 // 5 minutes

const val INTERVAL_TIME_TO_LOOP : Long = 5 * 1000 // 5 seconds

fun isDatabaseExists(context: Context, dbName: String): Boolean {
    val dbFile = context.getDatabasePath(dbName)
    return dbFile.exists()
}

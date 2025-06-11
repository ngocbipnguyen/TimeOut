package com.bachnn.timeout.utilities

import android.content.Context

const val CHANNEL_HOME_ID = "TimeOut"

const val DATABASE_NAME = "TimeOut_DB"

fun isDatabaseExists(context: Context, dbName: String): Boolean {
    val dbFile = context.getDatabasePath(dbName)
    return dbFile.exists()
}

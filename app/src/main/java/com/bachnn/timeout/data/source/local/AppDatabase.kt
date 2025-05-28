package com.bachnn.timeout.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bachnn.timeout.data.model.AppInfo
import com.bachnn.timeout.data.source.local.dao.AppInfoDao

@Database(entities = [AppInfo::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAppInfoDao(): AppInfoDao
}
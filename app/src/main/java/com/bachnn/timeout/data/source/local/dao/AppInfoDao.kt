package com.bachnn.timeout.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bachnn.timeout.data.model.AppInfo

@Dao
interface AppInfoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAppInfo(appInfo: AppInfo)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllAppInfo(appInfo: List<AppInfo>)

    @Query("SELECT *FROM AppInfo ORDER BY timestamp DESC")
    fun getAllAppInfo() : List<AppInfo>

    @Query("UPDATE AppInfo SET timestamp = :newTimestamp WHERE packageName = :pkg")
    fun updateTimestamp(pkg: String, newTimestamp: Long)

    @Query("SELECT COUNT(packageName) FROM AppInfo")
    fun getCount(): Int

}
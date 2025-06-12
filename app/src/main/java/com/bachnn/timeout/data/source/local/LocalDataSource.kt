package com.bachnn.timeout.data.source.local

import com.bachnn.timeout.data.model.AppInfo
import com.bachnn.timeout.data.source.AppDataSource
import com.bachnn.timeout.data.source.local.dao.AppInfoDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val appInfoDao: AppInfoDao) :
    AppDataSource.Local {
    override fun insertAppInfo(appInfo: AppInfo) {
        appInfoDao.insertAppInfo(appInfo)
    }

    override fun insertAllAppInfo(appInfo: List<AppInfo>) {
        appInfoDao.insertAllAppInfo(appInfo)
    }

    override fun getAllAppInfo(): List<AppInfo> {
        return appInfoDao.getAllAppInfo()
    }

    override fun updateTimestamp(pkg: String, newTimestamp: Long) {
        appInfoDao.updateTimestamp(pkg, newTimestamp)
    }

    override fun getTimestampByPackageName(packageName: String): Long? {
        return appInfoDao.getTimestampByPackageName(packageName)
    }

    override fun getCount(): Int {
        return appInfoDao.getCount()
    }

    override fun deleteAppInfoByPackageName(packageName: String) {
        appInfoDao.deleteAppInfoByPackageName(packageName)
    }
}
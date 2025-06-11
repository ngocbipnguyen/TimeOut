package com.bachnn.timeout.data.source

import com.bachnn.timeout.data.model.AppInfo

interface AppDataSource {

    interface Local {

        fun insertAppInfo(appInfo: AppInfo)

        fun insertAllAppInfo(appInfo: List<AppInfo>)

        fun getAllAppInfo() : List<AppInfo>

        fun updateTimestamp(pkg: String, newTimestamp: Long)

        fun getCount(): Int

        fun deleteAppInfoByPackageName(packageName: String)

    }

}
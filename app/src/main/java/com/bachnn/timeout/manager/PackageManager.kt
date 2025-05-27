package com.bachnn.timeout.manager

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PackageManager @Inject constructor(@ApplicationContext private val context: Context) {

    private var appsInstall: List<PackageInfo>

    init {
        appsInstall = context.packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

        appsInstall.forEach { it ->
            Log.e("PackageManager", it.packageName)
            Log.e("PackageManager",
                context.packageManager.getApplicationIcon(it.packageName).toString()
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                Log.d(
                    "AppInfo", """
            Package Name: ${it.packageName}
            Version Name: ${it.versionName}
            Version Code: ${it.longVersionCode}
            First Install Time: ${it.firstInstallTime}
            Last Update Time: ${it.lastUpdateTime}
            Application Label: ${context.packageManager.getApplicationLabel(it.applicationInfo!!)}
            Source Dir: ${it.applicationInfo?.sourceDir}
            Target SDK: ${it.applicationInfo?.targetSdkVersion}
        """.trimIndent()
                )
            }
        }

    }


    fun getPackageInfo(): List<PackageInfo> {
        return appsInstall
    }
}
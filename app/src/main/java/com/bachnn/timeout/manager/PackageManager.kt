package com.bachnn.timeout.manager

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PackageManager @Inject constructor(@ApplicationContext private val context: Context) {

    private var appsInstall: List<PackageInfo>

    init {
        appsInstall = context.packageManager.getInstalledPackages(PackageManager.GET_META_DATA)

        appsInstall.forEach { it ->
            Log.e("PackageManager", it.packageName)
        }

    }
}
package com.bachnn.timeout.ui.viewModel

import android.content.Context
import android.content.pm.PackageInfo
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bachnn.timeout.TimeApplication
import com.bachnn.timeout.base.BaseViewModel
import com.bachnn.timeout.data.model.AppInfo
import com.bachnn.timeout.data.source.local.LocalDataSource
import com.bachnn.timeout.manager.PackageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localDataSource: LocalDataSource
) :
    BaseViewModel() {

    private var packageManager: PackageManager? = null

    private var _packageInfo = MutableLiveData<List<AppInfo>>()
    val packageInfo: LiveData<List<AppInfo>> = _packageInfo

    init {
        try {
            val timeApplication: TimeApplication = context.applicationContext as TimeApplication
            timeApplication.packageManager
            if (timeApplication.packageManager != null) {
                packageManager = timeApplication.packageManager
                packageManager?.let {
                    val listPackage = ArrayList<AppInfo>()
                    it.getPackageInfo().forEach { packageInfo ->
                        val label: String? =
                            context.packageManager.getApplicationLabel(packageInfo.applicationInfo!!)
                                .toString()
                        label?.let {
                            if (!(label.contains("/") || label.contains("."))) {
                                listPackage.add(
                                    AppInfo(
                                        packageInfo.packageName,
                                        label,
                                        packageInfo.applicationInfo!!.targetSdkVersion
                                    )
                                )
                            }
                        }
                    }
                    _packageInfo.postValue(listPackage)
                    localDataSource.insertAllAppInfo(listPackage)
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Exception: ${e.toString()}")
        }
    }

    fun getAppInfo(): List<AppInfo> {
        return if (_packageInfo.value == null) {
            return ArrayList<AppInfo>()
        } else {
            _packageInfo.value!!
        }
    }

}
package com.bachnn.timeout.ui.viewModel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bachnn.timeout.TimeApplication
import com.bachnn.timeout.base.BaseViewModel
import com.bachnn.timeout.data.model.AppInfo
import com.bachnn.timeout.data.source.local.LocalDataSource
import com.bachnn.timeout.manager.PackageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localDataSource: LocalDataSource
) :
    BaseViewModel() {

    private var packageManager: PackageManager? = null
    private val _packageInfo = MutableLiveData<List<AppInfo>>()
    val packageInfo: LiveData<List<AppInfo>> = _packageInfo

    private var packageInfoSelected : ArrayList<AppInfo>? = null

    private var job: Job? = null

    init {
        try {
            val timeApplication: TimeApplication = context.applicationContext as TimeApplication
            packageManager = timeApplication.packageManager

            packageInfoSelected = ArrayList()
            packageInfoSelected?.addAll(localDataSource.getAllAppInfo())
            Log.e("packageInfoSelected", "packageInfoSelected ${packageInfoSelected!!.size}")
//            loadSelectedPackages()
            
            viewModelScope.launch {
                packageManager?.let { manager ->
                    try {
                        val listPackage = ArrayList<AppInfo>()
                        var listSortByTime: List<AppInfo> ? = null
                        manager.getPackageInfo().forEach { packageInfo ->
                            try {
                                val label: String? = context.packageManager
                                    .getApplicationLabel(packageInfo.applicationInfo!!)
                                    .toString()

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    Log.e("packageInfoSelected", "packageInfo : ${packageInfo.versionName}")
                                }
                                label?.let {
                                    if (!(label.contains("/") || label.contains("."))) {
                                        listPackage.add(
                                            AppInfo(
                                                packageInfo.packageName,
                                                label,
                                                packageInfo.applicationInfo!!.targetSdkVersion,
                                                packageInfo.versionName.toString()
                                            )
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                Log.e("HomeViewModel", "Error processing package: ${packageInfo.packageName}", e)
                            }
                        }
                        if (packageInfoSelected != null) {
                            packageInfoSelected?.forEach {selected ->
                                listPackage.forEach {
                                    if (it.packageName == selected.packageName) {
                                        it.active = true
                                        it.timestamp = selected.timestamp
                                    }
                                }
                            }
                            val listSortByTrue = listPackage.sortedByDescending { it.active }
                            listSortByTime = listSortByTrue.sortedByDescending { it.timestamp }
                        }
                        if (listSortByTime != null) {
                            _packageInfo.postValue(listSortByTime!!)
                        } else {
                            _packageInfo.postValue(listPackage)
                        }
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Error processing packages", e)
                        _packageInfo.postValue(emptyList())
                    }
                } ?: run {
                    Log.e("HomeViewModel", "PackageManager is null")
                    _packageInfo.postValue(emptyList())
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Initialization error", e)
            _packageInfo.postValue(emptyList())
        }
    }

    fun updatePackageSelected(packageName: AppInfo, checked: Boolean) {
        if (checked) {
            packageName.active = checked
            packageInfoSelected?.add(packageName)
            localDataSource.insertAppInfo(packageName)
            Log.e("HomeViewModel", "updatePackageSelected AppInfo $packageName")
        } else {
            Log.e("HomeViewModel", "---updatePackageSelected AppInfo $packageName")
            packageInfoSelected?.forEachIndexed { index, appInfo ->
                if (appInfo.packageName == packageName.packageName) {
                    packageInfoSelected?.removeAt(index)
                    localDataSource.deleteAppInfoByPackageName(packageName.packageName)
                }
            }
        }
    }

    fun startLoopingFunction() {
        job?.cancel() // Cancel any existing job
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(3000) // Update every second for more responsive UI
                refreshAppInfoList()
            }
        }
    }

    private fun refreshAppInfoList() {
        try {
            viewModelScope.launch {
                try {
                    // Get fresh data from database
                    val freshData = localDataSource.getAllAppInfo()
                    packageInfoSelected?.clear()
                    packageInfoSelected?.addAll(freshData)
                    Log.e("packageInfoSelected", "refreshAppInfoList ${packageInfoSelected!!.size}")


                    // Update current list with new timestamps
                    val currentList = _packageInfo.value

                    packageInfoSelected?.forEach { selected ->
                        currentList?.forEach {
                            if (it.packageName == selected.packageName) {
                                Log.e("packageInfoSelected", "${it.packageName} == ${selected.packageName}")
                                it.active = true
                                if (it.timestamp != selected.timestamp) {
                                    it.timestamp = selected.timestamp
                                }
                            }
                        }
                    }

                    // Only sort and update if there were changes
                    val sortedList = currentList
                        ?.sortedByDescending { it.active }
                        ?.sortedByDescending { it.timestamp }
                    _packageInfo.postValue(sortedList!!)

                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error refreshing app info list", e)
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error in refreshAppInfoList", e)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
package com.bachnn.timeout.ui.viewModel

import android.content.Context
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

    init {
        try {
            val timeApplication: TimeApplication = context.applicationContext as TimeApplication
            packageManager = timeApplication.packageManager

            packageInfoSelected = ArrayList()
            packageInfoSelected?.addAll(localDataSource.getAllAppInfo())
//            loadSelectedPackages()
            
            viewModelScope.launch {
                packageManager?.let { manager ->
                    try {
                        val listPackage = ArrayList<AppInfo>()
                        manager.getPackageInfo().forEach { packageInfo ->
                            try {
                                val label: String? = context.packageManager
                                    .getApplicationLabel(packageInfo.applicationInfo!!)
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
                            } catch (e: Exception) {
                                Log.e("HomeViewModel", "Error processing package: ${packageInfo.packageName}", e)
                            }
                        }
                        if (packageInfoSelected != null) {
                            listPackage.forEach { appInfo ->
                                packageInfoSelected?.find { selected -> 
                                    selected.packageName == appInfo.packageName 
                                }?.let { selected ->
                                    appInfo.active = true
                                    appInfo.timestamp = selected.timestamp
                                }
                            }
                        }
                        _packageInfo.postValue(listPackage)
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

    private fun loadSelectedPackages() {
        if (packageInfoSelected == null) {
            if (localDataSource.getAllAppInfo().isNotEmpty()) {
                packageInfoSelected = ArrayList()
                packageInfoSelected?.addAll(localDataSource.getAllAppInfo())
            }
        } else {
            if (packageInfoSelected?.size != localDataSource.getAllAppInfo().size) {
                updateSelectedPackages()
            }
        }
    }

    private fun updateSelectedPackages() {
        val currentPackages = localDataSource.getAllAppInfo()
        packageInfoSelected?.clear()
        packageInfoSelected?.addAll(currentPackages)
    }

    private fun isPackageSelected(packageName: String): Boolean {
        return packageInfoSelected?.any { it.packageName == packageName } ?: false
    }

    fun getSelectedPackages(): List<AppInfo> {
        return packageInfoSelected?.toList() ?: emptyList()
    }

    fun getAppInfo(): List<AppInfo> {
        return _packageInfo.value ?: emptyList()
    }

    fun updatePackageSelected(packageName: AppInfo, checked: Boolean) {
        if (checked) {
            packageName.active = checked
            packageInfoSelected?.add(packageName)
            localDataSource.insertAppInfo(packageName)
            Log.e("HomeViewModel", "AppInfo $packageName")
        } else {
            packageInfoSelected?.let { selectedList ->
                val iterator = selectedList.iterator()
                while (iterator.hasNext()) {
                    val selected = iterator.next()
                    if (selected.packageName == packageName.packageName) {
                        iterator.remove()
                        localDataSource.deleteAppInfoByPackageName(packageName.packageName)
                        break
                    }
                }
            }
        }
    }
}
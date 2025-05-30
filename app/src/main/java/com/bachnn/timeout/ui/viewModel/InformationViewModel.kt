package com.bachnn.timeout.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bachnn.timeout.base.BaseViewModel
import com.bachnn.timeout.data.model.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor() : BaseViewModel() {

    private val _appInfo = MutableLiveData<AppInfo>()
    val appInfo: LiveData<AppInfo> = _appInfo

    fun setAppInfo(appInfo: AppInfo?) {
        if (appInfo == null) {
            Log.e("InformationViewModel", "Attempted to set null appInfo")
            return
        }
        _appInfo.postValue(appInfo!!)
    }

    fun openClick() {
        Log.e("InformationViewModel", "openClick")
    }

    fun forceStopClick() {
        Log.e("InformationViewModel", "forceStopClick")
    }

    fun uninstallClick() {
        Log.e("InformationViewModel", "uninstallClick")
    }
}
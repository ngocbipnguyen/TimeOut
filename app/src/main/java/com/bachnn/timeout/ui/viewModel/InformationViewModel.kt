package com.bachnn.timeout.ui.viewModel

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bachnn.timeout.R
import com.bachnn.timeout.base.BaseViewModel
import com.bachnn.timeout.broadcast.RuntimeReceiver
import com.bachnn.timeout.data.model.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class InformationViewModel @Inject constructor(@ApplicationContext private val context: Context) : BaseViewModel() {

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
        val intent = Intent(RuntimeReceiver.OPEN_APP)
        intent.setClass(context, RuntimeReceiver::class.java)
        intent.putExtra(RuntimeReceiver.PACKAGE_NAME, _appInfo.value?.packageName)
        context.sendBroadcast(intent)

    }

    fun forceStopClick() {
        Log.e("InformationViewModel", "forceStopClick")
        val intent = Intent(RuntimeReceiver.KILL_AND_DISABLE)
        intent.putExtra(RuntimeReceiver.PACKAGE_NAME, _appInfo.value?.packageName)
        context.sendBroadcast(intent)
    }

    fun uninstallClick() {
        Log.e("InformationViewModel", "uninstallClick")
        val intent = Intent(RuntimeReceiver.UNINSTALL_APP)
        intent.putExtra(RuntimeReceiver.PACKAGE_NAME, _appInfo.value?.packageName)
        context.sendBroadcast(intent)
    }

    fun notificationClick() {
        Log.e("InformationViewModel", "uninstallClick")
        val intents = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context.packageName, null)
        intents.data = uri
        intents.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intents)
    }

    fun getVersion(): String {
        return "${context.getString(R.string.version)}: ${_appInfo.value?.versionName}"
    }


    fun isAppEnabled(): Boolean {
        if(_appInfo.value?.packageName.isNullOrEmpty()) return false
        return try {
            val pm = context.packageManager
            val appInfo = pm.getApplicationInfo(_appInfo.value?.packageName!!, 0)
            appInfo.enabled
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    fun notificationsEnabled(): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

}
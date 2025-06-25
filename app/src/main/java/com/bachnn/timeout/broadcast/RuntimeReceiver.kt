package com.bachnn.timeout.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class RuntimeReceiver : BroadcastReceiver() {

    companion object {
        const val KILL_AND_DISABLE = "kill_and_disable"
        const val DISABLE_APP = "disable_app"
        const val ENABLE_APP = "enable_app"
        const val PACKAGE_NAME = "package_name"
        const val OPEN_APP = "open_app"
        const val UNINSTALL_APP = "uninstall_app"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            when (it.action) {
                KILL_AND_DISABLE -> {
                    val packageName: String? = intent.getStringExtra(PACKAGE_NAME)
                    if (packageName != null) {
                        forceStopApp(packageName)
                        disableApp(packageName)
                    }
                }
                DISABLE_APP -> {
                    // todo still using app, when user quit app then disable app.
                }
                ENABLE_APP -> {
                    val packageName: String? = intent.getStringExtra(PACKAGE_NAME)
                    if (packageName != null) {
                        enableApp(packageName)
                    }
                }
                OPEN_APP -> {
                    val packageName: String? = intent.getStringExtra(PACKAGE_NAME)
                    if (packageName != null) {
                        openAppByCommand(packageName)
                    }
                }
                UNINSTALL_APP -> {
                    val packageName: String? = intent.getStringExtra(PACKAGE_NAME)
                    if (packageName != null) {
                        uninstallApp(packageName)
                    }
                }
                else -> {

                }
            }
        }
    }

    private fun disableApp(packageName: String) {
        Runtime.getRuntime().exec("su -c 'pm disable-user --user 0 $packageName'")
    }

    private fun enableApp(packageName: String) {
        Runtime.getRuntime().exec("su -c 'pm enable $packageName'")

    }

    private fun forceStopApp(packageName: String) {
        val command = "su -c 'am force-stop $packageName'"
        Runtime.getRuntime().exec(command)
    }

    private fun openApp(context: Context, packageName: String) {
        val pm = context.packageManager
        val launchIntent = pm.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            context.startActivity(launchIntent)
        } else {
            Toast.makeText(context, "App not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openAppByCommand(packageName: String) {
        runShellCommand("monkey -p $packageName -c android.intent.category.LAUNCHER 1")
    }


    //  "monkey -p $packageName -c android.intent.category.LAUNCHER 1"
    private fun runShellCommand(command: String): String {
        return try {
            val process = Runtime.getRuntime().exec(arrayOf("su", "-c", command))
            process.inputStream.bufferedReader().readText().trim()
        } catch (e: Exception) {
            e.printStackTrace()
            "Error running command"
        }
    }

    private fun uninstallApp(packageName: String) {
        try {
            Runtime.getRuntime().exec(arrayOf("su", "-c", "pm uninstall $packageName"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
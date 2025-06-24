package com.bachnn.timeout.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.bachnn.timeout.R
import com.bachnn.timeout.broadcast.RuntimeReceiver
import com.bachnn.timeout.utilities.ALERT_NOTIFICATION
import com.bachnn.timeout.utilities.CHANNEL_ID

class PushNotification {

    companion object {
        fun createNotificationChannel(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.getString(R.string.channel_name)
                val descriptionText = context.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID,name, importance).apply {
                    description = descriptionText
                }

                val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
        }


        fun showNotification(context: Context, message: String, packageName: String, isShowButton: Boolean) {

//            val bundle = Bundle()
//            bundle.putSerializable("user_notification",user)
//
//            val pendingIntent = NavDeepLinkBuilder(context)
//                .setComponentName(MainActivity::class.java)
//                .setGraph(R.navigation.main_nav_graph)
//                .setDestination(R.id.messageFragment)
//                .setArguments(bundle)
//                .createPendingIntent()
//

            val pendingIntent = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.main_nav_graph)
                .addDestination(R.id.informationFragment)
//                .setArguments(Bundle().apply {
//                    putSerializable("userArg", user)
//                })
                .createPendingIntent()
            var builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)


            if (isShowButton) {

                val killAndDisable = Intent(context, RuntimeReceiver::class.java).apply {
                    action = RuntimeReceiver.KILL_AND_DISABLE
                    putExtra(RuntimeReceiver.PACKAGE_NAME, packageName)
                }

                val killAndDisablePendingIntent: PendingIntent =
                    PendingIntent.getBroadcast(
                        context,
                        0,
                        killAndDisable,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                val clearNotification = Intent(context, RuntimeReceiver::class.java).apply {
                    action = RuntimeReceiver.DISABLE_APP
                    putExtra(RuntimeReceiver.PACKAGE_NAME, packageName)
                }

                val clearNotificationPendingIntent: PendingIntent =
                    PendingIntent.getBroadcast(
                        context,
                        0,
                        clearNotification,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                builder.addAction(
                    R.drawable.outline_disabled_visible_24,
                    context.getString(R.string.kill_and_disable),
                    killAndDisablePendingIntent
                )
                builder.addAction(
                    R.drawable.baseline_close_24,
                    context.getString(R.string.disable_app),
                    clearNotificationPendingIntent
                )
            }

            with(NotificationManagerCompat.from(context)) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    // ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    // public fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                    //                                        grantResults: IntArray)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.

                    return@with
                }
                // notificationId is a unique int for each notification that you must define.
                notify(ALERT_NOTIFICATION, builder.build())
            }
        }

    }

}
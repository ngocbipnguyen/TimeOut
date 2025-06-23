package com.bachnn.timeout.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bachnn.timeout.R
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


        fun showNotification(context: Context, message: String) {

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

//            val pendingIntent = NavDeepLinkBuilder(context)
//                .setGraph(R.navigation.main_nav_graph)
//                .addDestination(R.id.messengerFragment)
//                .setArguments(Bundle().apply {
//                    putSerializable("userArg", user)
//                })
//                .createPendingIntent()

            var builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
//                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true)

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
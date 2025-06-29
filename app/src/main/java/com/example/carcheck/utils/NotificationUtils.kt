package com.example.carcheck.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.carcheck.R
import com.example.carcheck.data.model.NodeParameters

object NotificationUtils {
    const val CHANNEL_ID = "critical_parameters_channel"
    const val CHANNEL_NAME = "Critical Car Parameters"

    fun createNotificationChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Уведомления о критических параметрах автомобиля"
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun showCriticalParameterNotification(context: Context, parameter: NodeParameters) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val granted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) return // не показываем уведомление без разрешения
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.archiv_icon)
            .setContentTitle("❗ Критический параметр")
            .setContentText("${parameter.name} = ${parameter.value}")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(parameter.id.hashCode(), builder.build())
    }

}

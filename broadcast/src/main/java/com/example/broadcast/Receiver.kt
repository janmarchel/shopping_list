package com.example.broadcast

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.concurrent.atomic.AtomicInteger

class Receiver : BroadcastReceiver() {
    private val c: AtomicInteger = AtomicInteger(10)

    fun createChannel(context: Context) {
        val notiChannel = NotificationChannel(
            "channel1",
            "Shopping List Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        NotificationManagerCompat.from(context).createNotificationChannel(notiChannel)
    }

    override fun onReceive(context: Context, intent: Intent) {
        Toast.makeText(context,"Recevied !!", Toast.LENGTH_SHORT).show()
        createChannel(context)
        val productName = intent.getStringExtra("productName")
//
        val intent1 = Intent()

        intent1.component = ComponentName(
            "com.example.shopping_list",
            "com.example.shopping_list.MainActivity"
        )

        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_IMMUTABLE)


        val notification = NotificationCompat.Builder(context, "channel1")
            .setSmallIcon(android.R.drawable.alert_dark_frame)
            .setContentTitle("Product Edited")
            .setContentText(productName)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(getID(),notification)
    }

    fun getID(): Int {
        return c.incrementAndGet()
    }

}


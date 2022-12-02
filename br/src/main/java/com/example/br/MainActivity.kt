package com.example.br

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.util.concurrent.atomic.AtomicInteger


class MainActivity : AppCompatActivity() {

    private val c: AtomicInteger = AtomicInteger(3)
    private val broad = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                "com.example.shopping_list.ACTION_SEND" -> {
                    val product_name =
                        intent.getStringExtra("com.example.shopping_list.PRODUCT_NAME")

//
                    val pendingIntent: PendingIntent =
                        PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

                    val mBuilder = NotificationCompat.Builder(context!!)
                        .setSmallIcon(android.R.drawable.alert_dark_frame)
                        .setContentTitle("Product Edited")
                        .setContentText(product_name)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)

                    val notificationManager: NotificationManager =
                        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    notificationManager.notify(getID(), mBuilder.build())

                }
            }
        }
    }

    fun getID(): Int {
        return c.incrementAndGet()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//       setContentView(R.layout.activity_main)
        val intentFilter = IntentFilter("com.example.shopping_list.ACTION_SEND")
        registerReceiver(broad, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broad)
    }
}
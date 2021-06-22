@file:Suppress("DEPRECATION")

package com.example.luf


import android.R
import android.app.Notification
import android.app.NotificationManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import com.example.luf.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        sendNotification(remoteMessage.notification!!.body)
    }

    private fun sendNotification(messageBody: String?) {
        val intent = Intent(this, MenuAct::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder: Notification.Builder? = Notification.Builder(this)
            .setSmallIcon(R.drawable.ic_delete)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.drawable.ic_delete))
            .setContentTitle(this.getString(R.string.ok))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder!!.build())
    }
}
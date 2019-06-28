package android.base

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.log.logActivity
import android.widget.Toast
import androidx.core.app.NotificationCompat

open class CApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        showDebugDBAddressLogToast(applicationContext)
        logActivity()
    }

    fun showDebugDBAddressLogToast(context: Context) {
        try {
            val debugDB = Class.forName("com.amitshekhar.DebugDB")
            val getAddressLog = debugDB.getMethod("getAddressLog")
            val value = getAddressLog.invoke(null) as String

            val pm = context.packageManager
            val applicationInfo = pm.getApplicationInfo(context.packageName, 0)
            val name = pm.getApplicationLabel(applicationInfo).toString()

            val notificationBuilder = NotificationCompat.Builder(context, "COMMON")
                    .setSmallIcon(android.R.drawable.ic_notification_overlay)
                    .setAutoCancel(false)
                    .setContentTitle(name)
                    .setContentText(value)
            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.notify(0 /* ID of notification */, notificationBuilder.build())
            Toast.makeText(context, value, Toast.LENGTH_LONG).show()
        } catch (ignore: Exception) {
        }
    }
}
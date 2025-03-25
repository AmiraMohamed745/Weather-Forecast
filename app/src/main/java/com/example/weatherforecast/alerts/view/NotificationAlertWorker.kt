/*
package com.example.weatherforecast.alerts.view

import android.app.Notification.DEFAULT_ALL
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.job.JobInfo.PRIORITY_MAX
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Color.RED
import android.media.AudioAttributes
import android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION
import android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE
import android.media.RingtoneManager.TYPE_NOTIFICATION
import android.media.RingtoneManager.getDefaultUri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.util.Log
import androidx.compose.ui.graphics.Canvas
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.weatherforecast.AlertDetailsScreen
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.database.LocalDataSourceImpl
import com.example.weatherforecast.home.viewmodel.CurrentWeatherViewModel
import com.example.weatherforecast.model.RepositoryImpl
import com.example.weatherforecast.navigation.NavigationRouter
import com.example.weatherforecast.network.RemoteDataSourceImpl
import com.example.weatherforecast.network.RetrofitHelper
import java.nio.charset.CodingErrorAction.REPLACE
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit.MILLISECONDS

*/
/*
To Do:
- Check for run time permissions for notifications
 *//*


class NotificationAlertWorker(
    context: Context,
    workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    private val repository = RepositoryImpl.getInstance(
        RemoteDataSourceImpl(RetrofitHelper.weatherService),
        LocalDataSourceImpl()
    )

    override suspend fun doWork(): Result {

        Log.d("NotificationAlertWorker", "Worker started!") // this log doesn't get printed

        val id = inputData.getLong(NOTIFICATION_ID, 0).toInt()
        val title = inputData.getString("ALERT_TITLE") ?: "No title"
        val lat = inputData.getString("LOCATION_LATITUDE")
        val lon = inputData.getString("LOCATION_LONGITUDE")

        return try {
            val weatherData = repository.fetchCurrentWeatherData(lat.toString(), lon.toString())

            val weatherDescription = weatherData.weather.firstOrNull()?.description
            val location = weatherData.sys?.country
            val city = weatherData.name

            val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val currentDateTime = dateFormat.format(Date((weatherData.dt?.toLong() ?: 0L) * 1000))
            val sunriseTime = timeFormat.format(Date((weatherData.sys?.sunrise?.toLong() ?: 0L)))
            val sunsetTime = timeFormat.format(Date((weatherData.sys?.sunset?.toLong() ?: 0L) * 1000))

            val feelsLike = weatherData.main?.feelsLike.toString()
            val temperature = weatherData.main?.temp.toString()
            // Still going to add more data such as pressure, humidity, etc.

            val intent = Intent(applicationContext,AlertDetailsScreen::class.java).apply {
                flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
                putExtra(NOTIFICATION_ID, id)
                putExtra("ALERT_TITLE", title)
                putExtra("ALERT_LOCATION", location)
                putExtra("ALERT_CITY", city )
                putExtra("ALERT_DESCRIPTION", weatherDescription)
                putExtra("ALERT_DATE", currentDateTime)
                putExtra("ALERT_SUNRISE", sunriseTime)
                putExtra("ALERT_SUNSET", sunsetTime)
                putExtra("ALERT_TEMPERATURE", temperature)
                putExtra("ALERT_FEELS_LIKE", feelsLike)
            }

            val contextText = "The Weather in $city, $location is $weatherDescription"

            sendNotification(id, title, contextText, intent)
            return Result.success()
        } catch (ex: Exception) {
            Result.failure()
        }
    }

    private fun sendNotification(id: Int, title: String, contextText: String, intent: Intent) {

        val pendingIntent = getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE) // None of the following candidates is applicable:

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_notification_active)
            .setContentTitle(title)
            .setContentText(contextText)
            .setDefaults(DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notification.priority = PRIORITY_MAX

        if (SDK_INT >= O) {
            notification.setChannelId(NOTIFICATION_CHANNEL)

            val ringtoneManager = getDefaultUri(TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder().setUsage(USAGE_NOTIFICATION_RINGTONE)
                .setContentType(CONTENT_TYPE_SONIFICATION).build()

            val channel =
                NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, IMPORTANCE_HIGH)

            channel.enableLights(true)
            channel.lightColor = RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(id, notification.build())

    }

    */
/*private fun scheduleNotification(delay: Long, data: Data) {
        val notificationWork = OneTimeWorkRequest
            .Builder(NotificationAlertWorker::class.java)
            .setInitialDelay(delay, MILLISECONDS)
            .setInputData(data)
            .build()

        val instanceWorkManager = WorkManager.getInstance(applicationContext)

        instanceWorkManager
            .beginUniqueWork(
                NOTIFICATION_WORK,
                ExistingWorkPolicy.REPLACE,
                notificationWork
            )
            .enqueue()
    }*//*


    // Will probably need to define meaningful names for these and have them in the constants file
    companion object {
        const val NOTIFICATION_ID = "appName_notification_id"
        const val NOTIFICATION_NAME = "appName"
        const val NOTIFICATION_CHANNEL = "appName_channel_01"
        const val NOTIFICATION_WORK = "appName_notification_work"
    }

}


*/

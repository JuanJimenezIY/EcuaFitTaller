package com.jimenez.ecuafit.ui.activities

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.jimenez.ecuafit.R
import com.jimenez.ecuafit.databinding.ActivityAguaBinding
import com.jimenez.ecuafit.ui.utilities.BroadcasterNotifications
import com.jimenez.ecuafit.ui.utilities.ServiceReminder
import kotlin.properties.Delegates

class AguaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAguaBinding
    private var agua = 0
    private var vaso = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAguaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPref = getSharedPreferences("agua", Context.MODE_PRIVATE)
        vaso = sharedPref.getInt("agua", agua)
        Log.d("UCE", vaso.toString())
        binding.totalAgua.text = (vaso * 250).toString() + " ml"
        binding.totalVasos.text = "$vaso"
        binding.botonMas.setOnClickListener {
            agregarAgua()
        }
        binding.botonMenos.setOnClickListener {
            restarAgua()
        }
        createNotificationChannel()
        if (vaso < 8) {

            scheduleNotification(100)
        }
        else{
            cancelScheduledNotification()
        }
        scheduleMidnightJob()

        //  sendNotification()
    }

    fun agregarAgua() {
        vaso += 1
        actualizarAgua()
    }

    fun restarAgua() {
        if (vaso >= 1) {
            vaso -= 1
        }
        actualizarAgua()
    }

    private fun scheduleNotification(intervalMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(this, BroadcasterNotifications::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Calcular el tiempo en el futuro para la próxima notificación
        val currentTime = System.currentTimeMillis()
        val triggerTime = currentTime + intervalMillis

        // Programar la alarma con repetición
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            intervalMillis,
            pendingIntent
        )
    }

    private fun actualizarAgua() {

        binding.totalAgua.text = (vaso * 250).toString() + " ml"

        binding.totalVasos.text = "$vaso"
        val sharedPref = getSharedPreferences("agua", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("agua", vaso)
                .apply()
        }
    }

    val CHANNEL: String = "Notificaciones"
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Ecuafit"
            val descriptionText = "Notificaciones recordatorios"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun scheduleMidnightJob() {
        val componentName = ComponentName(this, ServiceReminder::class.java)
        val jobInfo = JobInfo.Builder(1, componentName)
            .setRequiresDeviceIdle(false)
            .setRequiresCharging(false)
            .setPeriodic(AlarmManager.INTERVAL_DAY) // Repetir cada día
            .build()

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(jobInfo)
    }

    private fun cancelScheduledNotification() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(this, BroadcasterNotifications::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.cancel(pendingIntent)
    }


}
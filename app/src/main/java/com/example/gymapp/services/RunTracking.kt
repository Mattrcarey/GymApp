package com.example.gymapp.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.gymapp.MainActivity
import com.example.gymapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*

typealias Polyline = MutableList<LatLng>
typealias Polylines = MutableList<Polyline>

class RunTracking: LifecycleService()
{
    var isFirstRun = true

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var notificationBuilder: NotificationCompat.Builder

    private val timeRunInSeconds = MutableLiveData<Long>()
    private var latitude: Double = 0.0
    private var longitude : Double = 0.0

    companion object
    {
        val timeRunMilliseconds = MutableLiveData<Long>()
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
        val speed = MutableLiveData<Float>()
        val distance = MutableLiveData<Double>()
    }

    private fun postInitialValues()
    {
        timeRunInSeconds.postValue(0L)
        timeRunMilliseconds.postValue(0L)
        speed.postValue(0F)
        distance.postValue(0.0)
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onCreate()
    {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        isTracking.observe(this, Observer {
            updateLocationTracking(it)
            updateNotification(it)
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int
    {
        intent?.let {
            when (it.action) {
                "Start" -> {
                    if(isFirstRun)
                    {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        startTimer()
                    }
                }

                "Pause" -> {
                    isTracking.postValue(false)
                    isTimerEnabled = false
                    speed.postValue(0F )
                }

                "Stop" -> {
                    print("PlaceHolderStop")
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private var isTimerEnabled = false
    private var lapTime = 0L
    private var timeRun = 0L
    private var timeStarted = 0L
    private var lastSecondTimestamp = 0L

    private fun startTimer()
    {
        addEmptyPolyline()
        isTracking.postValue(true)
        timeStarted = System.currentTimeMillis()
        isTimerEnabled = true
        CoroutineScope(Dispatchers.Main).launch {
            while(isTracking.value!!)
            {
                lapTime = System.currentTimeMillis() - timeStarted
                timeRunMilliseconds.postValue(timeRun + lapTime)
                if(timeRunMilliseconds.value!! >= lastSecondTimestamp + 1000L)
                {
                    timeRunInSeconds.postValue(timeRunInSeconds.value!! + 1)
                    lastSecondTimestamp += 1000L
                }
                delay(50L)
            }
            timeRun += lapTime
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean)
    {
        if(isTracking)
        {
            val locationRequest = LocationRequest.create().apply {
                interval = 5000
                fastestInterval = 2000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                maxWaitTime = 5000
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun updateNotification(isTracking: Boolean) {
        val notificationText = if(isTracking) "Pause" else "Resume"
        val pendingIntent = if(isTracking) {
            val pauseIntent = Intent(this, RunTracking::class.java).apply {
                action = "Pause"
            }
            PendingIntent.getService(this, 1, pauseIntent, FLAG_UPDATE_CURRENT)
        } else {
            val resumeIntent = Intent(this, RunTracking::class.java).apply {
                action = "Start"
            }
            PendingIntent.getService(this, 2, resumeIntent, FLAG_UPDATE_CURRENT)
        }

        val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationBuilder.javaClass.getDeclaredField("mActions").apply {
            isAccessible = true
            set(notificationBuilder, ArrayList<NotificationCompat.Action>())
        }

        notificationBuilder = notificationBuilder.addAction(
            R.drawable.ic_baseline_directions_run_24,
            notificationText,
            pendingIntent
        )
        notificationManger.notify(1, notificationBuilder.build())
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            if(isTracking.value!!)
            {
                result.locations.let { locations ->
                    for(location in locations)
                    {
                        addPathPoint(location)
                    }
                }
            }
        }
    }

    private fun addPathPoint(location: Location?)
    {
        location?.let {
            val position = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(position)
                pathPoints.postValue(this)
            }
            if (location.hasSpeed())
            {
                speed.postValue(location.speed)
            } else { speed.postValue(0F ) }
            updateDistance(location.latitude, location.longitude)
        }
    }

    private fun updateDistance(lat: Double, long: Double)
    {
        if (latitude == 0.0 && longitude == 0.0)
        {
            latitude = lat
            longitude = long
        } else {
            val results = FloatArray(1)
            Location.distanceBetween(latitude, longitude, lat, long, results)
            distance.postValue(distance.value?.plus(results[0] * 0.000621371)) // weird number converts meters to miles
            latitude = lat
            longitude = long
        }
    }

    private fun addEmptyPolyline() = pathPoints.value?.apply {
        add(mutableListOf())
        pathPoints.postValue(this)
    } ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService()
    {
        startTimer()

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        notificationBuilder = NotificationCompat.Builder(this, "Tracking_Channel")
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_baseline_directions_run_24)
            .setContentTitle("GymApp")
            .setContentText("00:00:00")
            .setContentIntent(getMainActivityPendingIntent())

        startForeground(1, notificationBuilder.build())

        timeRunInSeconds.observe(this, Observer {
            val notification = notificationBuilder
                .setContentText(formatTime(timeRunInSeconds.value!!))
            notificationManager.notify(1, notification.build())
        })
    }

    private fun formatTime(seconds: Long) : String
    {
        val hours : Long = seconds / 3600
        val minutes : Long = (seconds % 3600) / 60
        val secondsRemaining : Long = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secondsRemaining);
    }

    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java).also {
            it.action = "ShowTrackingFrag"
        },
        FLAG_UPDATE_CURRENT
    )

    private fun createNotificationChannel (notificationManger: NotificationManager) {
        val channel = NotificationChannel("Tracking_Channel", "Tracking", IMPORTANCE_LOW)
        notificationManger.createNotificationChannel(channel)
    }
}
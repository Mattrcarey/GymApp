package com.example.gymapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gymapp.runDB.Run
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import java.lang.Double.max
import java.lang.Double.min
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt


class StartedRunFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var currentSpeed: TextView
    private lateinit var miles: TextView
    private lateinit var timer: Chronometer
    private lateinit var startRun: Button
    private lateinit var pauseRun: Button
    private lateinit var resumeRun: Button
    private lateinit var endRun: Button
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallBack: LocationCallback
    private var coords: MutableList<Double> = mutableListOf(91.0,-91.0,181.0,-181.0)
    private var time: Long = 0
    private var latitude: Double = 0.0
    private var longitude : Double = 0.0
    private var distance: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val applicationContext = requireContext().applicationContext

        val view: View = inflater.inflate(
            com.example.gymapp.R.layout.fragment_started_run,
            container,
            false
        )
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync (this)

        locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 5000
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(applicationContext)

        locationCallBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                updateUI(locationResult.lastLocation)
            }
        }

        return view
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currentSpeed = requireView().findViewById<TextView>(R.id.currentSpeed)
        miles = requireView().findViewById<TextView>(R.id.tv_miles)
        timer = requireView().findViewById<Chronometer>(R.id.tvTimer)
        startRun = requireView().findViewById<Button>(R.id.btnStartRun)
        pauseRun = requireView().findViewById<Button>(R.id.btnPauseRun)
        resumeRun = requireView().findViewById<Button>(R.id.btnResumeRun)
        endRun = requireView().findViewById<Button>(R.id.btnEndRun)

        startRun.setOnClickListener { view ->
            timer.base = SystemClock.elapsedRealtime() - time
            timer.start()
            startTracking()
            startRun.visibility = View.INVISIBLE
            pauseRun.visibility = View.VISIBLE
            endRun.visibility = View.VISIBLE
        }

        pauseRun.setOnClickListener { view ->
            timer.stop()
            time = SystemClock.elapsedRealtime() - timer.base
            stopTracking()
            resumeRun.visibility = View.VISIBLE
            pauseRun.visibility = View.INVISIBLE
        }

        resumeRun.setOnClickListener { view ->
            timer.start()
            startTracking()
            pauseRun.visibility = View.VISIBLE
            resumeRun.visibility = View.GONE

        }

        endRun.setOnClickListener { view ->
            timer.stop()
            time = SystemClock.elapsedRealtime() - timer.base
            stopTracking()
            saveData()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveData() {
        val bounds = LatLngBounds(
            LatLng(coords[0],coords[2]),
            LatLng(coords[1],coords[3])
        )
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 25))

        //val run: Run = Run() //TODO
    }

    private fun stopTracking() {
        latitude = 0.0
        longitude = 0.0
        fusedLocationProviderClient.removeLocationUpdates(locationCallBack)
    }

    @SuppressLint("MissingPermission")
    private fun startTracking() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
        }

        mMap.uiSettings.isZoomControlsEnabled = true

        setUpMap()
    }

    @SuppressLint("MissingPermission")
    private fun setUpMap() {
        mMap.isMyLocationEnabled = true
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 18f))
                updateUI(location)
            }
        }
    }

    private fun updateUI(location: Location){
        if(location.hasSpeed()) {
            currentSpeed.setText(String.format("%.2f",location.speed.toDouble()))
        }
        else {
            currentSpeed.setText("00.00")
        }
        if (location.latitude < coords[0]){
            coords[0] = location.latitude
        }
        if (location.latitude > coords[1]){
            coords[1] = location.latitude
        }
        if (location.longitude < coords[2]){
            coords[2] = location.longitude
        }
        if (location.longitude > coords[3]){
            coords[3] = location.longitude
        }
        updateDistance(location.latitude, location.longitude)
    }

    private fun updateDistance(lat: Double, long: Double){
        if (latitude == 0.0 && longitude == 0.0){
            latitude = lat
            longitude = long
        }
        else {
            val latdif = abs(max(lat,latitude) - min(lat,latitude))
            val longdif = abs(max(long,longitude) - min(long,longitude))
            distance += sqrt(latdif.pow(2) + longdif.pow(2))
            mMap.addPolyline(PolylineOptions()
                .color(-0x7e387c)
                .clickable(false)
                .add(
                    LatLng(latitude, longitude),
                    LatLng(lat,long)
                ))
            miles.setText(String.format("%.2f",distance))
            latitude = lat
            longitude = long
        }
    }
}
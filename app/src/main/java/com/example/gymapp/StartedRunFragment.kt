package com.example.gymapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
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
    private lateinit var timer: TextView
    private lateinit var startRun: Button
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallBack: LocationCallback
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
        timer = requireView().findViewById<TextView>(R.id.tvTimer)
        startRun = requireView().findViewById<Button>(R.id.btnStartRun)
        startRun.setOnClickListener { view ->
            startTracking()
        }

        super.onViewCreated(view, savedInstanceState)
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
            miles.setText(String.format("%.2f",distance))
        }
    }

}
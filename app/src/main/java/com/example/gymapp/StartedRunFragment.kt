package com.example.gymapp

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment


class StartedRunFragment : Fragment(R.layout.fragment_started_run), OnMapReadyCallback {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var timeRunMillisecs: Long = 0

    private lateinit var lastLocation: Location
    private lateinit var currentSpeed: TextView
    private lateinit var miles: TextView
    private lateinit var timer: Chronometer
    private lateinit var startRun: Button
    private lateinit var pauseRun: Button
    private lateinit var resumeRun: Button
    private lateinit var endRun: Button
    private var navController : NavController?= null
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

        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync (this)

        return view
    }


    private fun subscribeToObservers() {
        RunTracking.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLastLocation()
            moveCameraToUser()
        })

        RunTracking.timeRunMillisecs.observe(viewLifecycleOwner, Observer {
            timeRunMillisecs = it
        })
    }


    private fun moveCameraToUser() {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty() && ::mMap.isInitialized) {
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    18F
                )
            )
        }
    }


    private fun addAllPolylines() {
        for(polyline in pathPoints) {
            val polylineOptions = PolylineOptions()
                .color(-0x7e387c)
                .clickable(false)
                .addAll(polyline)

            if(::mMap.isInitialized) {
                mMap.addPolyline(polylineOptions)
            }
        }
    }


    private fun addLastLocation() {
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1) {
            val penultimateLatLng = pathPoints.last()[pathPoints.last().size -2]
            val lastLatLng = pathPoints.last().last()
            val polyLineOptions = PolylineOptions()
                .color(-0x7e387c)
                .clickable(false)
                .add(
                    penultimateLatLng,
                    lastLatLng
                )
            if(this::mMap.isInitialized) {
                mMap.addPolyline(polyLineOptions)
            }
        }
    }


    override fun onResume() {
        mapFragment.getMapAsync(this)
        super.onResume()
    }


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)

        currentSpeed = requireView().findViewById<TextView>(R.id.currentSpeed)
        miles = requireView().findViewById<TextView>(R.id.tv_miles)
        timer = requireView().findViewById<Chronometer>(R.id.tvTimer)
        startRun = requireView().findViewById<Button>(R.id.btnStartRun)
        pauseRun = requireView().findViewById<Button>(R.id.btnPauseRun)
        resumeRun = requireView().findViewById<Button>(R.id.btnResumeRun)
        endRun = requireView().findViewById<Button>(R.id.btnEndRun)

        startRun.setOnClickListener { view ->
            timer.base = SystemClock.elapsedRealtime() - timeRunMillisecs
            timer.start()
            startTracking()
            startRun.visibility = View.INVISIBLE
            pauseRun.visibility = View.VISIBLE
            endRun.visibility = View.VISIBLE
        }

        pauseRun.setOnClickListener { view ->
            timer.stop()
            stopTracking()
            resumeRun.visibility = View.VISIBLE
            pauseRun.visibility = View.INVISIBLE
        }

        resumeRun.setOnClickListener { view ->
            timer.base = SystemClock.elapsedRealtime() - timeRunMillisecs
            timer.start()
            startTracking()
            pauseRun.visibility = View.VISIBLE
            resumeRun.visibility = View.GONE

        }

        endRun.setOnClickListener { view ->
            timer.stop()
            stopTracking()
//            saveData()
        }

        subscribeToObservers()
        super.onViewCreated(view, savedInstanceState)
    }


//    private fun saveData() {
//        val bounds = LatLngBounds(
//            LatLng(coords[0],coords[2]),
//            LatLng(coords[1],coords[3])
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 25))
//        mMap.snapshot { bmp ->
//            val avgSpeed = round((distance/time)*10) / 10f
//            val dateTimeStamp = Calendar.getInstance().timeInMillis
//            val run = Run(bmp, dateTimeStamp, avgSpeed.toFloat(), distance.toFloat(), time)
//            MainActivity.runDao.insertRun(run)
//            navController!!.navigate(R.id.action_startedRunFragment_to_runFragment)
//        }
//    }

    private fun stopTracking() {
        commandTracker("Pause")
        latitude = 0.0
        longitude = 0.0
    }

    @SuppressLint("MissingPermission")
    private fun startTracking() {
        commandTracker("Start")
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            mMap = googleMap
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        addAllPolylines()
        mMap.isMyLocationEnabled = true
        moveCameraToUser()
    }


//    private fun updateUI(location: Location){
//        if(location.hasSpeed()) {
//            currentSpeed.setText(String.format("%.2f",location.speed.toDouble()))
//        }
//        else {
//            currentSpeed.setText("00.00")
//        }
//        if (location.latitude < coords[0]){
//            coords[0] = location.latitude
//        }
//        if (location.latitude > coords[1]){
//            coords[1] = location.latitude
//        }
//        if (location.longitude < coords[2]){
//            coords[2] = location.longitude
//        }
//        if (location.longitude > coords[3]){
//            coords[3] = location.longitude
//        }
//        updateDistance(location.latitude, location.longitude)
//    }

//    private fun updateDistance(lat: Double, long: Double){
//        if (latitude == 0.0 && longitude == 0.0){
//            latitude = lat
//            longitude = long
//        }
//        else {
//            val latdif = abs(max(lat,latitude) - min(lat,latitude))
//            val longdif = abs(max(long,longitude) - min(long,longitude))
//            distance += sqrt(latdif.pow(2) + longdif.pow(2))
//            mMap.addPolyline(PolylineOptions()
//                .color(-0x7e387c)
//                .clickable(false)
//                .add(
//                    LatLng(latitude, longitude),
//                    LatLng(lat,long)
//                ))
//            miles.setText(String.format("%.2f",distance))
//            latitude = lat
//            longitude = long
//        }
//    }

    private fun commandTracker(action: String) {
        Intent(requireContext(), RunTracking::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    }
}
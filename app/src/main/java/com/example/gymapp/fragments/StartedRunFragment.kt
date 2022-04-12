package com.example.gymapp.fragments

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.gymapp.R
import com.example.gymapp.services.Polyline
import com.example.gymapp.services.RunTracking
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment


class StartedRunFragment : Fragment(), OnMapReadyCallback
{
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var mMap: GoogleMap
    private var isTracking = false
    private var pathPoints = mutableListOf<Polyline>()
    private var timeRunMilliseconds: Long = 0

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
        ): View
    {
        val view: View = inflater.inflate(R.layout.fragment_started_run, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync (this)
        return view
    }

    private fun subscribeToObservers()
    {
        RunTracking.isTracking.observe(viewLifecycleOwner, Observer
        {
            isTracking = it
            if(isTracking)
            {
                timer.base = SystemClock.elapsedRealtime() - RunTracking.timeRunMilliseconds.value!!
                timer.start()
                startRun.visibility = View.INVISIBLE
                pauseRun.visibility = View.VISIBLE
                endRun.visibility = View.VISIBLE
                resumeRun.visibility = View.GONE
            } else {
                timer.base = SystemClock.elapsedRealtime() - RunTracking.timeRunMilliseconds.value!!
                timer.stop()
                endRun.visibility = View.VISIBLE
                startRun.visibility = View.INVISIBLE
                resumeRun.visibility = View.VISIBLE
                pauseRun.visibility = View.INVISIBLE
            }
        })

        RunTracking.pathPoints.observe(viewLifecycleOwner, Observer
        {
            pathPoints = it
            addLastLocation()
            moveCameraToUser()
        })

        RunTracking.timeRunMilliseconds.observe(viewLifecycleOwner, Observer
        {
            timeRunMilliseconds = it
        })

        RunTracking.speed.observe(viewLifecycleOwner, Observer
        {
            currentSpeed.text = String.format("%.2f",it.toDouble())
        })

        RunTracking.distance.observe(viewLifecycleOwner, Observer
        {
            miles.text = String.format("%.2f", it)
        })
    }

    private fun moveCameraToUser()
    {
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty() && ::mMap.isInitialized)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pathPoints.last().last(), 18F))
        }
    }

    private fun addAllPolylines()
    {
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
        subscribeToObservers()
        mapFragment.getMapAsync(this)
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navController = Navigation.findNavController(view)

        currentSpeed = requireView().findViewById<TextView>(R.id.currentSpeed)
        miles = requireView().findViewById<TextView>(R.id.tv_miles)
        timer = requireView().findViewById<Chronometer>(R.id.tvTimer)
        startRun = requireView().findViewById<Button>(R.id.btnStartRun)
        pauseRun = requireView().findViewById<Button>(R.id.btnPauseRun)
        resumeRun = requireView().findViewById<Button>(R.id.btnResumeRun)
        endRun = requireView().findViewById<Button>(R.id.btnEndRun)

        startRun.setOnClickListener { startTracking() }
        pauseRun.setOnClickListener { stopTracking() }
        resumeRun.setOnClickListener { startTracking() }
        endRun.setOnClickListener {
            stopTracking()
            // TODO: Oops I broke saveData
//            saveData()
        }

        subscribeToObservers()
        super.onViewCreated(view, savedInstanceState)
    }

// TODO: Fix this method

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

    private fun stopTracking()
    {
        commandTracker("Pause")
        latitude = 0.0
        longitude = 0.0
    }

    private fun startTracking()
    {
        commandTracker("Start")
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap)
    {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        addAllPolylines()
        mMap.isMyLocationEnabled = true
    }

// TODO: Maybe don't need this anymore

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


    private fun commandTracker(action: String)
    {
        Intent(requireContext(), RunTracking::class.java).also{
            it.action = action
            requireContext().startService(it)
        }

    }
}
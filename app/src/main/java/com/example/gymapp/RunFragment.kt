package com.example.gymapp

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.gymapp.runDB.RunDAO


/**
 * A simple [Fragment] subclass.
 * Use the [RunFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RunFragment : Fragment() {

    private var navController : NavController?= null
    private var runDao : RunDAO?= null
    var requestPermissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        permissions.entries.forEach {
            Log.e("DEBUG", "${it.key} = ${it.value}")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val applicationContext = requireContext().applicationContext
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_run, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runDao = MainActivity.runDao
        navController = Navigation.findNavController(view)
        val applicationContext = requireContext().applicationContext
        val newRun : Button = requireView().findViewById<Button>(R.id.startRunningfrag)
        newRun.setOnClickListener { view ->
            if (!hasLocationPermission(applicationContext)) {
                requestMyPermissions()
            }
            else {
                navController!!.navigate(R.id.action_runFragment_to_startedRunFragment)
            }
        }
    }

    private fun hasFineLocationPermission(context: Context) : Boolean {
        return (ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun hasCoarseLocationPermission(context: Context) : Boolean {
        return (ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun hasBackgroundLocationPermission(context: Context) : Boolean {
        return (ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    fun hasLocationPermission(context: Context) : Boolean {
//        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            return (hasFineLocationPermission(context) && hasCoarseLocationPermission(context))
//        } else {
//            return (hasFineLocationPermission(context) &&
//                    hasCoarseLocationPermission(context) &&
//                    hasBackgroundLocationPermission(context))
//        }
        return hasFineLocationPermission(context)
    }

    fun requestMyPermissions() {
        val applicationContext = requireContext().applicationContext

        var permissionsNeeded = mutableListOf<String>()
        if (!hasLocationPermission(applicationContext)) {
            if (!hasFineLocationPermission(applicationContext)) {
                permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
//            if (!hasCoarseLocationPermission(applicationContext)) {
//                permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
//                if (!hasBackgroundLocationPermission(applicationContext)) {
//                    permissionsNeeded.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
//                }
//            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            for(x in permissionsNeeded) {
                requestPermissionsLauncher.launch(permissionsNeeded.toTypedArray())
            }
        }
    }




}
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.runDB.Run
import com.example.gymapp.runDB.RunDAO



class RunFragment : Fragment(), OnItemClickListener {

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

    override fun onItemClick(position: Int) {
        val applicationContext = requireContext().applicationContext
        val runlist : ArrayList<Run> = MainActivity.runDao.getRuns() as ArrayList<Run>
        val clickedItem : Run = runlist[position]
        val bundle = Bundle()
//        bundle.putInt("EID", clickedItem.exerciseID)
//        navController?.navigate(R.id.action_exerciseFragment_to_exerciseEntry, bundle)
//        TODO on click move to a new run page with more details
    }

    private fun viewRuns(){
        val applicationContext = requireContext().applicationContext
        val runList : ArrayList<Run> = MainActivity.runDao.getRuns() as ArrayList<Run>
        val adapter =  RunAdapter(runList, this)
        val rv : RecyclerView = requireView().findViewById(R.id.rvRunList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false ) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    override fun onResume() {
        viewRuns()
        super.onResume()
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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return (hasFineLocationPermission(context) && hasCoarseLocationPermission(context))
        } else {
            return (hasFineLocationPermission(context) &&
                    hasCoarseLocationPermission(context) &&
                    hasBackgroundLocationPermission(context))
        }
    }
//        private fun hasLocationPermission(context: Context) : Boolean {
//            return hasFineLocationPermission(context)
//        }

    private fun requestMyPermissions() {
        val applicationContext = requireContext().applicationContext

        var permissionsNeeded = mutableListOf<String>()
        if (!hasLocationPermission(applicationContext)) {
            if (!hasFineLocationPermission(applicationContext)) {
                permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (!hasCoarseLocationPermission(applicationContext)) {
                permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (!hasBackgroundLocationPermission(applicationContext)) {
                    permissionsNeeded.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            }
        }

        if (permissionsNeeded.isNotEmpty()) {
            for(x in permissionsNeeded)
                requestPermissionsLauncher.launch(permissionsNeeded.toTypedArray())
        }

    }
}
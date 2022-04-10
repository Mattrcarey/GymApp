package com.example.gymapp.fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.MainActivity
import com.example.gymapp.R
import com.example.gymapp.adapters.OnItemClickListener
import com.example.gymapp.adapters.RunAdapter
import com.example.gymapp.runDB.Run
import com.example.gymapp.runDB.RunDAO


class RunFragment : Fragment(), OnItemClickListener {

    lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>

    private var navController : NavController?= null
    private var runDao : RunDAO?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View?
    {
        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            permissions.entries.forEach {
                Log.e("DEBUG", "${it.key} = ${it.value}")
            }
        }
        return inflater.inflate(R.layout.fragment_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        runDao = MainActivity.runDao
        navController = Navigation.findNavController(view)
        val applicationContext = requireContext().applicationContext
        val newRun : Button = requireView().findViewById<Button>(R.id.startRunningfrag)
        newRun.setOnClickListener {
            if (!hasLocationPermission(applicationContext))
            {
                requestMyPermissions()
            } else {
                navController!!.navigate(R.id.action_runFragment_to_startedRunFragment)
            }
        }
    }

    override fun onItemClick(position: Int)
    {
//        val runList : ArrayList<Run> = MainActivity.runDao.getRuns() as ArrayList<Run>
//        val clickedItem : Run = runList[position]
//        val bundle = Bundle()
//        bundle.putInt("EID", clickedItem.exerciseID)
//        navController?.navigate(R.id.action_exerciseFragment_to_exerciseEntry, bundle)
//        TODO: on click move to a new run page displaying more details
    }

    private fun viewRuns()
    {
        val applicationContext = requireContext().applicationContext
        val runList : ArrayList<Run> = MainActivity.runDao.getRuns() as ArrayList<Run>
        val adapter =  RunAdapter(runList, this)
        val rv : RecyclerView = requireView().findViewById(R.id.rvRunList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false )
        rv.adapter = adapter
    }

    override fun onResume()
    {
        viewRuns()
        super.onResume()
    }


    private fun hasFineLocationPermission(context: Context) : Boolean
    {
        return (ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun hasCoarseLocationPermission(context: Context) : Boolean
    {
        return (ActivityCompat.checkSelfPermission(context,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
    }

    private fun hasLocationPermission(context: Context) : Boolean
    {
        return (hasFineLocationPermission(context) && hasCoarseLocationPermission(context))
    }

    private fun requestMyPermissions()
    {
        val applicationContext = requireContext().applicationContext
        val permissionsNeeded = mutableListOf<String>()
            if (!hasFineLocationPermission(applicationContext))
            {
                permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }

            if (!hasCoarseLocationPermission(applicationContext))
            {
                permissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            }

        if (permissionsNeeded.isNotEmpty())
        {
            permissionsLauncher.launch(permissionsNeeded.toTypedArray())
        }
    }
}
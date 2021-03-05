package com.example.gymapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutSetup.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutSetup : Fragment() {

    companion object{
        var WID: Int = 0
        private var navController : NavController?= null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_workout_setup, container, false)
        val btnAddE : Button = view.findViewById<Button>(R.id.btnAddE)
        btnAddE.setOnClickListener { view ->
            addExercisesToWorkout()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val applicationContext = requireContext().applicationContext
        WID = arguments?.getInt("WID")!!
        Toast.makeText(applicationContext, "Item $WID clicked", Toast.LENGTH_SHORT).show()
        if (WID != null) {
            viewExercises(WID)
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mContext = context
    }

    fun addExercisesToWorkout(){
        val applicationContext = requireContext().applicationContext
        val bundle = Bundle()
        bundle.putInt("WID", WID)

        navController?.navigate(R.id.action_workoutSetup_to_addEToW, bundle)
    }


    private fun viewExercises(WID : Int){
        val applicationContext = requireContext().applicationContext
        val exercisesList : ArrayList<Exercises> =  MainActivity.databaseHandler.getExercises(applicationContext, WID)
        val adapter =  ExerciseAdapter(applicationContext, exercisesList)
        val rv : RecyclerView = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, true ) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }


    override fun onResume() {
        viewExercises(WID)
        super.onResume()
    }
}
package com.example.gymapp.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.MainActivity
import com.example.gymapp.R
import com.example.gymapp.adapters.ExerciseAdapter
import com.example.gymapp.adapters.OnItemClickListener
import com.example.gymapp.models.Exercises


class WorkoutSetup : Fragment(), OnItemClickListener {

    companion object {
        var WID: Int = 0
    }

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_workout_setup, container, false)
        val btnAddE: Button = view.findViewById(R.id.btnAddE)
        btnAddE.setOnClickListener { addExercisesToWorkout() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        WID = arguments?.getInt("WID")!!
        viewExercises(WID)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        navController?.popBackStack()
        return true
    }

    override fun onItemClick(position: Int) {
        val exerciseList: ArrayList<Exercises> = MainActivity.databaseHandler.getExercises(WID)
        val clickedItem: Exercises = exerciseList[position]
        val bundle = Bundle()
        bundle.putInt("EID", clickedItem.exerciseID)
        navController?.navigate(R.id.action_workoutSetup_to_ExerciseEntry, bundle)
    }

    private fun addExercisesToWorkout() {
        val bundle = Bundle()
        bundle.putInt("WID", WID)
        navController?.navigate(R.id.action_workoutSetup_to_addEToW, bundle)
    }

    private fun viewExercises(WID: Int) {
        val applicationContext = requireContext().applicationContext
        val exercisesList: ArrayList<Exercises> = MainActivity.databaseHandler.getExercises(WID)
        val adapter = ExerciseAdapter(exercisesList, this)
        val rv: RecyclerView = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        rv.adapter = adapter
    }

    override fun onResume() {
        viewExercises(WID)
        super.onResume()
    }
}
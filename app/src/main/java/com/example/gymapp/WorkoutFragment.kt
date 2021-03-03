package com.example.gymapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * A simple [Fragment] subclass.
 * Use the [WorkoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WorkoutFragment : Fragment(), WorkoutAdapter.OnItemClickListener {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_workout, container, false)
        val btnAdd : Button = view.findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener { view ->
            addRecord()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val applicationContext = requireContext().applicationContext
        viewWorkouts()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mContext = context
    }

    override fun onItemClick(position: Int) {
        val applicationContext = requireContext().applicationContext
        val workoutsList : ArrayList<Workouts> =  MainActivity.databaseHandler.getWorkouts(applicationContext)
        val adapter =  WorkoutAdapter(applicationContext, workoutsList, this)
        val clickedItem : Workouts = workoutsList[position]
        Toast.makeText(applicationContext, "Item $position clicked", Toast.LENGTH_SHORT).show()
        Toast.makeText(applicationContext, "workoutid ${clickedItem.workoutsID} clicked", Toast.LENGTH_SHORT).show()
    }

    private fun viewWorkouts(){
        val applicationContext = requireContext().applicationContext
        val workoutsList : ArrayList<Workouts> =  MainActivity.databaseHandler.getWorkouts(applicationContext)
        val adapter =  WorkoutAdapter(applicationContext, workoutsList, this)
        val rv : RecyclerView = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, true ) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    private fun addRecord() {
        val applicationContext = activity?.applicationContext
        val etName = view?.findViewById<EditText>(R.id.etName)
        val name = etName?.text.toString()
        if (name.isNotEmpty()) {
            val workouts = Workouts()
            workouts.workoutsName = name
            if (applicationContext != null) {
                MainActivity.databaseHandler.addWorkout(applicationContext, workouts)
            }

            Toast.makeText(applicationContext, name, Toast.LENGTH_SHORT).show()
            etName?.text?.clear()

        } else {
            Toast.makeText(
                applicationContext,
                "Name cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
        viewWorkouts()
    }

    override fun onResume() {
        viewWorkouts()
        super.onResume()
    }

}
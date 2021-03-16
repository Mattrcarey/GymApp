package com.example.gymapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ExerciseFragment : Fragment(), OnItemClickListener {


    private var navController : NavController?= null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_exercise, container, false)
        val btnAdd : Button = view.findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener { view ->
            addRecord()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val applicationContext = requireContext().applicationContext
        viewExercises()
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mContext = context
    }


    override fun onItemClick(position: Int) {
        val applicationContext = requireContext().applicationContext
        val exerciseList : ArrayList<Exercises> =  MainActivity.databaseHandler.getExercises(applicationContext)
        val adapter =  ExerciseAdapter(applicationContext, exerciseList, this)
        val clickedItem : Exercises = exerciseList[position]
        val bundle = Bundle()
        bundle.putInt("EID", clickedItem.exerciseID)
        navController?.navigate(R.id.action_exerciseFragment_to_exerciseEntry, bundle)
    }

    private fun viewExercises(){
        val applicationContext = requireContext().applicationContext
        val exercisesList : ArrayList<Exercises> =  MainActivity.databaseHandler.getExercises(applicationContext)
        val adapter =  ExerciseAdapter(applicationContext, exercisesList, this)
        val rv : RecyclerView = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false ) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    private fun addRecord() {
        val applicationContext = activity?.applicationContext
        val etName = view?.findViewById<EditText>(R.id.etName)
        val name = etName?.text.toString()
        var error : Long = 0
        if (name.isNotEmpty()) {
            val exercises = Exercises()
            exercises.exerciseName = name
            if (applicationContext != null) {
                error = MainActivity.databaseHandler.addExercise(applicationContext, exercises)
            }

            etName?.text?.clear()

        } else {
            Toast.makeText(
                    applicationContext,
                    "Name cannot be blank",
                    Toast.LENGTH_SHORT
            ).show()
        }
        if(error.toInt() == -1){
            Toast.makeText(
                applicationContext,
                "Name must be unique",
                Toast.LENGTH_SHORT
            ).show()
        }
        viewExercises()
    }

    override fun onResume() {
        viewExercises()
        super.onResume()
    }

}
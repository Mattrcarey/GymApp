package com.example.gymapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.MainActivity
import com.example.gymapp.R
import com.example.gymapp.adapters.AddExerciseAdapter
import com.example.gymapp.adapters.OnItemClickListener
import com.example.gymapp.models.Exercises
import com.example.gymapp.models.Links


class AddNewEToW : Fragment(), OnItemClickListener
{
    companion object
    {
        var WID: Int = 0
        lateinit var exerciseList : ArrayList<Exercises>
        lateinit var rv : RecyclerView
    }

    private var navController : NavController?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView
    (
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        val view: View = inflater.inflate(R.layout.fragment_add_e_to_w, container, false)
        val btnAdd : Button = view.findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener { addRecord() }
        val addSelected : Button = view.findViewById<Button>(R.id.addSelected)
        addSelected.setOnClickListener { addToWorkout() }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        WID = arguments?.getInt("WID")!!
        getExercises()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        navController?.popBackStack()
        return true
    }

    override fun onItemClick(position: Int)
    {
        val exercise : Exercises = exerciseList[position]
        exercise.isChecked = !exercise.isChecked
        exerciseList[position] = exercise
        rv.adapter?.notifyItemChanged(position)
    }

    private fun getExercises()
    {
        val applicationContext = requireContext().applicationContext
        exerciseList =  MainActivity.databaseHandler.getExercises()
        val adapter =  AddExerciseAdapter(exerciseList, this)
        val rv : RecyclerView = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false )
        rv.adapter = adapter
    }

    private fun viewExercises()
    {
        val applicationContext = requireContext().applicationContext
        val adapter =  AddExerciseAdapter(exerciseList, this)
        rv = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false )
        rv.adapter = adapter
    }

    private fun addRecord()
    {
        val applicationContext = activity?.applicationContext
        val etName = view?.findViewById<EditText>(R.id.etName)
        val name = etName?.text.toString()
        var error = -2
        if (name.isNotEmpty())
        {
            val exercises = Exercises()
            exercises.exerciseName = name
            if (applicationContext != null)
            {
                error = MainActivity.databaseHandler.addExercise(exercises).toInt()
            }

            etName?.text?.clear()
        } else {
            Toast.makeText(applicationContext, "Name cannot be blank", Toast.LENGTH_SHORT).show()
        }

        if(error == -1)
        {
            Toast.makeText(applicationContext, "Name must be unique", Toast.LENGTH_SHORT).show()
        } else if (error >= 0 ) {
            getNewExercise(name)
        }
    }

    private fun getNewExercise(name: String)
    {
        val applicationContext = activity?.applicationContext
        if (applicationContext != null)
        {
            exerciseList.add(MainActivity.databaseHandler.getExerciseByName(name)[0])
            rv.adapter?.notifyItemInserted(exerciseList.size)
        }
    }

    private fun addToWorkout()
    {
        val applicationContext = activity?.applicationContext
        for (exercise in exerciseList){
            if (exercise.isChecked){
                val link = Links()
                link.eid = exercise.exerciseID
                link.wid = WID
                if (applicationContext != null) {
                    MainActivity.databaseHandler.addLink(link)
                }
            }
        }
        navController?.popBackStack()
    }

    override fun onResume()
    {
        viewExercises()
        super.onResume()
    }
}
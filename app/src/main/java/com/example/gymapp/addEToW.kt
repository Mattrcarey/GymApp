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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [addNewEToW.newInstance] factory method to
 * create an instance of this fragment.
 */
class addNewEToW : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_e_to_w, container, false)
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mContext = context
    }

    private fun viewExercises(){
        val applicationContext = requireContext().applicationContext
        val exercisesList : ArrayList<Exercises> =  MainActivity.databaseHandler.getExercises(applicationContext)
        val adapter =  ExerciseAdapter(applicationContext, exercisesList)
        val rv : RecyclerView = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, true ) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    private fun addRecord() {
        val applicationContext = activity?.applicationContext
        val etName = view?.findViewById<EditText>(R.id.etName)
        val name = etName?.text.toString()
        if (name.isNotEmpty()) {
            val exercises = Exercises()
            exercises.exerciseName = name
            if (applicationContext != null) {
                MainActivity.databaseHandler.addExercise(applicationContext, exercises)
            }

            Toast.makeText(applicationContext, name, Toast.LENGTH_LONG).show()
            etName?.text?.clear()

        } else {
            Toast.makeText(
                applicationContext,
                "Name cannot be blank",
                Toast.LENGTH_LONG
            ).show()
        }
        viewExercises()
    }

    override fun onResume() {
        viewExercises()
        super.onResume()
    }
}
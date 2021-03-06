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

/**
 * A simple [Fragment] subclass.
 * Use the [addNewEToW.newInstance] factory method to
 * create an instance of this fragment.
 */
class addNewEToW : Fragment(), OnItemClickListener {




    companion object{
        var WID: Int = 0
        var selected : BooleanArray? = null
        var selectedID : java.util.ArrayList<Exercises>? = null
        private var navController : NavController?= null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_add_e_to_w, container, false)
        val btnAdd : Button = view.findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener { view ->
            addRecord()
        }
        val addselected : Button = view.findViewById<Button>(R.id.addSelected)
        addselected.setOnClickListener { view ->
            addToWorkout()
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val applicationContext = requireContext().applicationContext
        WID = arguments?.getInt("WID")!!
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
        selected?.set(position, !selected?.get(position)!!)
        //TODO change checked state
    }

    private fun getExercises(){
        //TODO get the list of exercises and save it
    }

    private fun viewExercises(){
        val applicationContext = requireContext().applicationContext
        val exercisesList : ArrayList<Exercises> =  MainActivity.databaseHandler.getExercises(applicationContext)
        //TODO convert list of exercises to ADDexercises with checked state
        val adapter =  AddExerciseAdapter(applicationContext, exercisesList, this)
        val rv : RecyclerView = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, true ) as RecyclerView.LayoutManager
        rv.adapter = adapter
        selected = BooleanArray(adapter.itemCount)
        selectedID = exercisesList
    }

    private fun addRecord() {
        //TODO add new exercise to the list of exercises
        val applicationContext = activity?.applicationContext
        val etName = view?.findViewById<EditText>(R.id.etName)
        val name = etName?.text.toString()
        if (name.isNotEmpty()) {
            val exercises = Exercises()
            exercises.exerciseName = name
            if (applicationContext != null) {
                MainActivity.databaseHandler.addExercise(applicationContext, exercises)
            }

            Toast.makeText(applicationContext, "$name added", Toast.LENGTH_SHORT).show()
            etName?.text?.clear()

        } else {
            Toast.makeText(
                applicationContext,
                "Name cannot be blank",
                Toast.LENGTH_SHORT
            ).show()
        }
        viewExercises()
    }

    private fun addToWorkout() {
        val applicationContext = activity?.applicationContext
        for ((counter, i) in selected!!.withIndex()){
            if (i){
                val link = Links()
                link.eid = selectedID?.get(counter)!!.exerciseID
                link.wid = WID
                if (applicationContext != null) {
                    MainActivity.databaseHandler.addLink(applicationContext, link)
                }
            }
        }
        navController?.popBackStack()
    }

    override fun onResume() {
        viewExercises()
        super.onResume()
    }
}
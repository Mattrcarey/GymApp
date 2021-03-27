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
        private var navController : NavController?= null
        lateinit var exerciseList : ArrayList<Exercises>
        lateinit var rv : RecyclerView

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

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        val applicationContext = requireContext().applicationContext
//
//        super.onActivityCreated(savedInstanceState)
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        WID = arguments?.getInt("WID")!!
        getExercises()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val mContext = context
    }

    override fun onItemClick(position: Int) {
        var exercise : Exercises? = exerciseList?.get(position)
        if (exercise != null) {
            exercise.isChecked = !exercise.isChecked
        }
        if (exercise != null) {
            exerciseList?.set(position, exercise)
        }

        rv.adapter?.notifyItemChanged(position)
    }


    private fun getExercises(){
        val applicationContext = requireContext().applicationContext
        exerciseList =  MainActivity.databaseHandler.getExercises(applicationContext)
        var adapter =  AddExerciseAdapter(applicationContext, exerciseList, this)
        val rv : RecyclerView = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false ) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }


    private fun viewExercises(){
        val applicationContext = requireContext().applicationContext
        val adapter =  AddExerciseAdapter(applicationContext, exerciseList, this)
        rv = requireView().findViewById(R.id.rvItemsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false ) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    private fun addRecord() {
        val applicationContext = activity?.applicationContext
        val etName = view?.findViewById<EditText>(R.id.etName)
        val name = etName?.text.toString()
        var error : Long = -2
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
        if ((error.toInt()) >= 0 ) {
            getNewExercise(name)
        }
    }

    private fun getNewExercise(name: String){
        val applicationContext = activity?.applicationContext
        if (applicationContext != null) {
            exerciseList.add(MainActivity.databaseHandler.getExerciseByName(applicationContext, name)[0])
            rv.adapter?.notifyItemInserted(exerciseList.size)
        }

    }

    private fun addToWorkout() {
        val applicationContext = activity?.applicationContext
        for (i in exerciseList){
            if (i.isChecked){
                val link = Links()
                link.eid = i.exerciseID
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
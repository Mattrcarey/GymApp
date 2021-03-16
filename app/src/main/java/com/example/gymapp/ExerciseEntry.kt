package com.example.gymapp

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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ExerciseEntry : Fragment() {

    companion object{
        var EID: Int = 0
        private var navController : NavController?= null
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_exercise_entry, container, false)
        val btnAdd : Button = view.findViewById<Button>(R.id.btnAdd)
        btnAdd.setOnClickListener { view ->
            addRecord()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        val applicationContext = requireContext().applicationContext
        EID = arguments?.getInt("EID")!!
        viewRecords(EID)
        super.onActivityCreated(savedInstanceState)
    }

    private fun viewRecords(EID : Int){
        val applicationContext = requireContext().applicationContext
        val recordList : ArrayList<Records> =  MainActivity.databaseHandler.getRecords(applicationContext,EID)
        val adapter =  RecordAdapter(applicationContext, recordList)
        val rv : RecyclerView = requireView().findViewById(R.id.rvRecordsList)
        rv.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, true ) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    private fun addRecord() {
        val applicationContext = activity?.applicationContext
        val etWeight = view?.findViewById<EditText>(R.id.etWeight)
        val etReps = view?.findViewById<EditText>(R.id.etReps)
        val weight = etWeight?.text.toString()
        val reps = etReps?.text.toString()
        var error : Long = 0
        if(weight.toFloatOrNull() == null){
            Toast.makeText(
                    applicationContext,
                    "weight must be a number",
                    Toast.LENGTH_SHORT
            ).show()
            return
        }
        if(reps.toIntOrNull() == null) {
            Toast.makeText(
                    applicationContext,
                    "reps must be a number",
                    Toast.LENGTH_SHORT
            ).show()
            return
        }
        if (weight.isNotEmpty() && reps.isNotEmpty()) {
            val records = Records()
            records.weight = weight.toFloat()
            records.reps = reps.toInt()
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
            val formatted = current.format(formatter)
            records.EntryDate = formatted
            records.eid = EID
            if (applicationContext != null) {
                error = MainActivity.databaseHandler.addRecord(applicationContext, records)
            }

            etWeight?.text?.clear()
            etReps?.text?.clear()

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
                    "Something went wrong",
                    Toast.LENGTH_SHORT
            ).show()
        }
        viewRecords(EID)
    }
}
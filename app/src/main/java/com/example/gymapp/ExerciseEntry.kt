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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ExerciseEntry : Fragment() {

    private var navController : NavController?= null


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

    private fun viewRecords(){
        //TODO: implement this method
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
        viewRecords()
    }
}
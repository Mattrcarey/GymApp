package com.example.gymapp


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class WorkoutAdapter(mCtx : Context, val workouts : ArrayList<Workouts>) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    val mCtx = mCtx

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val txtWorkoutName = itemView.findViewById<TextView>(R.id.workoutName)
        val btnUpdate = itemView.findViewById<ImageView>(R.id.btnUpdate)
        val btnDelete = itemView.findViewById<ImageView>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutAdapter.ViewHolder {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_workouts,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: WorkoutAdapter.ViewHolder, position: Int) {
        val workout : Workouts = workouts[position]
        holder.txtWorkoutName.text = workout.workoutsName

    }

    override fun getItemCount(): Int {
        return workouts.size
    }

}


class ExerciseAdapter(mCtx : Context, val exercises : ArrayList<Exercises>) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    val mCtx = mCtx

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val txtExerciseName = itemView.findViewById<TextView>(R.id.exerciseName)
        val btnUpdate = itemView.findViewById<ImageView>(R.id.btnUpdate)
        val btnDelete = itemView.findViewById<ImageView>(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAdapter.ViewHolder {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_exercises,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.ViewHolder, position: Int) {
        val exercise : Exercises = exercises[position]
        holder.txtExerciseName.text = exercise.exerciseName

    }

    override fun getItemCount(): Int {
        return exercises.size
    }

}
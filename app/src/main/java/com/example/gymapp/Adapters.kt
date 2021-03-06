package com.example.gymapp


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


interface OnItemClickListener {
    fun onItemClick(position: Int)
}

class WorkoutAdapter(mCtx : Context, val workouts : ArrayList<Workouts>,
                     private val listener: OnItemClickListener) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    val mCtx = mCtx

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
        val txtWorkoutName = itemView.findViewById<TextView>(R.id.workoutName)
        val btnUpdate = itemView.findViewById<ImageView>(R.id.btnUpdate)
        val btnDelete = itemView.findViewById<ImageView>(R.id.btnDelete)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(adapterPosition)
            }
        }
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


class AddExerciseAdapter(mCtx : Context, val exercises : ArrayList<Exercises>,
                         private val listener: OnItemClickListener) : RecyclerView.Adapter<AddExerciseAdapter.ViewHolder>() {


    var ischecked : BooleanArray? = null
    val mCtx = mCtx
    var parent : ViewGroup? = null

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val txtExerciseName = itemView.findViewById<TextView>(R.id.exerciseName)
        val checked = itemView.findViewById<ImageView>(R.id.checked)

        init {
            itemView.setOnClickListener(this)
            ischecked = BooleanArray(itemCount)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                ischecked?.set(position, !ischecked!![position])
                listener.onItemClick(adapterPosition)
                checkbox(this,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddExerciseAdapter.ViewHolder {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_add_exercises,parent, false)
        this.parent = parent
        return ViewHolder(v)
    }

    fun checkbox (holder: ViewHolder, position: Int) {
        (parent as RecyclerView).layoutManager
        if (ischecked?.get(position) == true) {
            holder.checked.visibility = View.VISIBLE
        }
        else {
            holder.checked.visibility = View.GONE
        }
    }

    override fun onBindViewHolder(holder: AddExerciseAdapter.ViewHolder, position: Int) {
        val exercise : Exercises = exercises[position]
        holder.txtExerciseName.text = exercise.exerciseName
    }

    override fun getItemCount(): Int {
        return exercises.size
    }


}



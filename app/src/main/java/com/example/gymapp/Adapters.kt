package com.example.gymapp


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * OnItemClickListener : Simple interface used for recyclerView clicks
 */
interface OnItemClickListener {
    fun onItemClick(position: Int)
}

/**
 * WorkoutAdapter :
 */
class WorkoutAdapter(mCtx : Context, val workouts : ArrayList<Workouts>,
                     private val listener: OnItemClickListener) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>() {

    val mCtx = mCtx

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
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


class ExerciseAdapter(mCtx : Context, val exercises : ArrayList<Exercises>,
                      private val listener: OnItemClickListener) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    val mCtx = mCtx

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val txtExerciseName = itemView.findViewById<TextView>(R.id.exerciseName)
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


    val mCtx = mCtx
    var parent : ViewGroup? = null

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val txtExerciseName = itemView.findViewById<TextView>(R.id.exerciseName)
        val checked = itemView.findViewById<ImageView>(R.id.checked)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddExerciseAdapter.ViewHolder {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_add_exercises,parent, false)
        this.parent = parent
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AddExerciseAdapter.ViewHolder, position: Int) {
        val exercise : Exercises = exercises[position]
        holder.txtExerciseName.text = exercise.exerciseName
        if(exercise.isChecked){
            holder.checked.visibility = View.VISIBLE
        }
        else{
            holder.checked.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }
}


class RecordAdapter(mCtx : Context, val records : ArrayList<Records>) : RecyclerView.Adapter<RecordAdapter.ViewHolder>() {

    val mCtx = mCtx

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val txtDate = itemView.findViewById<TextView>(R.id.date)
        val txtWeight = itemView.findViewById<TextView>(R.id.weight)
        val txtReps = itemView.findViewById<TextView>(R.id.reps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordAdapter.ViewHolder {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_records,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecordAdapter.ViewHolder, position: Int) {
        val record : Records = records[position]
        holder.txtDate.text = record.EntryDate.substring(0,10)
        holder.txtWeight.text = record.weight.toString()
        holder.txtReps.text = record.reps.toString()

    }

    override fun getItemCount(): Int {
        return records.size
    }

}
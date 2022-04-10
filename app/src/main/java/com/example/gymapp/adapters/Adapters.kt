package com.example.gymapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymapp.R
import com.example.gymapp.models.Exercises
import com.example.gymapp.models.Records
import com.example.gymapp.models.Workouts
import com.example.gymapp.runDB.Run

/**
 * OnItemClickListener : Simple interface used for recyclerView clicks
 */
interface OnItemClickListener
{
    fun onItemClick(position: Int)
}

/**
 * WorkoutAdapter : adapter for displaying workouts
 */
class WorkoutAdapter (
    private val workouts : ArrayList<Workouts>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<WorkoutAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        val txtWorkoutName: TextView = itemView.findViewById(R.id.workoutName)
        // TODO: implement delete and update
//        val btnUpdate = itemView.findViewById<ImageView>(R.id.btnUpdate)
//        val btnDelete = itemView.findViewById<ImageView>(R.id.btnDelete)

        init
        {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?)
        {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
            {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutAdapter.ViewHolder
    {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_workouts, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: WorkoutAdapter.ViewHolder, position: Int)
    {
        val workout : Workouts = workouts[position]
        holder.txtWorkoutName.text = workout.workoutsName
    }

    override fun getItemCount(): Int
    {
        return workouts.size
    }
}


/**
 * ExerciseAdapter : adapter for displaying exercises
 */
class ExerciseAdapter(
    private val exercises : ArrayList<Exercises>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        val txtExerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        // TODO: implement delete and update
//        val btnUpdate = itemView.findViewById<ImageView>(R.id.btnUpdate)
//        val btnDelete = itemView.findViewById<ImageView>(R.id.btnDelete)

        init
        {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?)
        {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
            {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseAdapter.ViewHolder
    {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_exercises, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.ViewHolder, position: Int)
    {
        val exercise : Exercises = exercises[position]
        holder.txtExerciseName.text = exercise.exerciseName
    }

    override fun getItemCount(): Int
    {
        return exercises.size
    }
}


/**
 * AddExerciseAdapter : adapter for displaying exercises to add to a workout
 */
class AddExerciseAdapter(
    private val exercises : ArrayList<Exercises>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<AddExerciseAdapter.ViewHolder>()
{
    var parent : ViewGroup? = null

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        val txtExerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        val checked: ImageView = itemView.findViewById(R.id.checked)

        init
        {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?)
        {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
            {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddExerciseAdapter.ViewHolder
    {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_add_exercises, parent, false)
        this.parent = parent
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: AddExerciseAdapter.ViewHolder, position: Int)
    {
        val exercise : Exercises = exercises[position]
        holder.txtExerciseName.text = exercise.exerciseName
        if(exercise.isChecked)
        {
            holder.checked.visibility = View.VISIBLE
        }
        else
        {
            holder.checked.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int
    {
        return exercises.size
    }
}


/**
 * RecordAdapter : adapter for displaying exercise entries
 */
class RecordAdapter(private val records : ArrayList<Records>) : RecyclerView.Adapter<RecordAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val txtDate: TextView = itemView.findViewById(R.id.date)
        val txtWeight: TextView = itemView.findViewById(R.id.weight)
        val txtReps: TextView = itemView.findViewById(R.id.reps)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordAdapter.ViewHolder
    {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_records,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecordAdapter.ViewHolder, position: Int)
    {
        val record : Records = records[position]
        holder.txtDate.text = record.entryDate.substring(0,10)
        holder.txtWeight.text = record.weight.toString()
        holder.txtReps.text = record.reps.toString()
    }

    override fun getItemCount(): Int
    {
        return records.size
    }
}


/**
 * RunAdapter : adapter for displaying run's
 */
class RunAdapter(
    private val runs : ArrayList<Run>,
    private val listener: OnItemClickListener
    ) : RecyclerView.Adapter<RunAdapter.ViewHolder>()
{
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener
    {
        val milesRan: TextView = itemView.findViewById(R.id.milesRan)
        val avgMiles: TextView = itemView.findViewById(R.id.avgMiles)
        val timeRan: TextView = itemView.findViewById(R.id.timeRan)
        val runImage: ImageView = itemView.findViewById(R.id.runImage)

        init
        {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?)
        {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION)
            {
                listener.onItemClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
    {
        val v : View = LayoutInflater.from(parent.context).inflate(R.layout.lo_runs,parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RunAdapter.ViewHolder, position: Int)
    {
        val run : Run = runs[position]
        holder.milesRan.text = String.format("%.2f",run.distance)
        holder.avgMiles.text = String.format("%.2f",run.avg_speed)
        val milliseconds : Long = run.run_time
        val hours : Int = kotlin.math.floor((milliseconds / 3600000).toDouble()).toInt()
        val minutes : Int = kotlin.math.floor(((milliseconds % 3600000) / 60000).toDouble()).toInt()
        val seconds : Int = kotlin.math.floor((((milliseconds % 3600000) % 60000) / 1000).toDouble()).toInt()
        val runTime = "$hours:$minutes:$seconds"
        holder.timeRan.text = runTime
        holder.runImage.setImageBitmap(run.image)
    }

    override fun getItemCount(): Int
    {
        return runs.size
    }
}
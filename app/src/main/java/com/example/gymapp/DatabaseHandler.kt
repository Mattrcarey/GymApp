package com.example.gymapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

//creating the database logic, extending the SQLiteOpenHelper base class
class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "Database"

        private const val TABLE_WORKOUTS = "Workouts"
        private const val COLUMN_WORKOUTID = "workoutid"
        private const val COLUMN_WORKOUTNAME = "workoutname"
        private const val CREATE_WORKOUTTABLE = "CREATE TABLE $TABLE_WORKOUTS ( $COLUMN_WORKOUTID" +
                " INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_WORKOUTNAME TEXT)"
        private const val DELETE_WORKOUTENTRIES = "DROP TABLE IF EXISTS $TABLE_WORKOUTS"

        private const val TABLE_EXERCISES = "Exercises"
        private const val COLUMN_EXERCISEID = "exerciseid"
        private const val COLUMN_EXERCISENAME = "exercisename"
        private const val CREATE_EXERCISETABLE = "CREATE TABLE $TABLE_EXERCISES ( $COLUMN_EXERCISEID" +
                " INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_EXERCISENAME TEXT)"
        private const val DELETE_EXERCISEENTRIES = "DROP TABLE IF EXISTS $TABLE_EXERCISES"

        private const val TABLE_LINKINGTABLE = "Linker"
        private const val CREATE_LINKINGTABLE = "CREATE TABLE $TABLE_LINKINGTABLE ( $COLUMN_WORKOUTID" +
                " INTEGER, $COLUMN_EXERCISEID INTEGER, PRIMARY KEY ($COLUMN_WORKOUTID, $COLUMN_EXERCISEID)" +
                ", FOREIGN KEY ($COLUMN_WORKOUTID) REFERENCES $TABLE_WORKOUTS ON UPDATE CASCADE ON DELETE CASCADE" +
                ", FOREIGN KEY ($COLUMN_EXERCISEID) REFERENCES $TABLE_EXERCISES ON UPDATE CASCADE ON DELETE CASCADE)"
        private const val DELETE_LINKINGENTRIES = "DROP TABLE IF EXISTS $TABLE_LINKINGTABLE"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(CREATE_WORKOUTTABLE)
        db!!.execSQL(CREATE_EXERCISETABLE)
        db!!.execSQL(CREATE_LINKINGTABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(DELETE_WORKOUTENTRIES)
        db!!.execSQL(DELETE_EXERCISEENTRIES)
        db!!.execSQL(DELETE_LINKINGENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun addWorkout(mCtx: Context, workouts: Workouts) {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_WORKOUTNAME, workouts.workoutsName)

        val success = db.insert(TABLE_WORKOUTS, null, contentValues)

        db.close()
    }

    fun addExercise(mCtx: Context, exercises: Exercises) {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_EXERCISENAME, exercises.exerciseName)

        val success = db.insert(TABLE_EXERCISES, null, contentValues)

        db.close()
    }

    fun addLink(mCtx: Context, links: Links){
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(COLUMN_WORKOUTID, links.wid)
        contentValues.put(COLUMN_EXERCISEID, links.eid)

        val success = db.insert(TABLE_LINKINGTABLE, null, contentValues)

        db.close()
    }

    fun getWorkouts(mCtx : Context) : ArrayList<Workouts>{
        val qry = "SELECT * FROM $TABLE_WORKOUTS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val workouts = ArrayList<Workouts>()

        if (cursor.count == 0)
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show() else
        {while (cursor.moveToNext()){
            val workout = Workouts()
            workout.workoutsID = cursor.getInt(cursor.getColumnIndex(COLUMN_WORKOUTID))
            workout.workoutsName = cursor.getString(cursor.getColumnIndex(COLUMN_WORKOUTNAME))
            workouts.add(workout)
        }
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return workouts
    }

    fun getExercises(mCtx : Context) : ArrayList<Exercises> {
        val qry = "SELECT * FROM $TABLE_EXERCISES"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val exercises = ArrayList<Exercises>()

        if (cursor.count == 0)
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show() else
        {while (cursor.moveToNext()){
            val exercise = Exercises()
            exercise.exerciseID = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISEID))
            exercise.exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISENAME))
            exercises.add(exercise)
        }
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return exercises
    }

    fun getExercises(mCtx: Context, WID : Int) : ArrayList<Exercises> {
        //TODO sql statement to select all from linking table with workoutID = WID then get all exercises
        val qry = "SELECT $TABLE_EXERCISES.$COLUMN_EXERCISEID, $COLUMN_EXERCISENAME FROM $TABLE_EXERCISES, $TABLE_LINKINGTABLE "+
                "WHERE $TABLE_LINKINGTABLE.$COLUMN_WORKOUTID = $WID " +
                "AND $TABLE_LINKINGTABLE.$COLUMN_EXERCISEID = $TABLE_EXERCISES.$COLUMN_EXERCISEID"
        val db = this.readableDatabase
        val cursor = db.rawQuery(qry, null)
        val exercises = ArrayList<Exercises>()

        if (cursor.count == 0)
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show() else
        {while (cursor.moveToNext()){
            val exercise = Exercises()
            exercise.exerciseID = cursor.getInt(cursor.getColumnIndex(COLUMN_EXERCISEID))
            exercise.exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_EXERCISENAME))
            exercises.add(exercise)
        }
            Toast.makeText(mCtx, "${cursor.count.toString()} Records Found", Toast.LENGTH_SHORT).show()
        }
        cursor.close()
        db.close()
        return exercises
    }

}
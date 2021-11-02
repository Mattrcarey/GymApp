package com.example.gymapp.runDB

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface RunDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRun(run: Run)

    @Delete
    fun deleteRun(run: Run)

    @Query("SELECT * FROM runs ORDER BY id DESC")
    fun getRuns(): List<Run> //ArrayList<Run>//

    @Query("SELECT SUM(distance) FROM runs")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT SUM(run_time) FROM runs")
    fun getTotalRunTime(): LiveData<Int>


}
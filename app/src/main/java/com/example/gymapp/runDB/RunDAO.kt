package com.example.gymapp.runDB

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface RunDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM runs ORDER BY id DESC")
    fun getRuns(): LiveData<List<Run>>

    @Query("SELECT SUM(distance) FROM runs")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT SUM(run_time) FROM runs")
    fun getTotalRunTime(): LiveData<Int>

    @Query("SELECT SUM(calories) FROM runs")
    fun getTotalCalories(): LiveData<Int>

}
package com.example.gymapp.runDB

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class Run (
    @PrimaryKey (autoGenerate = true)
    var id: Int? = null,
    var image: Bitmap? = null,
    var run_date: String = "",
    var distance: Int = 0,
    var run_time: Int = 0,
    var calories: Int = 0,
)
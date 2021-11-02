package com.example.gymapp.runDB

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "runs")
data class Run(
    var image: Bitmap? = null,
    var run_date: Long = 0L,
    var avg_speed: Float = 0f,
    var distance: Float = 0f,
    var run_time: Long = 0L
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
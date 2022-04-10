package com.example.gymapp.runDB

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    version = 1,
    entities = [Run::class] ,
)

@TypeConverters(Converter::class)
abstract class RunDatabase : RoomDatabase() {
    abstract fun getRunDao(): RunDAO
}
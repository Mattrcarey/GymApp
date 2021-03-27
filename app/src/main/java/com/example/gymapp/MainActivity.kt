package com.example.gymapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.EditText
import android.widget.LinearLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gymapp.runDB.RunDAO
import com.example.gymapp.runDB.RunDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var databaseHandler: DatabaseHandler
        lateinit var runningDB: RunDatabase
        lateinit var runDao: RunDAO
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHandler = DatabaseHandler(this)

        runningDB = Room.databaseBuilder(applicationContext, RunDatabase::class.java, "runs").build()
        runDao = runningDB.getRunDao()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.workoutFragment,
            R.id.exerciseFragment, R.id.runFragment))

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }
}
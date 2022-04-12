package com.example.gymapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.gymapp.exerciseDB.DatabaseHandler
import com.example.gymapp.runDB.RunDAO
import com.example.gymapp.runDB.RunDatabase
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var databaseHandler: DatabaseHandler
        lateinit var runningDB: RunDatabase
        lateinit var runDao: RunDAO
    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigateToRun(intent)

        databaseHandler = DatabaseHandler(this)

        runningDB = Room.databaseBuilder(applicationContext, RunDatabase::class.java, "runs")
            .allowMainThreadQueries().build()
        runDao = runningDB.getRunDao()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navController = findNavController(R.id.fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.workoutFragment,
                R.id.exerciseFragment, R.id.runFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToRun(intent)
    }

    private fun navigateToRun(intent: Intent?) {
        if (intent?.action == "ShowTrackingFrag") {
            navController.navigate(R.id.action_global_to_startedRunFragment)
        }
    }
}
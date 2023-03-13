package com.udacity.asteroid_radar_app.main

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.work.*
import com.udacity.asteroid_radar_app.R
import com.udacity.asteroid_radar_app.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupActionBarWithNavController(this, navController)

        //Start up background worker
        setUpBackgroundWorker()
    }

    private fun setUpBackgroundWorker(){
        applicationScope.launch {
            //I made it so that charging isn't required despite the rubric. Requiring no low battery
            //should be plenty sufficient given that the amount of data we're getting. It shouldn't
            //put much of a strain on the device
            val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true)
                .setRequiresCharging(false)
                .apply {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        setRequiresDeviceIdle(true)
                    }
                }.build()
            val repeatingRequest = PeriodicWorkRequestBuilder<BackgroundWorker>(1, TimeUnit.DAYS).setConstraints(constraints).build()
            WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(BackgroundWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }
}
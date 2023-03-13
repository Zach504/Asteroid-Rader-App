package com.udacity.asteroid_radar_app.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroid_radar_app.data.AsteroidDatabaseDao

class MainViewModelFactory(private val database: AsteroidDatabaseDao, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(database, application) as T
        }
        throw java.lang.IllegalArgumentException("Unknown ViewModel class")
    }
}
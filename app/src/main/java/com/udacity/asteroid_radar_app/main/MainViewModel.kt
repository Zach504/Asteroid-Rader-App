package com.udacity.asteroid_radar_app.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroid_radar_app.data.Asteroid
import com.udacity.asteroid_radar_app.data.AsteroidDatabaseDao
import com.udacity.asteroid_radar_app.data.PictureOfDay
import com.udacity.asteroid_radar_app.network.NASAAsteroidApi
import com.udacity.asteroid_radar_app.network.NASAPODApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(val database: AsteroidDatabaseDao, application: Application) : AndroidViewModel(application){

    private val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

    var asteroids = database.getAllAsteroids(getDay())

    fun showAllAsteroids(){
        viewModelScope.launch {
            asteroids = database.getAllAsteroids(getDay())
       }
    }

    fun showWeeksAsteroids(){
        viewModelScope.launch {
            asteroids = database.getAsteroidsForDays(getWeek())
        }
    }

    fun showDaysAsteroids(){
        viewModelScope.launch {
            asteroids = database.getAsteroidsForDay(getDay())
        }
    }

    private var _pictureOfDay = MutableLiveData<PictureOfDay>()

    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _selectedAsteroid = MutableLiveData<Asteroid?>()

    val selectedAsteroid: LiveData<Asteroid?>
        get() = _selectedAsteroid

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _selectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _selectedAsteroid.value = null
    }

    init {
        //Picture of the day coroutine
        getPictureOfDay()
    }

    //Util Functions
    private fun getDay(): String{
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        return dateFormat.format(currentTime).toString()
    }
    private fun getWeek(): List<String>{
        val week = mutableListOf<String>()
        val calendar = Calendar.getInstance()
        var currentTime = calendar.time
        week.add(dateFormat.format(currentTime).toString())
        repeat(6) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            var currentTime = calendar.time
            week.add(dateFormat.format(currentTime).toString())
        }
        return week
    }

    //Network Calls
    private fun getPictureOfDay(){
        viewModelScope.launch {
            try {
                _pictureOfDay.value = NASAPODApi.retrofitService.getPictureOfDay()
            } catch (e: Exception) {
                //TODO: Handle Failure}
            }
        }
    }
}
package com.udacity.asteroid_radar_app.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroid_radar_app.data.Asteroid

class DetailViewModel(asteroid: Asteroid): ViewModel() {

    private val _asteroid = MutableLiveData<Asteroid>()

    val asteroid: LiveData<Asteroid>
        get() = _asteroid

    init {
        _asteroid.value = asteroid
    }
}
package com.udacity.asteroid_radar_app.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDatabaseDao {

    //Insert New Asteroids
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroids: List<Asteroid>)

    //Get All Asteroids
    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate >= :day ORDER BY closeApproachDate, codename DESC")
    fun getAllAsteroids(day: String) : LiveData<List<Asteroid>>

    //Get Asteroids for Day
    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate = :day ORDER BY codename DESC")
    fun getAsteroidsForDay(day: String) : LiveData<List<Asteroid>>

    //Get Asteroids for Days
    @Query("SELECT * FROM asteroid_table WHERE closeApproachDate in (:days) ORDER BY closeApproachDate, codename DESC")
    fun getAsteroidsForDays(days: List<String>) : LiveData<List<Asteroid>>

    //Clear Unnecessary Asteroids
    @Query("DELETE FROM asteroid_table WHERE closeApproachDate < :day")
    suspend fun cleanAsteroids(day: String)

    //Clear All Asteroids
    @Query("DELETE FROM asteroid_table")
    suspend fun clearAsteroids()
}
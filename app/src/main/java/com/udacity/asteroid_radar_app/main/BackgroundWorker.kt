package com.udacity.asteroid_radar_app.main

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroid_radar_app.data.AsteroidDatabase
import com.udacity.asteroid_radar_app.network.NASAAsteroidApi
import java.text.SimpleDateFormat
import java.util.*

class BackgroundWorker(appContext: Context, params: WorkerParameters): CoroutineWorker(appContext,
    params
) {
    companion object {
        const val WORK_NAME = "BackgroundWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getInstance(applicationContext)
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val currentDay = dateFormat.format(currentTime).toString()
        //Remove asteroids from the database older than today
        database.asteroidDatabaseDao.cleanAsteroids(currentDay)

        //Query the NASA API for new Asteroids
        //Get the next seven days of NEOs
        try {
            //Get first 7 days
            var asteroids = NASAAsteroidApi.parseAsteroidsJsonResult(
                NASAAsteroidApi.retrofitService.getAsteroids(currentDay)
            )
            database.asteroidDatabaseDao.insert(asteroids)

            //Get subsequent 7 days
            calendar.add(Calendar.DAY_OF_YEAR, 7)
            val futureStartTime = calendar.time
            val futureStartDay = dateFormat.format(futureStartTime).toString()
            asteroids = NASAAsteroidApi.parseAsteroidsJsonResult(
                NASAAsteroidApi.retrofitService.getAsteroids(futureStartDay)
            )
            database.asteroidDatabaseDao.insert(asteroids)
        } catch (e: Exception) {
            return Result.retry()
        }
        return Result.success()
    }

}
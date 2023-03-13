package com.udacity.asteroid_radar_app.network

import com.udacity.asteroid_radar_app.data.Asteroid
import com.udacity.asteroid_radar_app.main.Constants
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*
import kotlin.collections.ArrayList

private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()).baseUrl(Constants.BASE_URL).build()

interface NASAAsteroidApiService {
    @GET("neo/rest/v1/feed?api_key=${Constants.API_KEY}")
    suspend fun getAsteroids(@Query("start_date") startDate: String) : String
}

object NASAAsteroidApi {
    val retrofitService: NASAAsteroidApiService by lazy { retrofit.create(NASAAsteroidApiService::class.java) }

    fun parseAsteroidsJsonResult(jsonString: String): ArrayList<Asteroid> {
        val jsonResult = JSONObject(jsonString)
        val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

        val asteroidList = ArrayList<Asteroid>()

        var keys = nearEarthObjectsJson.keys()
        while(keys.hasNext()) {
            val date = keys.next()
            val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(date)

            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("kilometers").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson
                    .getJSONArray("close_approach_data").getJSONObject(0)
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("kilometers_per_second")
                val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("astronomical")
                val isPotentiallyHazardous = asteroidJson
                    .getBoolean("is_potentially_hazardous_asteroid")

                val asteroid = Asteroid(
                    id, codename, date, absoluteMagnitude,
                    estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous
                )
                asteroidList.add(asteroid)
            }
        }

        return asteroidList
    }
}
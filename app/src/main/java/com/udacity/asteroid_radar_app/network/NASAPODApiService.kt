package com.udacity.asteroid_radar_app.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroid_radar_app.data.PictureOfDay
import com.udacity.asteroid_radar_app.main.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

//Moshi JSON Parser Setup
private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

//Construct URL for Fetching Photo of the Day
private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi)).baseUrl(
    Constants.BASE_URL).build()

interface NASAPODApiService {
    @GET("planetary/apod?api_key=${Constants.API_KEY}")
    suspend fun getPictureOfDay(): PictureOfDay
}

object NASAPODApi {
    val retrofitService: NASAPODApiService by lazy { retrofit.create(NASAPODApiService::class.java) }
}

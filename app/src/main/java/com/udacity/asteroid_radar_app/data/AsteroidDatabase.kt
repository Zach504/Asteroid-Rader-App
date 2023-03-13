package com.udacity.asteroid_radar_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    //Connect Database to DAO
    abstract val asteroidDatabaseDao: AsteroidDatabaseDao

    //Asteroid Database Singleton Creation
    companion object {
        @Volatile
        private var INSTANCE: AsteroidDatabase? = null

        fun getInstance(context: Context) : AsteroidDatabase {
            synchronized(this) {
                var instance = INSTANCE
                //If null then database needs to be created still
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext, AsteroidDatabase::class.java, "asteroid_database").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}
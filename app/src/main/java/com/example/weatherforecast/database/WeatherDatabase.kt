package com.example.weatherforecast.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weatherforecast.utils.Constants


//@Database(entities = [], version = 1)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDAO

    companion object {

        @Volatile
        private var instanceOfDb: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return instanceOfDb ?: synchronized(this) {
                val temp: WeatherDatabase = Room.databaseBuilder(
                    context, WeatherDatabase::class.java,
                    Constants.WEATHER_DATABASE
                )
                    .fallbackToDestructiveMigration()
                    .build()
                instanceOfDb = temp
                temp
            }
        }
    }
}
package com.example.weatherforecast.network

import com.example.weatherforecast.utils.Constants
import com.example.weatherforecast.model.CurrentWeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getCurrentWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("lang") lang: String,
        @Query("units") units: String = Constants.METRIC_UNIT, // If you do not use the units parameter, standard units will be applied by default
        @Query("appid") appid: String = Constants.APP_ID
    ): CurrentWeatherResponse
}

object RetrofitHelper {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    private val retrofitInstance = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val weatherService: WeatherService = retrofitInstance.create(WeatherService::class.java)
}

package com.example.weatherforecast.network

import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.model.NextFiveDaysWeatherResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        lang: String = "en"
    ): Flow<CurrentWeatherResponse>

    suspend fun getNextFiveDaysWeatherData(
        lat: String,
        lon: String,
        lang: String = "en"
    ): Flow<NextFiveDaysWeatherResponse>
}
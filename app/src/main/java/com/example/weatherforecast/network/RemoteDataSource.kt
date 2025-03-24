package com.example.weatherforecast.network

import com.example.weatherforecast.model.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        lang: String = "en"
    ): CurrentWeatherResponse
}
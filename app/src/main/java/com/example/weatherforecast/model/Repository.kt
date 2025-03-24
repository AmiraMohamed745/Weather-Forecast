package com.example.weatherforecast.model

import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun fetchCurrentWeatherData(
        lat: String,
        lon: String,
        lang: String = "en"
    ): CurrentWeatherResponse

}
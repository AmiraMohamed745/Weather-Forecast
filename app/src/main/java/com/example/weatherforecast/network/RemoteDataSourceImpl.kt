package com.example.weatherforecast.network

import com.example.weatherforecast.model.CurrentWeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RemoteDataSourceImpl(private val service: WeatherService): RemoteDataSource {

    override suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        lang: String
    ): CurrentWeatherResponse {
        return service.getCurrentWeatherData(lat, lon, lang)
    }
}
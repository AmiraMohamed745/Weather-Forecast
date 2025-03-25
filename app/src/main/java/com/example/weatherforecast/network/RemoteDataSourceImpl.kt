package com.example.weatherforecast.network

import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.model.NextFiveDaysWeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class RemoteDataSourceImpl(private val service: WeatherService): RemoteDataSource {

    override suspend fun getCurrentWeatherData(
        lat: String,
        lon: String,
        lang: String
    ): Flow<CurrentWeatherResponse> {
        return flowOf(service.getCurrentWeatherData(lat, lon, lang))
    }

    override suspend fun getNextFiveDaysWeatherData(
        lat: String,
        lon: String,
        lang: String
    ): Flow<NextFiveDaysWeatherResponse> {
        return flowOf(service.getNextFiveDaysWeatherData(lat, lon, lang))
    }
}
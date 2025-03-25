package com.example.weatherforecast.model

import com.example.weatherforecast.database.LocalDataSource
import com.example.weatherforecast.network.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class RepositoryImpl private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource? = null
) : Repository {

    companion object {
        private var repositoryImplInstance: RepositoryImpl? = null
        fun getInstance(
            remoteDataSource: RemoteDataSource,
            localDataSource: LocalDataSource
        ): RepositoryImpl {
            return repositoryImplInstance ?: synchronized(this) {
                val temp = RepositoryImpl(remoteDataSource, localDataSource)
                repositoryImplInstance = temp
                temp
            }
        }
    }

    override suspend fun fetchCurrentWeatherData(
        lat: String,
        lon: String,
        lang: String
    ): Flow<CurrentWeatherResponse> {
        return remoteDataSource.getCurrentWeatherData(lat, lon, lang)
    }

    override suspend fun fetchNextFiveDaysWeatherData(
        lat: String,
        lon: String,
        lang: String
    ): Flow<NextFiveDaysWeatherResponse> {
        return remoteDataSource.getNextFiveDaysWeatherData(lat, lon, lang)
    }
}
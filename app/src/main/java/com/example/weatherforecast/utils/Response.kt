package com.example.weatherforecast.utils

import com.example.weatherforecast.model.CurrentWeatherResponse

/*
sealed class Response<> {

    data object Loading : Response()
    data class Success(val data: CurrentWeatherResponse) : Response() // I need this ti be generic
    data class Failure(val error: Throwable) : Response()

}*/

sealed class Response<out T: Any> {
    data object Loading : Response<Nothing>()
    data class Success<out T: Any>(val data: T) : Response<T>()
    data class Failure(val error: Throwable) : Response<Nothing>()
}

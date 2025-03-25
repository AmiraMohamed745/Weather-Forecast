package com.example.weatherforecast.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.model.NextFiveDaysWeatherResponse
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.utils.Response
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class NextFiveDaysWeatherViewModel(private val repository: Repository) : ViewModel() {

    private val _nextFiveDaysWeatherState =
        MutableStateFlow<Response<NextFiveDaysWeatherResponse>>(Response.Loading)
    val nextFiveDaysWeatherState = _nextFiveDaysWeatherState.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    fun getNextFiveDaysWeatherData(lat: String, lon: String, lang: String = "en") {
        viewModelScope.launch {
            try {
                val response = repository.fetchNextFiveDaysWeatherData(lat, lon, lang)
                response
                    .catch { exception ->
                        _nextFiveDaysWeatherState.value = Response.Failure(exception)
                        _message.emit("Failed to load weather data. Please try again later.")
                        Log.d("CurrentWeatherViewModel: coroutine", "Error: ${exception.message}")
                    }
                    .collect {
                        _nextFiveDaysWeatherState.value = Response.Success(it)
                    }

            } catch (exception: Exception) {
                _nextFiveDaysWeatherState.value = Response.Failure(exception)
                _message.emit("Failed to load weather data. Please try again later.")
                Log.d("CurrentWeatherViewModel", "Error: ${exception.message}")
            }

        }
    }
}


@Suppress("UNCHECKED_CAST")
class NextFiveDaysWeatherFactory(private val _repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NextFiveDaysWeatherViewModel::class.java)) {
            NextFiveDaysWeatherViewModel(_repository) as T
        } else {
            throw IllegalArgumentException("View class not found.")
        }
    }
}
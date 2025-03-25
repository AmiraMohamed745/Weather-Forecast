package com.example.weatherforecast.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.model.Repository
import com.example.weatherforecast.utils.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*class CurrentWeatherViewModel(private val repository: Repository) : ViewModel() {

    private val _currentWeatherState: MutableLiveData<CurrentWeatherResponse> = MutableLiveData()
    val currentWeatherState = _currentWeatherState

    fun getCurrentWeatherData(lat: String, lon: String, lang: String = "en") {
        viewModelScope.launch(Dispatchers.IO) { // consider removing Dispatchers.IO
            try {
                val response = repository.fetchCurrentWeatherData(lat, lon, lang)
                //currentWeatherState.value = response
                withContext(Dispatchers.Main) {
                    _currentWeatherState.value = response
                }
            } catch (ex: Exception) {

            }

        }
    }
}*/

class CurrentWeatherViewModel(private val repository: Repository) : ViewModel() {

    private val _currentWeatherState = MutableStateFlow<Response<CurrentWeatherResponse>>(Response.Loading)
    val currentWeatherState = _currentWeatherState.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    fun getCurrentWeatherData(lat: String, lon: String, lang: String = "en") {
        viewModelScope.launch {
            try {
                val response = repository.fetchCurrentWeatherData(lat, lon, lang)
                response
                    .catch { exception ->
                        _currentWeatherState.value = Response.Failure(exception)
                        _message.emit("Failed to load weather data. Please try again later.")
                        Log.d("CurrentWeatherViewModel: coroutine", "Error: ${exception.message}")
                    }
                    .collect {
                        _currentWeatherState.value = Response.Success(it)
                    }

            } catch (exception: Exception) {
                _currentWeatherState.value = Response.Failure(exception)
                _message.emit("Failed to load weather data. Please try again later.")
                Log.d("CurrentWeatherViewModel", "Error: ${exception.message}")
            }

        }
    }
}

@Suppress("UNCHECKED_CAST")
class CurrentWeatherFactory(private val _repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)) {
            CurrentWeatherViewModel(_repository) as T
        } else {
            throw IllegalArgumentException("View class not found.")
        }
    }
}
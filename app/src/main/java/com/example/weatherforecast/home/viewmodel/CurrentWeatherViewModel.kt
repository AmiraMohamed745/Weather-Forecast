package com.example.weatherforecast.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrentWeatherViewModel(private val repository: Repository) : ViewModel() {

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
}

@Suppress("UNCHECKED_CAST")
class CurrentWeatherFactory(private val _repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CurrentWeatherViewModel::class.java)) {
            CurrentWeatherViewModel(_repository) as T
        } else {
            throw IllegalArgumentException("View class not found.")
        }
    }
}
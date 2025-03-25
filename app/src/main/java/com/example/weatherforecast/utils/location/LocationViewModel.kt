package com.example.weatherforecast.utils.location

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/*
class LocationViewModel: ViewModel() {

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation = _currentLocation

    fun setCurrentLocation(location: Location?) {
        _currentLocation.value = location
    }
}*/

class LocationViewModel: ViewModel() {

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation = _currentLocation.asStateFlow()

    fun setCurrentLocation(location: Location?) {
        _currentLocation.value = location
    }
}


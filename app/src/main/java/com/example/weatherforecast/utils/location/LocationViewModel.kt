package com.example.weatherforecast.utils.location

import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationViewModel: ViewModel() {

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation = _currentLocation

    fun setCurrentLocation(location: Location?) {
        _currentLocation.value = location
    }


}
package com.example.weatherforecast.navigation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherforecast.navigation.NavigationRouter

class NavigationViewModel: ViewModel() {

    private val _currentScreen = MutableLiveData<NavigationRouter>(NavigationRouter.HomeScreen)
    val currentScreen: LiveData<NavigationRouter> = _currentScreen

    fun setCurrentScreen(screen: NavigationRouter) {
        _currentScreen.value = screen
    }
}
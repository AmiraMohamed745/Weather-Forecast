package com.example.weatherforecast.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRouter {

    @Serializable
    object HomeScreen: NavigationRouter()

    @Serializable
    object FavoriteScreen: NavigationRouter()

    @Serializable
    object AlertsScreen: NavigationRouter()

    @Serializable
    object SettingAlertScreen: NavigationRouter()

    @Serializable
    object SettingsScreen: NavigationRouter()

    @Serializable
    object WeatherDetailsScreen: NavigationRouter()
}
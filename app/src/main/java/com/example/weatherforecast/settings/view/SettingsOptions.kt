package com.example.weatherforecast.settings.view

enum class SettingsOptions(val displayName: String, val radioOptions: List<String>) {
    LANGUAGE("Language", listOf("English", "Arabic")),
    LOCATION("Location", listOf("GPS", "Map")),
    WIND_SPEED_UNIT("Wind Speed Unit", listOf("Meter/Sec", "Mile/Hour")),
    TEMPERATURE_UNIT("Temperature Unit", listOf("Celsius °C", "Kelvin °K", "Fahrenheit °F"))

}
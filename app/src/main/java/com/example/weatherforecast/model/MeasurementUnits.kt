package com.example.weatherforecast.model

enum class MeasurementUnits(val unitParameter: String) {
    KELVIN("standard"),
    CELSIUS("metric"),
    FAHRENHEIT("imperial")
}
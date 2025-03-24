package com.example.weatherforecast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.weatherforecast.details.view.WeatherDetailsScreenForAlerts
import com.example.weatherforecast.navigation.viewmodel.NavigationViewModel
import com.example.weatherforecast.ui.theme.WeatherForecastTheme

class AlertDetailsScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val alertTitle = intent.getStringExtra("ALERT_TITLE") ?: "No title"
        val alertLocation = intent.getStringExtra("ALERT_LOCATION") ?: "Unknown location"
        val alertCity = intent.getStringExtra("ALERT_CITY") ?: "Unknown city"
        val alertDescription = intent.getStringExtra("ALERT_DESCRIPTION") ?: "No description"
        val alertDate = intent.getStringExtra("ALERT_DATE") ?: "Unknown date"
        val alertSunriseTime = intent.getStringExtra("ALERT_SUNRISE") ?: "Unknown sunrise"
        val alertSunsetTime = intent.getStringExtra("ALERT_SUNSET") ?: "Unknown sunset"
        val alertTemperature = intent.getStringExtra("ALERT_TEMPERATURE") ?: "Unknown temperature"
        val alertFeelsLike = intent.getStringExtra("ALERT_FEELS_LIKE") ?: "Unknown description"

        setContent {
            WeatherForecastTheme {
                val navigationViewModel = NavigationViewModel()
                WeatherDetailsScreenForAlerts(
                    alertTitle = alertTitle,
                    navigationViewModel = navigationViewModel
                )
            }
        }
    }
}


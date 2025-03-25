package com.example.weatherforecast

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.weatherforecast.network.RetrofitHelper
import com.example.weatherforecast.ui.theme.WeatherForecastTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherforecast.database.LocalDataSourceImpl
import com.example.weatherforecast.utils.location.LocationHelper
import com.example.weatherforecast.home.view.LocationPermissionAlertDialog
import com.example.weatherforecast.home.viewmodel.CurrentWeatherFactory
import com.example.weatherforecast.home.viewmodel.CurrentWeatherViewModel
import com.example.weatherforecast.home.viewmodel.NextFiveDaysWeatherFactory
import com.example.weatherforecast.home.viewmodel.NextFiveDaysWeatherViewModel
import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.model.RepositoryImpl
import com.example.weatherforecast.navigation.viewmodel.NavigationViewModel
import com.example.weatherforecast.network.RemoteDataSourceImpl
import com.example.weatherforecast.ui.theme.composables.AppScaffold
import com.example.weatherforecast.utils.Constants
import com.example.weatherforecast.utils.location.LocationViewModel
import org.osmdroid.config.Configuration.*

class MainActivity : ComponentActivity() {

    private lateinit var locationHelper: LocationHelper
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel
    private lateinit var nextFiveDaysWeatherViewModel: NextFiveDaysWeatherViewModel


    private var showLocationPermissionAlertDialog = mutableStateOf(false)

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        // For OSM library
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        locationHelper = LocationHelper(this)

        setContent {
            WeatherForecastTheme {
                locationViewModel = viewModel()
                currentWeatherViewModel =
                    viewModel(
                        factory =
                            CurrentWeatherFactory(
                                RepositoryImpl.getInstance(
                                    RemoteDataSourceImpl(RetrofitHelper.weatherService),
                                    LocalDataSourceImpl()
                                )
                            )
                    )
                nextFiveDaysWeatherViewModel = viewModel(
                    factory =
                        NextFiveDaysWeatherFactory(
                            RepositoryImpl.getInstance(
                                RemoteDataSourceImpl(RetrofitHelper.weatherService),
                                LocalDataSourceImpl()
                            )
                        )
                )

                val nextFiveDaysWeatherViewModel: NextFiveDaysWeatherViewModel = viewModel(
                    factory = NextFiveDaysWeatherFactory(
                        RepositoryImpl.getInstance(
                            RemoteDataSourceImpl(RetrofitHelper.weatherService),
                            LocalDataSourceImpl()
                        )
                    )
                )
                val navigationViewModel: NavigationViewModel = viewModel()
                Surface(color = Color.White) {}
                if (showLocationPermissionAlertDialog.value) {
                    LocationPermissionAlertDialog(
                        onDismissRequest = { showLocationPermissionAlertDialog.value = false },
                        onConfirmation = {
                            enableLocationServices()
                            showLocationPermissionAlertDialog.value = false
                        }
                    )
                }
                AppScaffold(
                    locationViewModel = locationViewModel,
                    currentWeatherViewModel = currentWeatherViewModel,
                    nextFiveDaysWeatherViewModel = nextFiveDaysWeatherViewModel,
                    navigationViewModel = navigationViewModel
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermissions()) {
            if (locationHelper.isLocationEnabled()) {
                getFreshLocationAndFetchWeather()
            } else {
                showLocationPermissionAlertDialog.value = true
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION),
                Constants.REQUEST_LOCATION_CODE
            )
        }
    }

    private fun getFreshLocationAndFetchWeather() {
        locationHelper.getFreshLocation { location ->
            locationViewModel.setCurrentLocation(location)
            currentWeatherViewModel.getCurrentWeatherData(
                location.latitude.toString(),
                location.longitude.toString()
            )
            nextFiveDaysWeatherViewModel.getNextFiveDaysWeatherData(
                location.latitude.toString(),
                location.longitude.toString()
            )
        }
    }

    private fun checkPermissions(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    this,
                    ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun enableLocationServices() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
}




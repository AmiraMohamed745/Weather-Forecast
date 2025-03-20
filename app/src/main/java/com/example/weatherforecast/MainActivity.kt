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
import com.example.weatherforecast.home.view.LocationHelper
import com.example.weatherforecast.home.view.LocationPermissionAlertDialog
import com.example.weatherforecast.model.CurrentWeatherResponse
import com.example.weatherforecast.ui.theme.composables.AppScaffold
import com.example.weatherforecast.utils.Constants
import org.osmdroid.config.Configuration.*

class MainActivity : ComponentActivity() {

    private lateinit var locationHelper: LocationHelper
    private lateinit var locationState: MutableState<Location?>
    private lateinit var weatherState: MutableState<CurrentWeatherResponse?>

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
                Surface(color = Color.White) {}
                locationState = remember { mutableStateOf<Location?>(null) }
                weatherState = remember { mutableStateOf<CurrentWeatherResponse?>(null) }
              //  showLocationPermissionAlertDialog = rememberSaveable { mutableStateOf(false) }
                if (showLocationPermissionAlertDialog.value) {
                    LocationPermissionAlertDialog(
                        onDismissRequest = { showLocationPermissionAlertDialog.value = false },
                        onConfirmation = {
                            enableLocationServices()
                            showLocationPermissionAlertDialog.value = false
                        }
                    )
                   // showLocationPermissionAlertDialog.value = false
                }
                AppScaffold(locationState, weatherState)


                /*SettingsScreen(onLocationOptionsSelected = { option ->
                    Log.d("MainActivity", "Location option selected: $option")
                })*/
                //SettingsScreenWithMapDisplayed()
                /*HomeScreen(
                    location = locationState.value,
                    currentWeatherResponse = weatherState.value,
                    modifier = Modifier.padding(1.dp)
                )*/
                //val scrollBehavior =
                //  TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
                /*Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color.White,
                                titleContentColor = Color.Black,
                            ),
                            title = {
                                Text(
                                    "Weather Forecast",
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Medium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = {}) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = stringResource(R.string.menu_icon)
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    },

                ) { contentPadding ->
                    HomeScreen(
                        location = locationState.value,
                        currentWeatherResponse = weatherState.value,
                        modifier = Modifier.padding(contentPadding)
                    )
                }*/
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkPermissions()) {
            if (locationHelper.isLocationEnabled()) {
                getFreshLocationAndFetchWeather()
            } else {
                //enableLocationServices()
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
            locationState.value = location
            fetchWeatherData(location)
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
        //Toast.makeText(this, "Please enable location services", Toast.LENGTH_LONG).show()
        /*LocationPermissionAlertDialog {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }*/
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

    // Received location: 29.9227137, 31.0597966
    private fun fetchWeatherData(location: Location) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val serviceObj = RetrofitHelper.weatherService
                val responseObj: CurrentWeatherResponse = serviceObj.getCurrentWeatherData(
                    lat = location.latitude.toString(),
                    lon = location.longitude.toString(),
                    lang = "en"
                )
                Log.i("MainActivity", "Weather API response: $responseObj")
                withContext(Dispatchers.Main) {
                    weatherState.value = responseObj
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MainActivity", "Error fetching weather data: ${e.localizedMessage}")
            }
        }
    }
}




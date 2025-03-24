package com.example.weatherforecast.utils.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.IOException
import java.util.Locale

class LocationHelper(private val context: Context) {

    private val fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            ContextCompat.getSystemService(context, LocationManager::class.java) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getFreshLocation(onLocationReceived: (Location) -> Unit) {
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    val latestLocation = location.lastLocation
                    if (latestLocation != null) {
                        onLocationReceived(latestLocation)
                        Log.i("MainActivity", "Received location: ${latestLocation.latitude}, ${latestLocation.longitude}")
                        fusedLocationProviderClient.removeLocationUpdates(this)
                    }
                }
            },
            Looper.myLooper()
        )
    }

    fun getAddress(location: Location?): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        var addressString = "N/A"
        try {
            val addresses = geocoder.getFromLocation(
                location?.latitude ?: 0.0,
                location?.longitude?: 0.0,
                1)

            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val sb = StringBuilder()
                for (i in 0 until address.maxAddressLineIndex) {
                    sb.append(address.getAddressLine(i)).append("\n")
                }
                addressString = sb.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressString
    }

}
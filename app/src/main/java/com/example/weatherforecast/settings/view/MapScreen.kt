package com.example.weatherforecast.settings.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.weatherforecast.utils.Constants
import org.osmdroid.views.MapView
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.MapEventsOverlay

@Composable
fun MapScreen(onLocationChosen: (latitude: Double, longitude: Double) -> Unit) {

    val context = LocalContext.current
    AndroidView(
        factory = { context ->
            val startPoint = GeoPoint(Constants.CENTER_POINT_LATITUDE, Constants.CENTER_POINT_LONGITUDE)
            var mapView = MapView(context)
            val mapController = mapView.controller
            mapView = MapView(context).apply {
                setMultiTouchControls(true)
                setTileSource(TileSourceFactory.MAPNIK)
                setUseDataConnection(true)
                mapController.setZoom(50.0)
                setZoomLevel(4.0)
                mapController.setCenter(startPoint)
            }
            val overlay = MapEventsOverlay(object : MapEventsReceiver {
                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    p?.let {
                        onLocationChosen(it.latitude, it.longitude)
                    }
                    return true
                }

                override fun longPressHelper(p: GeoPoint?): Boolean = false
            })
            mapView.overlays.add(overlay)
            mapView
        },
        update = { mapView -> }
    )
}
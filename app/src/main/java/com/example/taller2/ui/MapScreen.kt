package com.example.taller2.ui

import android.annotation.SuppressLint
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.taller2.R
import com.example.taller2.location.formatAddress
import com.example.taller2.location.geocodeLocationName
import com.example.taller2.location.reverseGeocode
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

data class MapMarkerData(
    val position: LatLng,
    val title: String,
    val snippet: String? = null
)

private val Bogota = LatLng(4.60971, -74.08175)
private const val DarkMapThreshold = 40f

@Composable
fun MapScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(Bogota, 11f)
    }

    var searchText by rememberSaveable { mutableStateOf("") }
    var hasLocationPermission by remember {
        mutableStateOf(context.hasLocationPermission())
    }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var currentAddress by remember { mutableStateOf<String?>(null) }
    var searchMarker by remember { mutableStateOf<MapMarkerData?>(null) }
    var hasCenteredOnUser by rememberSaveable { mutableStateOf(false) }
    val longClickMarkers = remember { mutableStateListOf<MapMarkerData>() }
    val traveledRoute = remember { mutableStateListOf<LatLng>() }
    val ambientLight = rememberAmbientLightLevel()
    val destinationPoint = searchMarker?.position ?: longClickMarkers.lastOrNull()?.position

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        hasLocationPermission = results.values.any { it }
        if (!hasLocationPermission) {
            scope.launch {
                snackbarHostState.showSnackbar("Se necesita permiso de ubicación para mostrar tu posición actual.")
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!hasLocationPermission) {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    TrackLocationUpdates(
        hasLocationPermission = hasLocationPermission,
        onLocation = { location ->
            val newPoint = LatLng(location.latitude, location.longitude)
            currentLocation = newPoint
            if (traveledRoute.isEmpty() || traveledRoute.last().distanceTo(newPoint) > 5f) {
                traveledRoute.add(newPoint)
            }
        }
    )

    LaunchedEffect(currentLocation) {
        val userLocation = currentLocation ?: return@LaunchedEffect
        if (!hasCenteredOnUser) {
            hasCenteredOnUser = true
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(userLocation, 16f))
        }
        if (currentAddress == null) {
            currentAddress = reverseGeocode(context, userLocation)
        }
    }

    val mapStyle = remember(ambientLight, context) {
        val styleRes = if ((ambientLight ?: 200f) < DarkMapThreshold) {
            R.raw.map_style_dark
        } else {
            R.raw.map_style_light
        }
        MapStyleOptions.loadRawResourceStyle(context, styleRes)
    }

    fun searchAddress() {
        if (searchText.isBlank()) return
        keyboardController?.hide()
        scope.launch {
            val result = geocodeLocationName(context, searchText)
            if (result != null) {
                val point = LatLng(result.latitude, result.longitude)
                val resolvedAddress = result.formatAddress()
                searchMarker = MapMarkerData(
                    position = point,
                    title = searchText,
                    snippet = resolvedAddress
                )
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(point, 16f))
            } else {
                snackbarHostState.showSnackbar("No fue posible encontrar esa dirección.")
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SnackbarHost(hostState = snackbarHostState)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Buscar dirección",
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        label = { Text("Ejemplo: Universidad Javeriana") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { searchAddress() }),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar"
                            )
                        }
                    )
                    Button(onClick = ::searchAddress) {
                        Text("Ir")
                    }
                }
                Text(
                    text = if ((ambientLight ?: 200f) < DarkMapThreshold) {
                        "Estilo del mapa: oscuro por baja luminosidad"
                    } else {
                        "Estilo del mapa: claro por alta luminosidad"
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = currentAddress?.let { "Ubicación actual: $it" }
                        ?: "Ubicación actual: esperando posición del usuario...",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = if (destinationPoint != null) {
                        "Ruta directa activa hacia el último punto buscado o marcado."
                    } else {
                        "Haz una búsqueda o un long click para crear un destino adicional."
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (!hasLocationPermission) {
                    Button(
                        onClick = {
                            locationPermissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                    ) {
                        Text("Conceder permiso de ubicación")
                    }
                }
            }
        }

        Card(modifier = Modifier.fillMaxSize()) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission,
                    mapStyleOptions = mapStyle
                ),
                uiSettings = MapUiSettings(
                    zoomControlsEnabled = true,
                    myLocationButtonEnabled = hasLocationPermission
                ),
                onMapLongClick = { point ->
                    scope.launch {
                        val title = reverseGeocode(context, point) ?: "Lat: ${point.latitude}, Lng: ${point.longitude}"
                        longClickMarkers.add(
                            MapMarkerData(
                                position = point,
                                title = title,
                                snippet = "Marcador agregado con long click"
                            )
                        )
                        cameraPositionState.animate(CameraUpdateFactory.newLatLng(point))
                    }
                }
            ) {
                currentLocation?.let { userPoint ->
                    Marker(
                        state = MarkerState(position = userPoint),
                        title = "Mi ubicación",
                        snippet = currentAddress ?: "Ubicación actual"
                    )
                }

                searchMarker?.let { marker ->
                    Marker(
                        state = MarkerState(position = marker.position),
                        title = marker.title,
                        snippet = marker.snippet
                    )
                }

                longClickMarkers.forEach { marker ->
                    Marker(
                        state = MarkerState(position = marker.position),
                        title = marker.title,
                        snippet = marker.snippet
                    )
                }

                if (traveledRoute.size > 1) {
                    Polyline(
                        points = traveledRoute.toList(),
                        color = MaterialTheme.colorScheme.primary,
                        width = 10f
                    )
                }

                val currentPoint = currentLocation
                if (currentPoint != null && destinationPoint != null) {
                    Polyline(
                        points = listOf(currentPoint, destinationPoint),
                        color = MaterialTheme.colorScheme.tertiary,
                        width = 8f
                    )
                }
            }
        }
    }
}

@Composable
private fun rememberAmbientLightLevel(): Float? {
    val context = LocalContext.current
    val sensorManager = remember {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }
    val lightSensor = remember(sensorManager) {
        sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
    }
    var ambientLight by remember { mutableStateOf<Float?>(null) }

    DisposableEffect(sensorManager, lightSensor) {
        if (lightSensor == null) {
            ambientLight = null
            return@DisposableEffect onDispose { }
        }

        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                ambientLight = event?.values?.firstOrNull()
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
        }

        sensorManager.registerListener(listener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    return ambientLight
}

@Composable
@SuppressLint("MissingPermission")
private fun TrackLocationUpdates(
    hasLocationPermission: Boolean,
    onLocation: (Location) -> Unit
) {
    val context = LocalContext.current
    val fusedLocationProviderClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    DisposableEffect(hasLocationPermission) {
        if (!hasLocationPermission || !context.hasLocationPermission()) {
            return@DisposableEffect onDispose { }
        }

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3_000L)
            .setMinUpdateIntervalMillis(2_000L)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let(onLocation)
            }
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            location?.let(onLocation)
        }
        fusedLocationProviderClient.requestLocationUpdates(
            request,
            callback,
            context.mainLooper
        )

        onDispose {
            fusedLocationProviderClient.removeLocationUpdates(callback)
        }
    }
}

private fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
}

private fun LatLng.distanceTo(other: LatLng): Float {
    val results = FloatArray(1)
    Location.distanceBetween(
        latitude,
        longitude,
        other.latitude,
        other.longitude,
        results
    )
    return results.firstOrNull() ?: 0f
}


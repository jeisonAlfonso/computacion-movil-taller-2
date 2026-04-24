package com.example.taller2.location

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import kotlin.coroutines.resume

suspend fun geocodeLocationName(context: Context, query: String): Address? {
    val geocoder = Geocoder(context, Locale.getDefault())
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocationName(query, 1) { addresses ->
                    continuation.resume(addresses.firstOrNull())
                }
            }
        } else {
            withContext(Dispatchers.IO) {
                @Suppress("DEPRECATION")
                geocoder.getFromLocationName(query, 1)?.firstOrNull()
            }
        }
    } catch (_: IOException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    }
}

suspend fun reverseGeocode(context: Context, point: LatLng): String? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val address = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCancellableCoroutine { continuation ->
                geocoder.getFromLocation(point.latitude, point.longitude, 1) { addresses ->
                    continuation.resume(addresses.firstOrNull())
                }
            }
        } else {
            withContext(Dispatchers.IO) {
                @Suppress("DEPRECATION")
                geocoder.getFromLocation(point.latitude, point.longitude, 1)?.firstOrNull()
            }
        }
    } catch (_: IOException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    }

    return address?.formatAddress()
}

fun Address.formatAddress(): String {
    return listOfNotNull(
        thoroughfare,
        subThoroughfare,
        locality,
        adminArea,
        countryName
    ).joinToString(separator = ", ").ifBlank {
        getAddressLine(0) ?: "Dirección desconocida"
    }
}


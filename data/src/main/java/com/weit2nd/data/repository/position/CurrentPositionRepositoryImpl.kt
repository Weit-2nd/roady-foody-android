package com.weit2nd.data.repository.position

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.weit2nd.data.source.position.LocationPermissionDataSource
import com.weit2nd.domain.model.Location
import com.weit2nd.domain.repository.position.CurrentPositionRepository
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class CurrentPositionRepositoryImpl @Inject constructor(
    private val locationPermissionDataSource: LocationPermissionDataSource,
    private val locationClient: FusedLocationProviderClient,
) : CurrentPositionRepository {

    override suspend fun getCurrentPosition(): Location {
        val defaultLocation = Location(latitude = 37.5597706, longitude = 126.9423666)

        if (locationPermissionDataSource.requestLocationPermission()) {
            return suspendCancellableCoroutine { continuation ->
                requestLocationUpdates(continuation)
            }
        }
        return defaultLocation
    }

    // TedPermission.checkGranted()를 통해 권한 허용 확인 완료
    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates(continuation: CancellableContinuation<Location>) {
        val request = LocationRequest.Builder(1000L)
            .setIntervalMillis(1000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = Location(
                    latitude = locationResult.locations.first().latitude,
                    longitude = locationResult.locations.first().longitude,
                )
                continuation.resume(location)
                locationClient.removeLocationUpdates(this)
            }
        }

        continuation.invokeOnCancellation {
            locationClient.removeLocationUpdates(callback)
        }

        locationClient.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )
    }
}

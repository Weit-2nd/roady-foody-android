package com.weit2nd.data.repository.position

import android.annotation.SuppressLint
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.weit2nd.data.source.position.LocationPermissionDataSource
import com.weit2nd.domain.exception.LocationException
import com.weit2nd.domain.model.Location
import com.weit2nd.domain.repository.position.CurrentPositionRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CurrentPositionRepositoryImpl @Inject constructor(
    private val locationPermissionDataSource: LocationPermissionDataSource,
    private val locationClient: FusedLocationProviderClient,
) : CurrentPositionRepository {

    private val locationRequest = LocationRequest.Builder(1000L)
        .setIntervalMillis(1000L)
        .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        .build()

    override suspend fun getCurrentPosition(): Location {
        if (locationPermissionDataSource.requestLocationPermission()) {
            return requestLocationUpdates()
        }
        throw SecurityException("위치 권한이 허용되지 않았습니다.")
    }

    // TedPermission.checkGranted()를 통해 권한 허용 확인 완료
    @SuppressLint("MissingPermission")
    private suspend fun requestLocationUpdates() = callbackFlow {

        val locationCallback = object : LocationCallback() {
            override fun onLocationAvailability(availability: LocationAvailability) {
                if (availability.isLocationAvailable.not()) {
                    close(LocationException("위치 정보를 받아올 수 없습니다."))
                }
            }

            override fun onLocationResult(locationResult: LocationResult) {
                val location = Location(
                    latitude = locationResult.locations.first().latitude,
                    longitude = locationResult.locations.first().longitude,
                )
                trySend(location)
            }
        }

        locationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }.first()
}

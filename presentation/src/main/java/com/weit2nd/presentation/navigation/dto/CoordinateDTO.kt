package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import com.weit2nd.domain.model.Coordinate
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoordinateDTO(
    val latitude: Double,
    val longitude: Double,
) : Parcelable

fun CoordinateDTO.toCoordinate(): Coordinate =
    Coordinate(
        latitude = latitude,
        longitude = longitude,
    )

fun Coordinate.toCoordinateDTO(): CoordinateDTO =
    CoordinateDTO(
        latitude = latitude,
        longitude = longitude,
    )

package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import com.weit2nd.domain.model.search.Place
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceDTO(
    val placeName: String,
    val addressName: String,
    val roadAddressName: String,
    val longitude: Double,
    val latitude: Double,
    val tel: String,
) : Parcelable

fun PlaceDTO.toPlace(): Place =
    Place(
        placeName = placeName,
        addressName = addressName,
        roadAddressName = roadAddressName,
        longitude = longitude,
        latitude = latitude,
        tel = tel,
    )

fun Place.toPlaceDTO(): PlaceDTO =
    PlaceDTO(
        placeName = placeName,
        addressName = addressName,
        roadAddressName = roadAddressName,
        longitude = longitude,
        latitude = latitude,
        tel = tel,
    )

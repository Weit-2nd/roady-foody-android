package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PlaceSearchDTO(
    val searchWords: String,
    val coordinate: CoordinateDTO?,
) : Parcelable

package com.weit2nd.domain.model.spot

sealed class ReportFoodSpotState {
    data object BadCoordinate : ReportFoodSpotState()

    data object BadFoodSpotName : ReportFoodSpotState()

    data object TooManyImages : ReportFoodSpotState()

    data class InvalidImage(
        val uri: String,
    ) : ReportFoodSpotState()

    data object Valid : ReportFoodSpotState()
}

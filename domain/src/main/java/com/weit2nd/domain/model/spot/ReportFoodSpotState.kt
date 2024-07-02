package com.weit2nd.domain.model.spot

sealed class ReportFoodSpotState {
    data object BadCoordinateState : ReportFoodSpotState()
    data object BadFoodSpotNameState : ReportFoodSpotState()
    data object TooManyImagesState : ReportFoodSpotState()
    data class InvalidImageState(
        val uri: String,
    ) : ReportFoodSpotState()
}

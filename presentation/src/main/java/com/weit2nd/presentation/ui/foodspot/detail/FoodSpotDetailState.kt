package com.weit2nd.presentation.ui.foodspot.detail

import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.model.spot.FoodSpotOpenState
import com.weit2nd.presentation.model.foodspot.OperationHour
import com.weit2nd.presentation.model.foodspot.Review

data class FoodSpotDetailState(
    val name: String = "",
    val position: LatLng = LatLng.from(37.5597706, 126.9423666),
    val movableFoodSpots: Boolean = false,
    val openState: FoodSpotOpenState = FoodSpotOpenState.UNKNOWN,
    val address: String = "",
    val storeClosure: Boolean = false,
    val operationHours: List<OperationHour> = emptyList(),
    val foodCategoryList: List<FoodCategory> = emptyList(),
    val foodSpotsPhotos: List<String> = emptyList(),
    val reviews: List<Review> = emptyList(),
    val hasMoreReviews: Boolean = false,
    val isOperationHoursOpen: Boolean = false,
    val isLoading: Boolean = true,
    val shouldRetry: Boolean = false,
    val isViewMoreReviewVisible: Boolean = false,
    val map: KakaoMap? = null,
)

package com.weit2nd.presentation.ui.mypage.foodspot

import com.weit2nd.domain.model.spot.FoodSpotHistoryContent

data class FoodSpotHistoryState(
    val totalCount: Int = 0,
    val foodSpots: List<FoodSpotHistoryContent> = emptyList(),
)

package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.FoodSpotReviewDTO

class FoodSpotReviewType :
    BaseNavType<FoodSpotReviewDTO>(
        target = FoodSpotReviewDTO::class.java,
        isNullableAllowed = false,
    )

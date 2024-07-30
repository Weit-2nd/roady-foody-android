package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.FoodSpotForReviewDTO

class FoodSpotForReviewType :
    BaseNavType<FoodSpotForReviewDTO>(
        target = FoodSpotForReviewDTO::class.java,
        isNullableAllowed = false,
    )

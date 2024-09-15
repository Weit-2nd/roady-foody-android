package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.ReviewHistoryDTO

class ReviewHistoryType :
    BaseNavType<ReviewHistoryDTO>(
        target = ReviewHistoryDTO::class.java,
        isNullableAllowed = false,
    )

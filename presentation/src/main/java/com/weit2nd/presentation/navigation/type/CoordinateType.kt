package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.CoordinateDTO

class CoordinateType :
    BaseNavType<CoordinateDTO>(
        target = CoordinateDTO::class.java,
        isNullableAllowed = false,
    )

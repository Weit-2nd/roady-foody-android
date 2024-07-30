package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.PlaceDTO

class PlaceType :
    BaseNavType<PlaceDTO>(
        target = PlaceDTO::class.java,
        isNullableAllowed = false,
    )

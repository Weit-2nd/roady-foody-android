package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO

class PlaceSearchType :
    BaseNavType<PlaceSearchDTO>(
        target = PlaceSearchDTO::class.java,
        isNullableAllowed = true,
    )

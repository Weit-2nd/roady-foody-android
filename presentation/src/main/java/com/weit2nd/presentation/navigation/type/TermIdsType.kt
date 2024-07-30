package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.TermIdsDTO

class TermIdsType :
    BaseNavType<TermIdsDTO>(
        target = TermIdsDTO::class.java,
        isNullableAllowed = false,
    )

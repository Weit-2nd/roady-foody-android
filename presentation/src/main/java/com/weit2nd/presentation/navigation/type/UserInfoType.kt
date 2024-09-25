package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.UserInfoDTO

class UserInfoType :
    BaseNavType<UserInfoDTO>(
        target = UserInfoDTO::class.java,
        isNullableAllowed = false,
    )

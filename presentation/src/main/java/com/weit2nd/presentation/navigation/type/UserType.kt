package com.weit2nd.presentation.navigation.type

import com.weit2nd.presentation.navigation.dto.UserDTO

class UserType :
    BaseNavType<UserDTO>(
        target = UserDTO::class.java,
        isNullableAllowed = false,
    )

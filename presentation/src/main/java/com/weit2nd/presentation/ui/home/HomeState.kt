package com.weit2nd.presentation.ui.home

import com.weit2nd.domain.model.User

data class HomeState(
    val user: User =
        User(
            name = "",
        ),
)

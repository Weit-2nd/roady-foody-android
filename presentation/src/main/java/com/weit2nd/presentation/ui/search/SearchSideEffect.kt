package com.weit2nd.presentation.ui.search

import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO

sealed interface SearchSideEffect {
    data class ShowToastMessage(
        val message: String,
    ) : SearchSideEffect

    data object NavToBack : SearchSideEffect

    data class NavToHome(
        val placeSearch: PlaceSearchDTO,
    ) : SearchSideEffect
}

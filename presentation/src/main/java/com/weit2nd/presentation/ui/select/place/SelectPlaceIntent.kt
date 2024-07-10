package com.weit2nd.presentation.ui.select.place

sealed class SelectPlaceIntent {
    data object SearchPlace : SelectPlaceIntent()

    data class StoreSearchWord(
        val input: String,
    ) : SelectPlaceIntent()
}

package com.weit2nd.presentation.ui.select.location

sealed class SelectLocationIntent {

    data object SearchLocation : SelectLocationIntent()
    data class StoreSearchWord(val input: String) : SelectLocationIntent()
}

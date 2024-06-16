package com.weit2nd.presentation.ui.select.location

sealed class SelectLocationIntent {

    data class SearchLocation(val input: String) : SelectLocationIntent()
}

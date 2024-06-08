package com.weit2nd.presentation.ui.select.picture

sealed class SelectPictureIntent {
    data object RefreshImages : SelectPictureIntent()
}

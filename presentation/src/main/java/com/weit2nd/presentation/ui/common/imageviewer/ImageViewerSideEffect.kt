package com.weit2nd.presentation.ui.common.imageviewer

sealed class ImageViewerSideEffect {
    data object ExitImageViewer : ImageViewerSideEffect()
}

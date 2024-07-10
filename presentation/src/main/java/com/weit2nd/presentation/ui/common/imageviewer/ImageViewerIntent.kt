package com.weit2nd.presentation.ui.common.imageviewer

sealed class ImageViewerIntent {
    data object ExitImageViewer : ImageViewerIntent()
}

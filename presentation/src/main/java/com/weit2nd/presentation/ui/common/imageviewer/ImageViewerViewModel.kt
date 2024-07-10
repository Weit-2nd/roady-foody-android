package com.weit2nd.presentation.ui.common.imageviewer

import androidx.lifecycle.SavedStateHandle
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.ImageViewerRoutes
import com.weit2nd.presentation.navigation.dto.ImageViewerDTO
import com.weit2nd.presentation.navigation.dto.toImageViewerData
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class ImageViewerViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ImageViewerState, ImageViewerSideEffect>() {
    private val imageViewerData by lazy {
        checkNotNull(
            savedStateHandle
                .get<ImageViewerDTO>(ImageViewerRoutes.IMAGES_VIEWER_DATA_KEY)
                ?.toImageViewerData(),
        )
    }
    override val container =
        container<ImageViewerState, ImageViewerSideEffect>(
            ImageViewerState(
                images = imageViewerData.images,
                position = imageViewerData.position,
            ),
        )

    fun onClickExitButton() {
        ImageViewerIntent.ExitImageViewer.post()
    }

    private fun ImageViewerIntent.post() =
        intent {
            when (this@post) {
                ImageViewerIntent.ExitImageViewer -> {
                    postSideEffect(ImageViewerSideEffect.ExitImageViewer)
                }
            }
        }
}

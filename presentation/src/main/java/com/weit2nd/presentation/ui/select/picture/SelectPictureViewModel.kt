package com.weit2nd.presentation.ui.select.picture

import com.weit2nd.domain.usecase.localimage.GetLocalImagesUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SelectPictureViewModel @Inject constructor(
    private val getLocalImagesUseCase: GetLocalImagesUseCase,
) : BaseViewModel<SelectPictureState, SelectPictureSideEffect>() {
    override val container = container<SelectPictureState, SelectPictureSideEffect>(SelectPictureState())

    fun onCreate() {
        SelectPictureIntent.RefreshImages.post()
    }

    private fun SelectPictureIntent.post() =
        intent {
            when (this@post) {
                SelectPictureIntent.RefreshImages -> {
                    val images = getLocalImagesUseCase.invoke().toPersistentList()
                    reduce {
                        state.copy(
                            images = images,
                        )
                    }
                }
            }
        }
}

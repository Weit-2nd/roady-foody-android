package com.weit2nd.presentation.ui.review

import androidx.lifecycle.SavedStateHandle
import com.weit2nd.domain.model.review.PostReviewVerificationState
import com.weit2nd.domain.usecase.pickimage.PickMultipleImagesUseCase
import com.weit2nd.domain.usecase.review.PostReviewUseCase
import com.weit2nd.domain.usecase.review.VerifyReviewUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.PostReviewRoutes
import com.weit2nd.presentation.navigation.dto.FoodSpotForReviewDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PostReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val verifyReviewUseCase: VerifyReviewUseCase,
    private val postReviewUseCase: PostReviewUseCase,
    private val pickMultipleImagesUseCase: PickMultipleImagesUseCase,
) : BaseViewModel<PostReviewState, PostReviewSideEffect>() {
    private val foodSpot by lazy {
        checkNotNull(
            savedStateHandle
                .get<FoodSpotForReviewDTO>(PostReviewRoutes.FOOD_SPOT_FOR_REVIEW_KEY),
        )
    }

    override val container: Container<PostReviewState, PostReviewSideEffect> =
        container(
            PostReviewState(
                foodSpotName = foodSpot.name,
                maxLength = MAX_CONTENT_LENGTH,
            ),
        )

    fun onPostReviewButtonClick() {
        PostReviewIntent.PostReview.post()
    }

    fun onPickImageButtonClick() {
        PostReviewIntent.PickImage.post()
    }

    fun onDeleteImageButtonClick(image: String) {
        PostReviewIntent.DeleteImage(image).post()
    }

    fun onRatingChanged(rating: Float) {
        PostReviewIntent.ChangeRating(rating).post()
    }

    fun onReviewChanged(content: String) {
        PostReviewIntent.ChangeReviewContent(content).post()
    }

    fun onNavigationButtonClick() {
        PostReviewIntent.NavToBack.post()
    }

    private fun PostReviewIntent.post() =
        intent {
            when (this@post) {
                PostReviewIntent.PostReview -> {
                    val ratingForRequest = state.rating.toInt()
                    val verificationState =
                        verifyReviewUseCase.invoke(
                            contents = state.content,
                            rating = ratingForRequest,
                            images = state.selectedImages,
                        )
                    if (verificationState == PostReviewVerificationState.Valid) {
                        runCatching {
                            postReviewUseCase.invoke(
                                foodSpotId = foodSpot.id,
                                contents = state.content,
                                rating = ratingForRequest,
                                images = state.selectedImages,
                            )
                        }.onSuccess {
                            postSideEffect(PostReviewSideEffect.ShowToast("작성 했어용"))
                            postSideEffect(PostReviewSideEffect.NavToBack)
                        }.onFailure {
                            postSideEffect(PostReviewSideEffect.ShowToast(it.message.toString()))
                        }
                    } else {
                        postSideEffect(PostReviewSideEffect.ShowToast(verificationState.getErrorMessage()))
                    }
                }
                PostReviewIntent.PickImage -> {
                    val maxCount = MAX_IMAGE_COUNT - state.selectedImages.count()
                    if (maxCount > 0) {
                        val images = pickMultipleImagesUseCase.invoke(maxCount)
                        reduce {
                            state.copy(
                                selectedImages = state.selectedImages + images,
                            )
                        }
                    } else {
                        postSideEffect(PostReviewSideEffect.ShowToast("이미지는 최대 ${MAX_IMAGE_COUNT}장 까지 선택 가능합니다."))
                    }
                }

                is PostReviewIntent.ChangeRating -> {
                    reduce {
                        state.copy(
                            rating = rating,
                        )
                    }
                }

                is PostReviewIntent.ChangeReviewContent -> {
                    if (content.length < state.maxLength) {
                        reduce {
                            state.copy(
                                content = content,
                            )
                        }
                    }
                }

                is PostReviewIntent.DeleteImage -> {
                    reduce {
                        state.copy(
                            selectedImages = state.selectedImages.minus(image),
                        )
                    }
                }
                PostReviewIntent.NavToBack -> {
                    postSideEffect(PostReviewSideEffect.NavToBack)
                }
            }
        }

    private fun PostReviewVerificationState.getErrorMessage(): String {
        return when (this) {
            PostReviewVerificationState.EmptyContents -> "리뷰를 작성해주세요!"
            PostReviewVerificationState.InvalidImage -> "유효하지 않은 이미지에요!"
            PostReviewVerificationState.InvalidRating -> "어캐 실패했지"
            is PostReviewVerificationState.TooManyContents -> "최대 ${maxLength}자 까지 작성 가능해요!"
            PostReviewVerificationState.Valid -> ""
        }
    }

    companion object {
        private const val MAX_IMAGE_COUNT = 3
        private const val MAX_CONTENT_LENGTH = 300
    }
}

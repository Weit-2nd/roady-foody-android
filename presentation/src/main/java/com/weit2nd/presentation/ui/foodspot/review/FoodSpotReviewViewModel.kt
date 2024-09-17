package com.weit2nd.presentation.ui.foodspot.review

import androidx.lifecycle.SavedStateHandle
import com.weit2nd.domain.model.spot.FoodSpotReview
import com.weit2nd.domain.model.spot.ReviewSortType
import com.weit2nd.domain.usecase.spot.GetFoodSpotReviewsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.model.reivew.ExpendableReview
import com.weit2nd.presentation.navigation.FoodSpotReviewRoutes
import com.weit2nd.presentation.navigation.dto.FoodSpotReviewDTO
import com.weit2nd.presentation.navigation.dto.toFoodCategory
import com.weit2nd.presentation.navigation.dto.toRatingCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class FoodSpotReviewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFoodSpotReviewsUseCase: GetFoodSpotReviewsUseCase,
) : BaseViewModel<FoodSpotReviewState, FoodSpotReviewSideEffect>() {
    private val foodSpotReviewDTO = savedStateHandle.get<FoodSpotReviewDTO>(FoodSpotReviewRoutes.FOOD_SPOT_REVIEW_KEY)!!
    override val container: Container<FoodSpotReviewState, FoodSpotReviewSideEffect> =
        container(
            FoodSpotReviewState(
                id = foodSpotReviewDTO.id,
                name = foodSpotReviewDTO.name,
                foodSpotsPhotos = foodSpotReviewDTO.foodSpotsPhotos,
                movableFoodSpots = foodSpotReviewDTO.movableFoodSpots,
                categories = foodSpotReviewDTO.categories.map { it.toFoodCategory() },
                reviewCount = foodSpotReviewDTO.reviewCount,
                averageRating = foodSpotReviewDTO.averageRating,
                ratingCounts =
                    foodSpotReviewDTO.ratingCounts
                        .map { it.toRatingCount() }
                        .sortedBy { it.rating },
            ),
        )
    private var hasNext = AtomicBoolean(true)
    private var requestReviewJob: Job =
        Job().apply {
            complete()
        }

    fun onCreate() {
        requestReviewJob = FoodSpotReviewIntent.LoadNextReviews(null).post()
    }

    fun onImageClick(position: Int) {
        // TODO 사진 상세 화면 이동
    }

    fun onPostReviewClick() {
        FoodSpotReviewIntent.NavToPostReview.post()
    }

    fun onLastVisibleItemChanged(
        totalSize: Int,
        currentPosition: Int,
    ) {
        val needNextPage = (totalSize - currentPosition) <= REMAINING_PAGE_FOR_REQUEST
        val isRequestEnable = requestReviewJob.isCompleted && needNextPage && hasNext.get()
        if (isRequestEnable) {
            container.stateFlow.value.reviews.lastOrNull()?.let {
                requestReviewJob = FoodSpotReviewIntent.LoadNextReviews(it.review.reviewId).post()
            }
        }
    }

    fun onReviewContentsClick(position: Int) {
        FoodSpotReviewIntent
            .ChangeReviewContentExpendState(
                position = position,
                expandState = false,
            ).post()
    }

    fun onReviewContentsReadMoreClick(position: Int) {
        FoodSpotReviewIntent
            .ChangeReviewContentExpendState(
                position = position,
                expandState = true,
            ).post()
    }

    private fun FoodSpotReviewIntent.post() =
        intent {
            when (this@post) {
                FoodSpotReviewIntent.NavToPostReview -> {
                }
                is FoodSpotReviewIntent.ChangeReviewContentExpendState -> {
                    val updatedReviews =
                        state.reviews
                            .mapIndexed { index, review ->
                                if (index == position) {
                                    review.copy(
                                        isExpended = expandState,
                                    )
                                } else {
                                    review
                                }
                            }
                    reduce {
                        state.copy(
                            reviews = updatedReviews,
                        )
                    }
                }

                is FoodSpotReviewIntent.LoadNextReviews -> {
                    runCatching {
                        getFoodSpotReviewsUseCase.invoke(
                            foodSpotsId = state.id,
                            count = DEFAULT_LOAD_REVIEW_COUNT,
                            lastItemId = lastId,
                            sortType = ReviewSortType.LATEST,
                        )
                    }.onSuccess { foodSpotReviews ->
                        val reviews = foodSpotReviews.reviews.toReviews()
                        reduce {
                            hasNext.set(foodSpotReviews.hasNext)
                            state.copy(
                                reviews = reviews,
                            )
                        }
                    }.onFailure {
                        postSideEffect(FoodSpotReviewSideEffect.ShowNetworkErrorMessage)
                    }
                }
            }
        }

    private fun FoodSpotReview.toReview() =
        ExpendableReview(
            review =
                Review(
                    reviewId = id,
                    userId = userInfo.id,
                    profileImage = userInfo.profileImage,
                    nickname = userInfo.nickname,
                    date = createdAt,
                    rating = rate.toFloat(),
                    reviewImages = photos.map { it.image },
                    contents = contents,
                ),
            isExpended = false,
        )

    private fun List<FoodSpotReview>.toReviews() =
        map {
            it.toReview()
        }

    companion object {
        private const val DEFAULT_LOAD_REVIEW_COUNT = 10
        private const val REMAINING_PAGE_FOR_REQUEST = 3
    }
}

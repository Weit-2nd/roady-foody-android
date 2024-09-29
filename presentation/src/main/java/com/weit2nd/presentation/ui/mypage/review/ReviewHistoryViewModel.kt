package com.weit2nd.presentation.ui.mypage.review

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.weit2nd.domain.exception.user.UserReviewException
import com.weit2nd.domain.model.review.UserReview
import com.weit2nd.domain.usecase.user.GetUserReviewsUseCase
import com.weit2nd.domain.usecase.user.GetUserStatisticsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.model.reivew.ExpendableReview
import com.weit2nd.presentation.navigation.ReviewHistoryRoutes
import com.weit2nd.presentation.navigation.dto.UserInfoDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class ReviewHistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserReviewsUseCase: GetUserReviewsUseCase,
    private val getUserStatisticsUseCase: GetUserStatisticsUseCase,
) : BaseViewModel<ReviewHistoryState, ReviewHistorySideEffect>() {
    override val container: Container<ReviewHistoryState, ReviewHistorySideEffect> =
        container(ReviewHistoryState())
    private val userInfo =
        savedStateHandle.get<UserInfoDTO>(ReviewHistoryRoutes.REVIEW_HISTORY_KEY)!!
    private var hasNext = AtomicBoolean(true)
    private var requestReviewJob: Job =
        Job().apply {
            complete()
        }

    fun onCreate() {
        viewModelScope.launch {
            ReviewHistoryIntent.InitTotalCount.post()
        }
        ReviewHistoryIntent.LoadNextReviews(null).post()
    }

    fun onNavigationClick() {
        requestReviewJob = ReviewHistoryIntent.NavToBack.post()
    }

    fun onLastVisibleItemChanged(position: Int) {
        val currentReviewSize = container.stateFlow.value.reviews.size
        val needNextPage = (currentReviewSize - position) <= REMAINING_PAGE_FOR_REQUEST
        val isRequestEnable = requestReviewJob.isCompleted && needNextPage && hasNext.get()
        if (isRequestEnable) {
            container.stateFlow.value.reviews.lastOrNull()?.let {
                requestReviewJob = ReviewHistoryIntent.LoadNextReviews(it.review.reviewId).post()
            }
        }
    }

    fun onReviewContentsClick(position: Int) {
        ReviewHistoryIntent
            .ChangeReviewContentExpendState(
                position = position,
                expandState = false,
            ).post()
    }

    fun onReviewContentsReadMoreClick(position: Int) {
        ReviewHistoryIntent
            .ChangeReviewContentExpendState(
                position = position,
                expandState = true,
            ).post()
    }

    private fun ReviewHistoryIntent.post() =
        intent {
            when (this@post) {
                ReviewHistoryIntent.NavToBack -> {
                    postSideEffect(ReviewHistorySideEffect.NavToBack)
                }

                is ReviewHistoryIntent.LoadNextReviews -> {
                    runCatching {
                        getUserReviewsUseCase.invoke(
                            userId = userInfo.userId,
                            count = DEFAULT_LOAD_REVIEW_COUNT,
                            lastItemId = lastId,
                        )
                    }.onSuccess { userReviews ->
                        val reviews =
                            userReviews.toReviews(
                                userId = userInfo.userId,
                                nickname = userInfo.nickname,
                                profileImage = userInfo.profileImage,
                            )
                        reduce {
                            state.copy(
                                reviews = state.reviews.plus(reviews),
                            )
                        }
                    }.onFailure {
                        if (it is UserReviewException.NoMoreReviewException) {
                            hasNext.set(false)
                        } else {
                            postSideEffect(ReviewHistorySideEffect.ShowNetworkErrorMessage)
                        }
                    }
                }

                is ReviewHistoryIntent.ChangeReviewContentExpendState -> {
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

                ReviewHistoryIntent.InitTotalCount -> {
                    runCatching {
                        getUserStatisticsUseCase.invoke(userInfo.userId).reviewCount
                    }.onSuccess {
                        reduce {
                            state.copy(
                                totalCount = it,
                            )
                        }
                    }
                }
            }
        }

    private fun UserReview.toReview(
        userId: Long,
        nickname: String,
        profileImage: String?,
    ) = ExpendableReview(
        review =
            Review(
                reviewId = id,
                userId = userId,
                nickname = nickname,
                profileImage = profileImage,
                date = createdAt,
                rating = rating.toFloat(),
                reviewImages = photos.map { it.image },
                contents = contents,
            ),
        isExpended = false,
    )

    private fun List<UserReview>.toReviews(
        userId: Long,
        nickname: String,
        profileImage: String?,
    ) = map {
        it.toReview(
            userId = userId,
            nickname = nickname,
            profileImage = profileImage,
        )
    }

    companion object {
        private const val DEFAULT_LOAD_REVIEW_COUNT = 10
        private const val REMAINING_PAGE_FOR_REQUEST = 3
    }
}

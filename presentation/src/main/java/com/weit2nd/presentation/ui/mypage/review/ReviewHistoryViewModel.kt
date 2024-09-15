package com.weit2nd.presentation.ui.mypage.review

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.weit2nd.domain.model.review.UserReview
import com.weit2nd.domain.usecase.user.GetUserReviewsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.navigation.ReviewHistoryRoutes
import com.weit2nd.presentation.navigation.dto.ReviewHistoryDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class ReviewHistoryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserReviewsUseCase: GetUserReviewsUseCase,
) : BaseViewModel<ReviewHistoryState, ReviewHistorySideEffect>() {
    override val container: Container<ReviewHistoryState, ReviewHistorySideEffect> =
        container(ReviewHistoryState())
    private val userInfo =
        savedStateHandle.get<ReviewHistoryDTO>(ReviewHistoryRoutes.REVIEW_HISTORY_KEY)!!
    private var hasNext = AtomicBoolean(true)

    fun onCreate() {
        ReviewHistoryIntent.LoadNextReviews(null).post()
    }

    fun onNavigationClick() {
        ReviewHistoryIntent.NavToBack.post()
    }

    fun onFirstVisibleItemChanged(position: Int) {
        Log.d("MainTest", "$position")
    }

    private fun ReviewHistoryIntent.post() =
        intent {
            when (this@post) {
                ReviewHistoryIntent.NavToBack -> {
                    ReviewHistorySideEffect.NavToBack
                }
                is ReviewHistoryIntent.LoadNextReviews -> {
                    val result =
                        runCatching {
                            getUserReviewsUseCase.invoke(
                                userId = userInfo.userId,
                                count = DEFAULT_LOAD_REVIEW_COUNT,
                                lastItemId = lastId,
                            )
                        }
                    if (result.isSuccess) {
                        val reviews =
                            result.getOrThrow().toReviews(
                                userId = userInfo.userId,
                                nickname = userInfo.nickname,
                                profileImage = userInfo.profileImage,
                            )
                        reduce {
                            state.copy(
                                reviews = state.reviews.plus(reviews),
                            )
                        }
                    } else {
                        val error = result.exceptionOrNull()
                    }
                }
            }
        }

    private fun UserReview.toReview(
        userId: Long,
        nickname: String,
        profileImage: String?,
    ) = Review(
        userId = userId,
        nickname = nickname,
        profileImage = profileImage,
        date = createdAt,
        rating = rating.toFloat(),
        reviewImages = photos.map { it.image },
        contents = contents,
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
    }
}

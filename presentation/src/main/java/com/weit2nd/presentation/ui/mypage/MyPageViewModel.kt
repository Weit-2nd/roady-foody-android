package com.weit2nd.presentation.ui.mypage

import com.weit2nd.domain.exception.user.UserFoodSpotException
import com.weit2nd.domain.exception.user.UserReviewException
import com.weit2nd.domain.usecase.logout.LogoutUseCase
import com.weit2nd.domain.usecase.logout.WithdrawUseCase
import com.weit2nd.domain.usecase.spot.GetFoodSpotHistoriesUseCase
import com.weit2nd.domain.usecase.user.GetMyUserIdUseCase
import com.weit2nd.domain.usecase.user.GetMyUserInfoUseCase
import com.weit2nd.domain.usecase.user.GetUserReviewsUseCase
import com.weit2nd.domain.usecase.user.GetUserStatisticsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.navigation.dto.UserInfoDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val withdrawUseCase: WithdrawUseCase,
    private val getMyUserIdUseCase: GetMyUserIdUseCase,
    private val getMyUserInfoUseCase: GetMyUserInfoUseCase,
    private val getFoodSpotHistoriesUseCase: GetFoodSpotHistoriesUseCase,
    private val getUserReviewsUseCase: GetUserReviewsUseCase,
    private val getUserStatisticsUseCase: GetUserStatisticsUseCase,
) : BaseViewModel<MyPageState, MyPageSideEffect>() {
    override val container: Container<MyPageState, MyPageSideEffect> = container(MyPageState())

    fun onCreate() {
        MyPageIntent.GetMyUserInfo.post()
    }

    fun onUserInfoEditButtonClick() {
        MyPageIntent.NavToUserInfoEdit.post()
    }

    fun onLogoutButtonClick() {
        MyPageIntent.SetLogoutDialogShownState(true).post()
    }

    fun onWithdrawButtonClick() {
        MyPageIntent.SetWithdrawDialogShownState(true).post()
    }

    fun onLogoutConfirm() {
        MyPageIntent.Logout.post()
    }

    fun onWithdrawConfirm() {
        MyPageIntent.Withdraw.post()
    }

    fun onLogoutDialogClose() {
        MyPageIntent.SetLogoutDialogShownState(false).post()
    }

    fun onWithdrawDialogClose() {
        MyPageIntent.SetWithdrawDialogShownState(false).post()
    }

    fun onBackButtonClick() {
        MyPageIntent.NavToBack.post()
    }

    fun onReviewHistoryClick() {
        MyPageIntent.NavToReviewHistory.post()
    }

    fun onFoodSpotHistoryClick() {
        MyPageIntent.NavToFoodSpotHistory.post()
    }

    fun onFoodSpotContentClick(foodSpotId: Long) {
        MyPageIntent.NavToFoodSpotDetail(foodSpotId).post()
    }

    fun onRankingButtonClick() {
        MyPageIntent.SetRankingDialogShownState(true).post()
    }

    fun onRankingCloseButtonClick() {
        MyPageIntent.SetRankingDialogShownState(false).post()
    }

    private fun MyPageIntent.post() =
        intent {
            when (this@post) {
                MyPageIntent.GetMyUserInfo -> {
                    reduce {
                        state.copy(
                            isLoading = true,
                        )
                    }
                    coroutineScope {
                        runCatching {
                            val userId = getMyUserIdUseCase.invoke()
                            val userInfoDeferred =
                                async {
                                    getMyUserInfoUseCase.invoke()
                                }
                            val reportedFoodSpotDeferred =
                                async {
                                    runCatching {
                                        getFoodSpotHistoriesUseCase
                                            .invoke(
                                                userId = userId,
                                                count = LOAD_DATA_NUMBER,
                                            ).contents
                                            .firstOrNull()
                                    }.getOrElse {
                                        if (it is UserFoodSpotException.NoMoreFoodSpotException) {
                                            null
                                        } else {
                                            throw it
                                        }
                                    }
                                }
                            val writtenReviewDeferred =
                                async {
                                    runCatching {
                                        getUserReviewsUseCase
                                            .invoke(
                                                userId = userId,
                                                count = LOAD_DATA_NUMBER,
                                            ).firstOrNull()
                                    }.getOrElse {
                                        if (it is UserReviewException.NoMoreReviewException) {
                                            null
                                        } else {
                                            throw it
                                        }
                                    }
                                }
                            val statisticsDeferred =
                                async {
                                    getUserStatisticsUseCase
                                        .invoke(userId)
                                }

                            val userInfo = userInfoDeferred.await()
                            val reportedFoodSpot = reportedFoodSpotDeferred.await()
                            val writtenReview = writtenReviewDeferred.await()
                            val statistics = statisticsDeferred.await()

                            reduce {
                                state.copy(
                                    userId = userInfo.userId,
                                    nickname = userInfo.nickname,
                                    profileImage = userInfo.profileImage,
                                    coin = userInfo.coin,
                                    badge = userInfo.badge,
                                    restDailyReportCreationCount = userInfo.restDailyReportCreationCount,
                                    myRanking = userInfo.myRanking,
                                    foodSpotHistory = reportedFoodSpot,
                                    foodSpotCount = statistics.reportCount,
                                    review =
                                        writtenReview?.let {
                                            Review(
                                                reviewId = writtenReview.id,
                                                userId = userInfo.userId,
                                                profileImage = userInfo.profileImage,
                                                nickname = userInfo.nickname,
                                                date = writtenReview.createdAt,
                                                rating = writtenReview.rating.toFloat(),
                                                reviewImages = writtenReview.photos.map { it.image },
                                                contents = writtenReview.contents,
                                            )
                                        },
                                    reviewCount = statistics.reviewCount,
                                    likeCount = statistics.likeCount,
                                    isLoading = false,
                                )
                            }
                        }.onFailure {
                            postSideEffect(MyPageSideEffect.ShowToastMessage("네트워크 오류가 발생했습니다."))
                            postSideEffect(MyPageSideEffect.NavToBack)
                        }
                    }
                }

                is MyPageIntent.SetLogoutDialogShownState -> {
                    reduce {
                        state.copy(
                            isLogoutDialogShown = isShown,
                        )
                    }
                }

                is MyPageIntent.SetWithdrawDialogShownState -> {
                    reduce {
                        state.copy(
                            isWithdrawDialogShown = isShown,
                        )
                    }
                }

                MyPageIntent.Logout -> {
                    val result =
                        runCatching {
                            logoutUseCase()
                        }
                    if (result.isSuccess) {
                        postSideEffect(MyPageSideEffect.NavToLogin)
                    } else {
                        postSideEffect(MyPageSideEffect.ShowToastMessage("실패!"))
                    }
                }

                MyPageIntent.Withdraw -> {
                    val result =
                        runCatching {
                            withdrawUseCase()
                        }
                    if (result.isSuccess) {
                        postSideEffect(MyPageSideEffect.NavToLogin)
                    } else {
                        postSideEffect(MyPageSideEffect.ShowToastMessage("실패!"))
                    }
                }

                MyPageIntent.NavToBack -> {
                    postSideEffect(MyPageSideEffect.NavToBack)
                }

                MyPageIntent.NavToReviewHistory -> {
                    postSideEffect(
                        MyPageSideEffect.NavToReviewHistory(
                            UserInfoDTO(
                                userId = state.userId,
                                nickname = state.nickname,
                                profileImage = state.profileImage,
                            ),
                        ),
                    )
                }

                MyPageIntent.NavToFoodSpotHistory -> {
                    postSideEffect(MyPageSideEffect.NavToFoodSpotHistory(state.userId))
                }

                is MyPageIntent.NavToFoodSpotDetail -> {
                    postSideEffect(MyPageSideEffect.NavToFoodSpotDetail(foodSpotId))
                }

                MyPageIntent.NavToUserInfoEdit -> {
                    postSideEffect(
                        MyPageSideEffect.NavToUserInfoEdit(
                            UserInfoDTO(
                                userId = state.userId,
                                nickname = state.nickname,
                                profileImage = state.profileImage,
                            ),
                        ),
                    )
                }

                is MyPageIntent.SetRankingDialogShownState -> {
                    reduce {
                        state.copy(
                            isRankingDialogShown = isShown,
                        )
                    }
                }
            }
        }

    companion object {
        private const val LOAD_DATA_NUMBER = 1
    }
}

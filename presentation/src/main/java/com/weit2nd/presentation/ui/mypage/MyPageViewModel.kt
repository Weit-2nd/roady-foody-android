package com.weit2nd.presentation.ui.mypage

import com.weit2nd.domain.usecase.logout.LogoutUseCase
import com.weit2nd.domain.usecase.logout.WithdrawUseCase
import com.weit2nd.domain.usecase.spot.GetFoodSpotHistoriesUseCase
import com.weit2nd.domain.usecase.user.GetMyUserInfoUseCase
import com.weit2nd.domain.usecase.user.GetUserReviewsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.navigation.dto.ReviewHistoryDTO
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
    private val getMyUserInfoUseCase: GetMyUserInfoUseCase,
    private val getFoodSpotHistoriesUseCase: GetFoodSpotHistoriesUseCase,
    private val getUserReviewsUseCase: GetUserReviewsUseCase,
) : BaseViewModel<MyPageState, MyPageSideEffect>() {
    override val container: Container<MyPageState, MyPageSideEffect> = container(MyPageState())

    fun onCreate() {
        MyPageIntent.GetMyUserInfo.post()
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
                            val userInfo = getMyUserInfoUseCase.invoke()
                            val reportedFoodSpotDeferred =
                                async {
                                    getFoodSpotHistoriesUseCase
                                        .invoke(
                                            userId = userInfo.userId,
                                            count = LOAD_DATA_NUMBER,
                                        ).contents
                                        .firstOrNull()
                                }
                            val writtenReviewDeferred =
                                async {
                                    getUserReviewsUseCase
                                        .invoke(
                                            userId = userInfo.userId,
                                            count = LOAD_DATA_NUMBER,
                                        ).firstOrNull()
                                }

                            val reportedFoodSpot = reportedFoodSpotDeferred.await()
                            val writtenReview = writtenReviewDeferred.await()

                            reduce {
                                state.copy(
                                    userId = userInfo.userId,
                                    nickname = userInfo.nickname,
                                    profileImage = userInfo.profileImage,
                                    coin = userInfo.coin,
                                    foodSpotHistory = reportedFoodSpot,
                                    review =
                                        writtenReview?.let {
                                            Review(
                                                writtenReview.id,
                                                userInfo.userId,
                                                userInfo.profileImage,
                                                userInfo.nickname,
                                                writtenReview.createdAt,
                                                writtenReview.rating.toFloat(),
                                                writtenReview.photos.map { it.image },
                                                writtenReview.contents,
                                            )
                                        },
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
                            ReviewHistoryDTO(
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
            }
        }

    companion object {
        private const val LOAD_DATA_NUMBER = 1
    }
}

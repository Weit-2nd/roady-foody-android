package com.weit2nd.presentation.ui.mypage

import com.weit2nd.domain.usecase.logout.LogoutUseCase
import com.weit2nd.domain.usecase.logout.WithdrawUseCase
import com.weit2nd.domain.usecase.user.GetMyUserInfoUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.dto.ReviewHistoryDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
    private val withdrawUseCase: WithdrawUseCase,
    private val getMyUserInfoUseCase: GetMyUserInfoUseCase,
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

    private fun MyPageIntent.post() =
        intent {
            when (this@post) {
                MyPageIntent.GetMyUserInfo -> {
                    runCatching {
                        val userInfo = getMyUserInfoUseCase.invoke()
                        reduce {
                            state.copy(
                                userId = userInfo.userId,
                                nickname = userInfo.nickname,
                                profileImage = userInfo.profileImage,
                                coin = userInfo.coin,
                            )
                        }
                    }.onFailure { postSideEffect(MyPageSideEffect.ShowToastMessage("네트워크 오류가 발생했습니다.")) }
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
            }
        }
}

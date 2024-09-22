package com.weit2nd.presentation.ui.mypage.userinfoEdit

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.weit2nd.domain.exception.imageuri.NotImageException
import com.weit2nd.domain.usecase.pickimage.PickSingleImageUseCase
import com.weit2nd.domain.usecase.signup.CheckNicknameDuplicateUseCase
import com.weit2nd.domain.usecase.signup.VerifyNicknameUseCase
import com.weit2nd.domain.usecase.user.EditUserInfoUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class UserInfoEditViewModel @Inject constructor(
    private val editUserInfoUseCase: EditUserInfoUseCase,
    private val verifyNicknameUseCase: VerifyNicknameUseCase,
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
    private val pickSingleImageUseCase: PickSingleImageUseCase,
) : BaseViewModel<UserInfoEditState, UserInfoEditSideEffect>() {
    override val container: Container<UserInfoEditState, UserInfoEditSideEffect> =
        container(UserInfoEditState())

    private var nicknameDuplicateCheckJob: Job = Job().apply { complete() }

    fun onEditButtonClick() {
        UserInfoEditIntent.EditUserInfo.post()
    }

    fun onBackButtonClick() {
        UserInfoEditIntent.NavToBack.post()
    }

    fun onProfileImageClick() {
        UserInfoEditIntent.ChangeProfileImage.post()
    }

    fun onNicknameInputValueChange(nickname: String) {
        if (container.stateFlow.value.nickname != nickname) {
            nicknameDuplicateCheckJob.cancel()
            UserInfoEditIntent.VerifyNickname(nickname).post()
        }
    }

    fun onDuplicationBtnClick() {
        UserInfoEditIntent.CheckNicknameDuplication(container.stateFlow.value.nickname).post()
    }

    private fun setNicknameCheckingLoadingState() {
        UserInfoEditIntent
            .SetNicknameCheckingLoadingState(
                container.stateFlow.value.isNicknameCheckingLoading
                    .not(),
            ).post()
    }

    private fun UserInfoEditIntent.post() =
        intent {
            when (this@post) {
                UserInfoEditIntent.EditUserInfo -> {
                    reduce {
                        state.copy(
                            isLoading = true,
                        )
                    }
                    runCatching {
                        state.apply {
                            editUserInfoUseCase.invoke(
                                profileImage = profileImageUri?.toString(),
                                nickname = nickname,
                            )
                        }
                    }.onSuccess {
                        postSideEffect(UserInfoEditSideEffect.NavToBack)
                    }.onFailure { throwable ->
                        if (throwable is NotImageException) {
                            postSideEffect(UserInfoEditSideEffect.ShowToast("업로드된 파일이 이미지 형식이 아닙니다."))
                        } else {
                            throwable.message?.let {
                                postSideEffect(
                                    UserInfoEditSideEffect.ShowToast(
                                        it,
                                    ),
                                )
                            }
                            reduce {
                                state.copy(
                                    isLoading = false,
                                )
                            }
                        }
                    }
                }

                UserInfoEditIntent.ChangeProfileImage -> {
                    val result = pickSingleImageUseCase.invoke()
                    if (result != null) {
                        reduce {
                            state.copy(
                                profileImageUri = result.toUri(),
                            )
                        }
                    }
                }

                is UserInfoEditIntent.VerifyNickname -> {
                    val nicknameState = verifyNicknameUseCase.invoke(nickname)
                    reduce {
                        state.copy(
                            nickname = nickname,
                            nicknameState = nicknameState,
                        )
                    }
                }

                is UserInfoEditIntent.CheckNicknameDuplication -> {
                    setNicknameCheckingLoadingState()
                    nicknameDuplicateCheckJob =
                        viewModelScope
                            .launch {
                                runCatching {
                                    val nicknameState =
                                        checkNicknameDuplicateUseCase.invoke(nickname)
                                    reduce {
                                        state.copy(
                                            nicknameState = nicknameState,
                                        )
                                    }
                                }
                            }.apply {
                                invokeOnCompletion {
                                    setNicknameCheckingLoadingState()
                                }
                            }
                }

                is UserInfoEditIntent.SetNicknameCheckingLoadingState -> {
                    reduce {
                        state.copy(
                            isNicknameCheckingLoading = isLoading,
                        )
                    }
                }

                UserInfoEditIntent.NavToBack -> {
                    postSideEffect(UserInfoEditSideEffect.NavToBack)
                }
            }
        }
}

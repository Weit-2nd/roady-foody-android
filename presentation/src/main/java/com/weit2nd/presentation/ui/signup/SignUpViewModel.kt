package com.weit2nd.presentation.ui.signup

import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.weit2nd.domain.exception.imageuri.NotImageException
import com.weit2nd.domain.model.User
import com.weit2nd.domain.usecase.pickimage.PickSingleImageUseCase
import com.weit2nd.domain.usecase.signup.CheckNicknameDuplicateUseCase
import com.weit2nd.domain.usecase.signup.SignUpUseCase
import com.weit2nd.domain.usecase.signup.VerifyNicknameUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.SignUpNavRoutes
import com.weit2nd.presentation.navigation.dto.TermIdsDTO
import com.weit2nd.presentation.navigation.dto.toTermIds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val verifyNicknameUseCase: VerifyNicknameUseCase,
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
    private val pickSingleImageUseCase: PickSingleImageUseCase,
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpState, SignUpSideEffect>() {
    private val agreedTermIds by lazy {
        checkNotNull(
            savedStateHandle
                .get<TermIdsDTO>(SignUpNavRoutes.TERM_IDS)
                ?.toTermIds(),
        )
    }
    override val container = container<SignUpState, SignUpSideEffect>(SignUpState())
    private var nicknameDuplicateCheckJob: Job = Job().apply { complete() }

    fun onSignUpButtonClick() {
        SignUpIntent.RequestSignUp.post()
    }

    fun onProfileImageClick() {
        SignUpIntent.ChangeProfileImage.post()
    }

    fun onNicknameInputValueChange(nickname: String) {
        if (container.stateFlow.value.nickname != nickname) {
            nicknameDuplicateCheckJob.cancel()
            SignUpIntent.VerifyNickname(nickname).post()
        }
    }

    fun onDuplicationBtnClick() {
        SignUpIntent.CheckNicknameDuplication(container.stateFlow.value.nickname).post()
    }

    private fun setLoadingState() {
        SignUpIntent
            .SetLoadingState(
                container.stateFlow.value.isLoading
                    .not(),
            ).post()
    }

    private fun SignUpIntent.post() =
        intent {
            when (this@post) {
                SignUpIntent.RequestSignUp -> {
                    runCatching {
                        state.apply {
                            signUpUseCase.invoke(
                                image = (profileImageUri ?: "").toString(),
                                nickname = nickname,
                                agreedTermIds = agreedTermIds,
                            )
                        }
                    }.onSuccess {
                        postSideEffect(SignUpSideEffect.NavToHome(User("으악")))
                    }.onFailure { throwable ->
                        if (throwable is NotImageException) {
                            postSideEffect(SignUpSideEffect.ShowToast("업로드된 파일이 이미지 형식이 아닙니다."))
                        } else {
                            throwable.message?.let { postSideEffect(SignUpSideEffect.ShowToast(it)) }
                        }
                    }
                }

                SignUpIntent.ChangeProfileImage -> {
                    val result = pickSingleImageUseCase.invoke()
                    if (result != null) {
                        reduce {
                            state.copy(
                                profileImageUri = result.toUri(),
                            )
                        }
                    }
                }

                is SignUpIntent.VerifyNickname -> {
                    val nicknameState = verifyNicknameUseCase.invoke(nickname)
                    reduce {
                        state.copy(
                            nickname = nickname,
                            nicknameState = nicknameState,
                        )
                    }
                }

                is SignUpIntent.CheckNicknameDuplication -> {
                    setLoadingState()
                    nicknameDuplicateCheckJob =
                        viewModelScope
                            .launch {
                                runCatching {
                                    val nicknameState = checkNicknameDuplicateUseCase.invoke(nickname)
                                    reduce {
                                        state.copy(
                                            nicknameState = nicknameState,
                                        )
                                    }
                                }
                            }.apply {
                                invokeOnCompletion {
                                    setLoadingState()
                                }
                            }
                }

                is SignUpIntent.SetLoadingState -> {
                    reduce {
                        state.copy(
                            isLoading = isLoading,
                        )
                    }
                }
            }
        }
}

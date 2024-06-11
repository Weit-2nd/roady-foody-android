package com.weit2nd.presentation.ui.signup

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.weit2nd.domain.model.User
import com.weit2nd.domain.usecase.signup.CheckNicknameDuplicateUseCase
import com.weit2nd.domain.usecase.signup.VerifyNicknameUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val verifyNicknameUseCase: VerifyNicknameUseCase,
    private val checkNicknameDuplicateUseCase: CheckNicknameDuplicateUseCase,
) : BaseViewModel<SignUpState, SignUpSideEffect>() {

    override val container = container<SignUpState, SignUpSideEffect>(SignUpState())
    private var nicknameDuplicateCheckJob: Job = Job().apply { complete() }

    fun onSignUpButtonClick() {
        SignUpIntent.RequestSignUp.post()
    }

    fun onProfileImageClick() {
        SignUpIntent.ShowImagePicker.post()
    }

    fun onProfileImageChoose(imageUri: Uri?) {
        SignUpIntent.SetProfileImage(imageUri).post()
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

    private fun SignUpIntent.post() = intent {
        when (this@post) {
            SignUpIntent.RequestSignUp -> {
                runCatching {
                    // 회원가입 시도
                }.onSuccess {
                    postSideEffect(SignUpSideEffect.NavToHome(User("으악")))
                }.onFailure {
                    // 회원가입 실패 문구 띄우기
                }
            }

            SignUpIntent.ShowImagePicker -> {
                postSideEffect(SignUpSideEffect.ShowImagePicker)
            }

            is SignUpIntent.SetProfileImage -> {
                reduce {
                    state.copy(
                        profileImageUri = imageUri
                    )
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
                reduce {
                    state.copy(
                        isLoading = true,
                    )
                }
                nicknameDuplicateCheckJob = viewModelScope.launch {
                    runCatching {
                        val nicknameState = checkNicknameDuplicateUseCase.invoke(nickname)
                        reduce {
                            state.copy(
                                isLoading = false,
                                nicknameState = nicknameState,
                            )
                        }
                    }
                }.apply {
                    invokeOnCompletion {
                        intent {
                            reduce {
                                state.copy(
                                    isLoading = false,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

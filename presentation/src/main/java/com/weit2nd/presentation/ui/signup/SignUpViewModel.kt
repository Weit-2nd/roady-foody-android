package com.weit2nd.presentation.ui.signup

import android.net.Uri
import com.weit2nd.domain.model.User
import com.weit2nd.domain.usecase.signup.SignUpUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpState, SignUpSideEffect>() {

    override val container = container<SignUpState, SignUpSideEffect>(SignUpState())

    fun onSignUpButtonClick() {
        SignUpIntent.RequestSignUp.post()
    }

    fun onProfileImageClick(imageUri: Uri?) {
        SignUpIntent.SetProfileImage(imageUri).post()
    }

    fun onNicknameInputValueChange(nickname: String) {
        if (container.stateFlow.value.nickname != nickname) {
            SignUpIntent.VerifyNickname(nickname).post()
        }
    }

    fun onDuplicationBtnClick(nickname: String) {
        SignUpIntent.CheckNicknameDuplication(nickname).post()
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

            is SignUpIntent.SetProfileImage -> {
                reduce {
                    state.copy(
                        profileImageUri = imageUri
                    )
                }
            }

            is SignUpIntent.VerifyNickname -> {
                val nicknameState = signUpUseCase.verifyNickname(nickname)
                reduce {
                    state.copy(
                        nickname = nickname,
                        nicknameState = nicknameState,
                    )
                }
            }

            is SignUpIntent.CheckNicknameDuplication -> {
                runCatching {
                    val nicknameState = signUpUseCase.invoke(nickname)
                    reduce {
                        state.copy(
                            nicknameState = nicknameState
                        )
                    }
                }
            }
        }
    }
}

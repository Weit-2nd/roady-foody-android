package com.weit2nd.presentation.ui.signup

import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.weit2nd.domain.model.User
import com.weit2nd.domain.usecase.pickimage.PickSingleImageUseCase
import com.weit2nd.domain.usecase.signup.CheckNicknameDuplicateUseCase
import com.weit2nd.domain.usecase.signup.SignUpUseCase
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
    private val pickSingleImageUseCase: PickSingleImageUseCase,
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpState, SignUpSideEffect>() {

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
        SignUpIntent.SetLoadingState(container.stateFlow.value.isLoading.not()).post()
    }

    private fun SignUpIntent.post() = intent {
        when (this@post) {
            SignUpIntent.RequestSignUp -> {
                runCatching {
                    container.stateFlow.value.apply {
                        signUpUseCase.invoke(
                            image = (profileImageUri ?: "").toString(),
                            nickname = nickname,
                            agreedTermIds = listOf(1, 2, 3), // todo 약관 화면과 연결하며 수정
                        )
                    }
                }.onSuccess {
                    postSideEffect(SignUpSideEffect.NavToHome(User("으악")))
                }.onFailure { throwable ->
                    throwable.message?.let { postSideEffect(SignUpSideEffect.ShowToast(it)) }
                }
            }

            SignUpIntent.ChangeProfileImage -> {
                val result = pickSingleImageUseCase.invoke()
                if (result != null) {
                    reduce {
                        state.copy(
                            profileImageUri = result.toUri()
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
                nicknameDuplicateCheckJob = viewModelScope.launch {
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

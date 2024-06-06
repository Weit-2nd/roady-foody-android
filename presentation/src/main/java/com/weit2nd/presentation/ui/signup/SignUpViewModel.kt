package com.weit2nd.presentation.ui.signup

import androidx.lifecycle.SavedStateHandle
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.SignUpNavRoutes
import com.weit2nd.presentation.navigation.dto.UserDTO
import com.weit2nd.presentation.navigation.dto.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<SignUpState, SignUpSideEffect>() {

    override val container = container<SignUpState, SignUpSideEffect>(
        SignUpState(
            user = checkNotNull(
                savedStateHandle.get<UserDTO>(SignUpNavRoutes.USER_STATE_KEY)?.toUser()
            )
        )
    )

    fun onSignUpButtonClick() {
        SignUpIntent.RequestSignUp.post()
    }

    private fun SignUpIntent.post() = intent {
        when (this@post) {
            SignUpIntent.RequestSignUp -> {
                runCatching {
                    // 회원가입 시도
                }.onSuccess {
                    postSideEffect(SignUpSideEffect.NavToHome(container.stateFlow.value.user))
                }.onFailure {
                    // 회원가입 실패 문구 띄우기
                }
            }
        }
    }
}

package com.weit2nd.presentation.ui.login

import android.util.Log
import com.weit2nd.domain.usecase.login.LoginWithKakaoUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
): BaseViewModel<LoginState, LoginSideEffect>() {

    override val container = container<LoginState, LoginSideEffect>(LoginState())

    fun onLoginButtonClick() {
        LoginIntent.RequestLogin.post()
    }

    private fun LoginIntent.post() = intent {
        when (this@post) {
            LoginIntent.RequestLogin -> {
                reduce {
                    state.copy(
                        isLoading = true,
                    )
                }
                // TODO 로그인 UseCase는 Result로 반환되므로 runCatching 제거
                // TODO 로그인 성공시 메인 화면으로 이동
                // TODO UserNotFoundException이면 회원가입으로 이동
                runCatching {
                    loginWithKakaoUseCase.invoke().getOrThrow()
                }.onSuccess {
                    postSideEffect(LoginSideEffect.NavToSignUp)
                }.onFailure {
                    Log.e("MainTest", "$it")
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

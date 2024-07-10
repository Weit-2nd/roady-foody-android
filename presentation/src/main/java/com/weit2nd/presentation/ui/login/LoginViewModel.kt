package com.weit2nd.presentation.ui.login

import android.util.Log
import com.weit2nd.domain.exception.user.LoginException
import com.weit2nd.domain.model.User
import com.weit2nd.domain.usecase.login.LoginWithKakaoUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
) : BaseViewModel<LoginState, LoginSideEffect>() {
    override val container = container<LoginState, LoginSideEffect>(LoginState())

    fun onLoginButtonClick() {
        LoginIntent.RequestLogin.post()
    }

    private fun LoginIntent.post() =
        intent {
            when (this@post) {
                LoginIntent.RequestLogin -> {
                    reduce {
                        state.copy(
                            isLoading = true,
                        )
                    }

                    loginWithKakaoUseCase
                        .invoke()
                        .onSuccess { postSideEffect(LoginSideEffect.NavToHome(User("test"))) }
                        .onFailure { throwable ->
                            if (throwable is LoginException.UserNotFoundException) {
                                postSideEffect(LoginSideEffect.NavToTermAgreement)
                            } else {
                                Log.e("LoginError", "$throwable")
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

package com.weit2nd.presentation.ui.login

import com.weit2nd.domain.usecase.LoginUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
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
                runCatching {
                    val user = loginUseCase.invoke()
                    postSideEffect(LoginSideEffect.NavToHome(user))
                }.onFailure {
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

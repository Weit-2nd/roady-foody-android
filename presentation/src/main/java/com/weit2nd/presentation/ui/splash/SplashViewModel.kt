package com.weit2nd.presentation.ui.splash

import com.weit2nd.domain.usecase.login.LoginToServerUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginToServerUseCase: LoginToServerUseCase,
) : BaseViewModel<SplashState, SplashSideEffect>() {
    override val container = container<SplashState, SplashSideEffect>(SplashState())

    fun onCreate() {
        SplashIntent.RequestLogin.post()
    }

    private fun SplashIntent.post() =
        intent {
            when (this@post) {
                SplashIntent.RequestLogin -> {
                    loginToServerUseCase
                        .invoke()
                        .onSuccess { postSideEffect(SplashSideEffect.NavToHome) }
                        .onFailure { postSideEffect(SplashSideEffect.NavToLogin) }
                }
            }
        }
}

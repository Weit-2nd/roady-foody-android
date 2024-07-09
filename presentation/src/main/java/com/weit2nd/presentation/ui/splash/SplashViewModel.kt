package com.weit2nd.presentation.ui.splash

import com.weit2nd.domain.usecase.login.LoginWithKakaoUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginWithKakaoUseCase: LoginWithKakaoUseCase,
) : BaseViewModel<SplashState, SplashSideEffect>() {
    override val container = container<SplashState, SplashSideEffect>(SplashState())

    fun onCreate() {
        SplashIntent.RequestLogin.post()
    }

    private fun SplashIntent.post() = intent {
        when (this@post) {
            SplashIntent.RequestLogin -> {
                loginWithKakaoUseCase.invoke()
                    .onSuccess { postSideEffect(SplashSideEffect.NavToHome) }
                    .onFailure { postSideEffect(SplashSideEffect.NavToLogin) }
            }
        }
    }
}

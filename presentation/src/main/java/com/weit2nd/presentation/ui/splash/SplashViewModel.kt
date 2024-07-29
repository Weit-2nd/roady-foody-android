package com.weit2nd.presentation.ui.splash

import com.weit2nd.domain.exception.token.TokenState
import com.weit2nd.domain.usecase.login.GetTokenStateUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getTokenStateUseCase: GetTokenStateUseCase,
) : BaseViewModel<SplashState, SplashSideEffect>() {
    override val container = container<SplashState, SplashSideEffect>(SplashState())

    fun onCreate() {
        SplashIntent.RequestLogin.post()
    }

    private fun SplashIntent.post() =
        intent {
            when (this@post) {
                SplashIntent.RequestLogin -> {
                    when (getTokenStateUseCase.invoke()) {
                        TokenState.AccessTokenValid -> {
                            postSideEffect(SplashSideEffect.NavToHome)
                        }

                        TokenState.RefreshTokenInvalid -> {
                            postSideEffect(SplashSideEffect.NavToLogin)
                        }

                        TokenState.FailGettingToken -> {
                            postSideEffect(SplashSideEffect.ShowToast("네트워크 오류가 발생했습니다."))
                            postSideEffect(SplashSideEffect.NavToLogin)
                        }
                    }
                }
            }
        }
}

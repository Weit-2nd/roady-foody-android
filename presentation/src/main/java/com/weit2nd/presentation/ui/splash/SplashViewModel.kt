package com.weit2nd.presentation.ui.splash

import com.weit2nd.domain.exception.UnknownException
import com.weit2nd.domain.usecase.login.LoginUseCase
import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<SplashState, SplashSideEffect>() {
    override val container = container<SplashState, SplashSideEffect>(SplashState())

    fun onCreate() {
        SplashIntent.RequestLogin.post()
    }

    private fun SplashIntent.post() =
        intent {
            when (this@post) {
                SplashIntent.RequestLogin -> {
                    loginUseCase
                        .invoke()
                        .onSuccess { postSideEffect(SplashSideEffect.NavToHome) }
                        .onFailure { throwable ->
                            if (throwable is UnknownException) {
                                postSideEffect(SplashSideEffect.ShowToast("알 수 없는 오류가 발생했습니다."))
                            }
                            postSideEffect(SplashSideEffect.NavToLogin)
                        }
                }
            }
        }
}

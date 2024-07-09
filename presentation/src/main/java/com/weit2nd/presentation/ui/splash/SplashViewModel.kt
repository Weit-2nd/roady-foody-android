package com.weit2nd.presentation.ui.splash

import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel<SplashState, SplashSideEffect>() {
    override val container = container<SplashState, SplashSideEffect>(SplashState())

    fun onSplashEnd() {
        SplashIntent.NavToLogin.post()  // 임시 이동
    }

    private fun SplashIntent.post() = intent {
        when (this@post) {
            SplashIntent.NavToHome -> {
                postSideEffect(SplashSideEffect.NavToHome)
            }
            SplashIntent.NavToLogin -> {
                postSideEffect(SplashSideEffect.NavToLogin)
            }
        }
    }
}

package com.weit2nd.roadyfoody

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.weit2nd.presentation.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HiltApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }
}

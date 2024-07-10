package com.weit2nd.roadyfoody

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.weit2nd.data.util.ActivityProvider
import com.weit2nd.presentation.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HiltApplication : Application() {
    @Inject
    lateinit var activityProvider: ActivityProvider

    override fun onCreate() {
        super.onCreate()
        activityProvider.start(this)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
    }

    override fun onTerminate() {
        super.onTerminate()
        activityProvider.stop(this)
    }
}

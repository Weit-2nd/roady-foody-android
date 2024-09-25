package com.weit2nd.roadyfoody

import android.app.Application
import com.weit2nd.data.util.ActivityProvider
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class HiltApplication : Application() {
    @Inject
    lateinit var activityProvider: ActivityProvider

    override fun onCreate() {
        super.onCreate()
        activityProvider.start(this)
    }

    override fun onTerminate() {
        super.onTerminate()
        activityProvider.stop(this)
    }
}

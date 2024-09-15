package com.weit2nd.data.di

import com.squareup.moshi.Moshi
import com.weit2nd.data.util.ActivityProvider
import com.weit2nd.data.util.JwtDecoder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UtilModule {
    @Singleton
    @Provides
    fun providesActivityProvider(): ActivityProvider {
        return ActivityProvider()
    }

    @Provides
    fun providesJwtDecoder(moshi: Moshi): JwtDecoder {
        return JwtDecoder(moshi)
    }
}

package com.weit2nd.data.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.weit2nd.data.BuildConfig
import com.weit2nd.data.R
import com.weit2nd.data.util.LocalDateTimeConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultNetwork

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthNetwork

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @DefaultNetwork
    @Singleton
    @Provides
    fun provideDefaultOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

    // TODO: 6/2/24 (minseonglove) 토큰 인터셉터 추가
    @AuthNetwork
    @Singleton
    @Provides
    fun provideAuthOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @DefaultNetwork
    @Singleton
    @Provides
    fun provideDefaultRetrofit(
        @ApplicationContext context: Context,
        @DefaultNetwork okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(context.getString(R.string.base_url))
            .build()
    }

    @AuthNetwork
    @Singleton
    @Provides
    fun provideAuthRetrofit(
        @ApplicationContext context: Context,
        @AuthNetwork okHttpClient: OkHttpClient,
        moshi: Moshi,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(context.getString(R.string.base_url))
            .build()
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return HttpLoggingInterceptor().setLevel(level)
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(LocalDateTimeConverter())
        .add(KotlinJsonAdapterFactory())
        .build()
}

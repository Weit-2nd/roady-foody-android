package com.weit2nd.data.di

import com.weit2nd.data.repository.signup.SignUpRepositoryImpl
import com.weit2nd.data.source.signup.SignUpDataSource
import com.weit2nd.domain.repository.signup.SignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SignUpModule {

    @Singleton
    @Provides
    fun providesSignUpRepository(
        signUpDataSource: SignUpDataSource,
    ): SignUpRepository {
        return SignUpRepositoryImpl(
            signUpDataSource = signUpDataSource,
        )
    }

    @Singleton
    @Provides
    fun providesSignUpDataSource(): SignUpDataSource {
        return SignUpDataSource()
    }
}

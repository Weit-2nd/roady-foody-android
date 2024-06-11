package com.weit2nd.data.di

import com.weit2nd.data.repository.signup.SignUpRepositoryImpl
import com.weit2nd.data.source.signup.SignUpDataSource
import com.weit2nd.domain.repository.signup.SignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SignUpModule {

    @ViewModelScoped
    @Provides
    fun providesSignUpRepository(
        signUpDataSource: SignUpDataSource,
    ): SignUpRepository {
        return SignUpRepositoryImpl(
            signUpDataSource = signUpDataSource,
        )
    }

    @ViewModelScoped
    @Provides
    fun providesSignUpDataSource(): SignUpDataSource {
        return SignUpDataSource()
    }
}

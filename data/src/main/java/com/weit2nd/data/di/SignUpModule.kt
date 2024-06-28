package com.weit2nd.data.di

import com.squareup.moshi.Moshi
import com.weit2nd.data.repository.signup.SignUpRepositoryImpl
import com.weit2nd.data.service.CheckNicknameService
import com.weit2nd.data.service.SignUpService
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.signup.SignUpDataSource
import com.weit2nd.domain.repository.signup.SignUpRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object SignUpModule {

    @ViewModelScoped
    @Provides
    fun providesSignUpRepository(
        signUpDataSource: SignUpDataSource,
        localImageDatasource: LocalImageDatasource,
        moshi: Moshi,
    ): SignUpRepository {
        return SignUpRepositoryImpl(
            signUpDataSource = signUpDataSource,
            localImageDatasource = localImageDatasource,
            moshi = moshi,
        )
    }

    @ViewModelScoped
    @Provides
    fun providesSignUpDataSource(
        signUpService: SignUpService,
        checkNicknameService: CheckNicknameService,
    ): SignUpDataSource {
        return SignUpDataSource(
            signUpService = signUpService,
            checkNicknameService = checkNicknameService,
        )
    }

    @Provides
    @ViewModelScoped
    fun providesUserRegistrationService(
        @AuthNetwork retrofit: Retrofit,
    ): SignUpService {
        return retrofit.create(SignUpService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun providesCheckNicknameService(
        @DefaultNetwork retrofit: Retrofit,
    ): CheckNicknameService {
        return retrofit.create(CheckNicknameService::class.java)
    }
}

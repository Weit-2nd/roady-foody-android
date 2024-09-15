package com.weit2nd.data.di

import com.weit2nd.data.repository.user.UserRepositoryImpl
import com.weit2nd.data.service.UserService
import com.weit2nd.data.source.token.TokenDataSource
import com.weit2nd.data.source.user.UserDataSource
import com.weit2nd.data.util.JwtDecoder
import com.weit2nd.domain.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object UserModule {
    @ViewModelScoped
    @Provides
    fun providesUserRepository(
        userDataSource: UserDataSource,
        tokenDataSource: TokenDataSource,
        jwtDecoder: JwtDecoder,
    ): UserRepository {
        return UserRepositoryImpl(
            userDataSource = userDataSource,
            tokenDataSource = tokenDataSource,
            jwtDecoder = jwtDecoder,
        )
    }

    @ViewModelScoped
    @Provides
    fun providesUserDataSource(service: UserService): UserDataSource {
        return UserDataSource(service)
    }

    @ViewModelScoped
    @Provides
    fun providesUserService(
        @AuthNetwork retrofit: Retrofit,
    ): UserService {
        return retrofit.create(UserService::class.java)
    }
}

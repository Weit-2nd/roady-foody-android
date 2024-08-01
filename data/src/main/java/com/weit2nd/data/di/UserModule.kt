package com.weit2nd.data.di

import com.weit2nd.data.repository.user.UserRepositoryImpl
import com.weit2nd.data.service.UserService
import com.weit2nd.data.source.user.UserDataSource
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
    fun providesUserRepository(dataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(dataSource)
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

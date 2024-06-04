package com.weit2nd.data.di

import com.weit2nd.data.repository.LoginRepositoryImpl
import com.weit2nd.data.source.LoginDataSource
import com.weit2nd.domain.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {

    @Singleton
    @Provides
    fun providesLoginRepository(
        loginDataSource: LoginDataSource,
    ): LoginRepository {
        return LoginRepositoryImpl(
            loginDataSource = loginDataSource,
        )
    }

    @Singleton
    @Provides
    fun providesLoginDataSource(): LoginDataSource {
        return LoginDataSource()
    }
}

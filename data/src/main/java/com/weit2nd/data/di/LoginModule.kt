package com.weit2nd.data.di

import com.weit2nd.data.repository.login.LoginRepositoryImpl
import com.weit2nd.data.source.login.LoginDataSource
import com.weit2nd.data.util.ActivityProvider
import com.weit2nd.domain.repository.login.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {

    @Singleton
    @Provides
    fun providesLoginRepository(
        loginDataSource: LoginDataSource,
        activityProvider: ActivityProvider,
    ): LoginRepository {
        return LoginRepositoryImpl(
            loginDataSource = loginDataSource,
            activityProvider = activityProvider,
        )
    }

    @Singleton
    @Provides
    fun providesLoginDataSource(): LoginDataSource {
        return LoginDataSource()
    }
}

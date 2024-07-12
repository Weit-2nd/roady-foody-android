package com.weit2nd.data.di

import com.weit2nd.data.repository.logout.LogoutRepositoryImpl
import com.weit2nd.data.service.LogoutService
import com.weit2nd.data.source.logout.LogoutDataSource
import com.weit2nd.domain.repository.logout.LogoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object LogoutModule {
    @ViewModelScoped
    @Provides
    fun providesLogoutRepository(logoutDataSource: LogoutDataSource): LogoutRepository {
        return LogoutRepositoryImpl(
            logoutDataSource = logoutDataSource,
        )
    }

    @ViewModelScoped
    @Provides
    fun providesLogoutDataSource(service: LogoutService): LogoutDataSource {
        return LogoutDataSource(
            service = service,
        )
    }

    @ViewModelScoped
    @Provides
    fun providesLogoutService(
        @AuthNetwork retrofit: Retrofit,
    ): LogoutService {
        return retrofit.create(LogoutService::class.java)
    }
}

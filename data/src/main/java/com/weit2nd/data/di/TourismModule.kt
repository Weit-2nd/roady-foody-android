package com.weit2nd.data.di

import com.weit2nd.data.repository.tourism.TourismRepositoryImpl
import com.weit2nd.data.service.TourismService
import com.weit2nd.data.source.toursim.TourismDataSource
import com.weit2nd.domain.repository.tourism.TourismRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object TourismModule {

    @ViewModelScoped
    @Provides
    fun providesTourismRepository(
        dataSource: TourismDataSource,
    ): TourismRepository {
        return TourismRepositoryImpl(dataSource)
    }

    @ViewModelScoped
    @Provides
    fun providesTourismDataSource(
        service: TourismService,
    ): TourismDataSource {
        return TourismDataSource(service)
    }

    @ViewModelScoped
    @Provides
    fun providesTourismService(
        @AuthNetwork retrofit: Retrofit,
    ): TourismService {
        return retrofit.create(TourismService::class.java)
    }
}

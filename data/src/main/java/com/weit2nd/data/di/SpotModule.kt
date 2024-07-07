package com.weit2nd.data.di

import com.squareup.moshi.Moshi
import com.weit2nd.data.repository.spot.FoodSpotRepositoryImpl
import com.weit2nd.data.service.SpotService
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.spot.FoodSpotDataSource
import com.weit2nd.domain.repository.spot.FoodSpotRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object SpotModule {

    @Provides
    @ViewModelScoped
    fun providesFoodSpotRepository(
        foodSpotDataSource: FoodSpotDataSource,
        localImageDatasource: LocalImageDatasource,
        moshi: Moshi,
    ): FoodSpotRepository {
        return FoodSpotRepositoryImpl(
            foodSpotDataSource = foodSpotDataSource,
            localImageDatasource = localImageDatasource,
            moshi = moshi,
        )
    }

    @Provides
    @ViewModelScoped
    fun providesFoodSpotDataSource(
        service: SpotService,
    ): FoodSpotDataSource {
        return FoodSpotDataSource(service)
    }

    @Provides
    @ViewModelScoped
    fun providesFoodSpotService(
        @AuthNetwork retrofit: Retrofit,
    ): SpotService {
        return retrofit.create(SpotService::class.java)
    }
}

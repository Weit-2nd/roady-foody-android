package com.weit2nd.data.di

import com.weit2nd.data.repository.RestaurantRepositoryImpl
import com.weit2nd.data.source.RestaurantDataSource
import com.weit2nd.domain.repository.RestaurantRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RestaurantHiltModule {

    @Singleton
    @Provides
    fun providesRestaurantRepository(
        restaurantDataSource: RestaurantDataSource,
    ): RestaurantRepository {
        return RestaurantRepositoryImpl(
            restaurantDataSource = restaurantDataSource,
        )
    }

    @Singleton
    @Provides
    fun providesRestaurantDataSource(): RestaurantDataSource {
        return RestaurantDataSource()
    }
}

package com.weit2nd.data.di

import com.weit2nd.data.repository.LoginRepositoryImpl
import com.weit2nd.data.repository.RestaurantRepositoryImpl
import com.weit2nd.data.source.LoginDataSource
import com.weit2nd.data.source.RestaurantDataSource
import com.weit2nd.domain.repository.LoginRepository
import com.weit2nd.domain.repository.RestaurantRepository
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

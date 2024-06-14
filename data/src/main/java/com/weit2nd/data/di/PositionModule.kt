package com.weit2nd.data.di

import android.content.Context
import com.google.android.gms.location.LocationServices
import com.weit2nd.data.repository.position.CurrentPositionRepositoryImpl
import com.weit2nd.domain.repository.position.CurrentPositionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PositionModule {

    @ViewModelScoped
    @Provides
    fun provideCurrentPositionRepository(
        @ApplicationContext context: Context,
    ): CurrentPositionRepository {
        return CurrentPositionRepositoryImpl(
            locationClient = LocationServices.getFusedLocationProviderClient(context),
        )
    }
}

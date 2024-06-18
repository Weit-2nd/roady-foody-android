package com.weit2nd.data.di

import com.weit2nd.data.repository.searchlocation.SearchLocationRepositoryImpl
import com.weit2nd.data.source.searchlocation.SearchLocationDataSource
import com.weit2nd.domain.repository.searchlocation.SearchLocationRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SearchLocationModule {

    @ViewModelScoped
    @Provides
    fun providesSearchLocationRepository(
        searchLocationDataSource: SearchLocationDataSource,
    ): SearchLocationRepository {
        return SearchLocationRepositoryImpl(searchLocationDataSource)
    }

    @ViewModelScoped
    @Provides
    fun providesSearchLocationDatasource(): SearchLocationDataSource {
        return SearchLocationDataSource()
    }
}

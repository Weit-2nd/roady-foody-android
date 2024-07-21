package com.weit2nd.data.di

import com.weit2nd.data.repository.search.SearchFoodSpotsRepositoryImpl
import com.weit2nd.data.service.SearchService
import com.weit2nd.data.source.search.SearchFoodSpotsDataSource
import com.weit2nd.domain.repository.search.SearchFoodSpotsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SearchFoodSpotsModule {
    @ViewModelScoped
    @Provides
    fun providesSearchFoodSpotsRepository(searchFoodSpotsDataSource: SearchFoodSpotsDataSource): SearchFoodSpotsRepository {
        return SearchFoodSpotsRepositoryImpl(
            searchFoodSpotsDataSource = searchFoodSpotsDataSource,
        )
    }

    @ViewModelScoped
    @Provides
    fun providesSearchFoodSpotsDataSource(service: SearchService): SearchFoodSpotsDataSource {
        return SearchFoodSpotsDataSource(service)
    }
}

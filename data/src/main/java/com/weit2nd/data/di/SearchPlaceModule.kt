package com.weit2nd.data.di

import com.weit2nd.data.repository.search.SearchPlaceRepositoryImpl
import com.weit2nd.data.service.SearchService
import com.weit2nd.data.source.search.SearchPlaceDataSource
import com.weit2nd.domain.repository.search.SearchPlaceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object SearchPlaceModule {

    @ViewModelScoped
    @Provides
    fun providesSearchPlaceRepository(
        searchLocationDataSource: SearchPlaceDataSource,
    ): SearchPlaceRepository {
        return SearchPlaceRepositoryImpl(searchLocationDataSource)
    }

    @ViewModelScoped
    @Provides
    fun providesSearchPlaceDatasource(
        service: SearchService,
    ): SearchPlaceDataSource {
        return SearchPlaceDataSource(service)
    }

    @ViewModelScoped
    @Provides
    fun providesSearchService(
        @AuthNetwork retrofit: Retrofit,
    ): SearchService {
        return retrofit.create(SearchService::class.java)
    }
}

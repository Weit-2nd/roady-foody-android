package com.weit2nd.data.di

import android.content.Context
import com.weit2nd.data.repository.search.SearchHistoryRepositoryImpl
import com.weit2nd.data.source.search.SearchHistoryDataSource
import com.weit2nd.domain.repository.searchhistory.SearchHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object SearchHistoryModule {
    @ViewModelScoped
    @Provides
    fun providesSearchHistoryRepository(dataSource: SearchHistoryDataSource): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(dataSource)
    }

    @ViewModelScoped
    @Provides
    fun providesSearchHistoryDataSource(
        @ApplicationContext context: Context,
    ): SearchHistoryDataSource {
        return SearchHistoryDataSource(context)
    }
}

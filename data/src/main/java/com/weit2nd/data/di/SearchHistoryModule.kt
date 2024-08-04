package com.weit2nd.data.di

import android.content.Context
import com.weit2nd.data.repository.search.SearchHistoryRepositoryImpl
import com.weit2nd.data.source.search.SearchHistoryDataSource
import com.weit2nd.domain.repository.searchhistory.SearchHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SearchHistoryModule {
    @Singleton
    @Provides
    fun providesSearchHistoryRepository(dataSource: SearchHistoryDataSource): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(dataSource)
    }

    @Singleton
    @Provides
    fun providesSearchHistoryDataSource(
        @ApplicationContext context: Context,
    ): SearchHistoryDataSource {
        return SearchHistoryDataSource(context)
    }
}

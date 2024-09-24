package com.weit2nd.data.di

import com.weit2nd.data.repository.ranking.RankingRepositoryImpl
import com.weit2nd.data.service.RankingService
import com.weit2nd.data.source.ranking.RankingDataSource
import com.weit2nd.domain.repository.ranking.RankingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object RankingModule {
    @ViewModelScoped
    @Provides
    fun providesRankingRepository(dataSource: RankingDataSource): RankingRepository {
        return RankingRepositoryImpl(dataSource)
    }

    @ViewModelScoped
    @Provides
    fun providesRankingDatasource(service: RankingService): RankingDataSource {
        return RankingDataSource(service)
    }

    @ViewModelScoped
    @Provides
    fun providesRankingService(
        @AuthNetwork retrofit: Retrofit,
    ): RankingService {
        return retrofit.create(RankingService::class.java)
    }
}

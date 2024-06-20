package com.weit2nd.data.di

import com.weit2nd.data.repository.terms.TermsRepositoryImpl
import com.weit2nd.data.service.TermsService
import com.weit2nd.data.source.TermsDataSource
import com.weit2nd.domain.repository.terms.TermsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object TermsModule {

    @ViewModelScoped
    @Provides
    fun providesTermsRepository(
        dataSource: TermsDataSource,
    ): TermsRepository {
        return TermsRepositoryImpl(dataSource)
    }

    @ViewModelScoped
    @Provides
    fun providesTermsDataSource(
        service: TermsService,
    ): TermsDataSource {
        return TermsDataSource(service)
    }

    @ViewModelScoped
    @Provides
    fun providesTermsService(
        @AuthNetwork retrofit: Retrofit,
    ): TermsService {
        return retrofit.create(TermsService::class.java)
    }
}

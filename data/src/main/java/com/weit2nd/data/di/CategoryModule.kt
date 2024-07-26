package com.weit2nd.data.di

import com.weit2nd.data.repository.category.CategoryRepositoryImpl
import com.weit2nd.data.service.CategoryService
import com.weit2nd.data.source.category.CategoryCacheDataSource
import com.weit2nd.data.source.category.CategoryDataSource
import com.weit2nd.domain.repository.category.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object CategoryModule {
    @ViewModelScoped
    @Provides
    fun providesCategoryRepository(
        categoryDataSource: CategoryDataSource,
        categoryCacheDataSource: CategoryCacheDataSource,
    ): CategoryRepository {
        return CategoryRepositoryImpl(categoryDataSource, categoryCacheDataSource)
    }

    @ViewModelScoped
    @Provides
    fun providesCategoryDataSource(service: CategoryService): CategoryDataSource {
        return CategoryDataSource(service)
    }

    @ViewModelScoped
    @Provides
    fun providesCategoryCacheDataSource(): CategoryCacheDataSource {
        return CategoryCacheDataSource
    }

    @ViewModelScoped
    @Provides
    fun providesCategoryService(
        @AuthNetwork retrofit: Retrofit,
    ): CategoryService {
        return retrofit.create(CategoryService::class.java)
    }
}

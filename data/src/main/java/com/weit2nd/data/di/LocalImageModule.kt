package com.weit2nd.data.di

import android.content.Context
import com.weit2nd.data.repository.localimage.LocalImageRepositoryImpl
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.domain.repository.localimage.LocalImageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LocalImageModule {
    @ViewModelScoped
    @Provides
    fun provideLocalImageRepository(localImageDatasource: LocalImageDatasource): LocalImageRepository {
        return LocalImageRepositoryImpl(localImageDatasource)
    }

    @ViewModelScoped
    @Provides
    fun providesLocalImageDatasource(
        @ApplicationContext context: Context,
    ): LocalImageDatasource {
        return LocalImageDatasource(context.contentResolver)
    }
}

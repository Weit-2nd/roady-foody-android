package com.weit2nd.data.di

import com.squareup.moshi.Moshi
import com.weit2nd.data.repository.review.ReviewRepositoryImpl
import com.weit2nd.data.service.ReviewService
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.review.ReviewDataSource
import com.weit2nd.domain.repository.review.ReviewRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
object ReviewModule {
    @ViewModelScoped
    @Provides
    fun providesReviewRepository(
        reviewDataSource: ReviewDataSource,
        localImageDatasource: LocalImageDatasource,
        moshi: Moshi,
    ): ReviewRepository {
        return ReviewRepositoryImpl(
            reviewDataSource = reviewDataSource,
            localImageDatasource = localImageDatasource,
            moshi = moshi,
        )
    }

    @ViewModelScoped
    @Provides
    fun providesReviewDataSource(reviewService: ReviewService): ReviewDataSource {
        return ReviewDataSource(reviewService)
    }

    @ViewModelScoped
    @Provides
    fun providesReviewService(
        @AuthNetwork retrofit: Retrofit,
    ): ReviewService {
        return retrofit.create(ReviewService::class.java)
    }
}

package com.weit2nd.data.repository.review

import com.squareup.moshi.Moshi
import com.weit2nd.data.model.review.PostReviewRequest
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.review.ReviewDataSource
import com.weit2nd.data.util.getMultiPart
import com.weit2nd.domain.exception.review.ReviewException
import com.weit2nd.domain.repository.review.ReviewRepository
import okhttp3.internal.http.HTTP_BAD_REQUEST
import okhttp3.internal.http.HTTP_NOT_FOUND
import retrofit2.HttpException
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val reviewDataSource: ReviewDataSource,
    private val localImageDatasource: LocalImageDatasource,
    private val moshi: Moshi,
) : ReviewRepository {
    override suspend fun postReview(
        foodSpotId: Long,
        contents: String,
        rating: Int,
        images: List<String>,
    ) {
        val imageParts =
            localImageDatasource.getImageMultipartBodyParts(
                uris = images,
                formDataName = "reviewPhotos",
            )
        val request =
            PostReviewRequest(
                foodSpotId = foodSpotId,
                contents = contents,
                rating = rating,
            )
        val postReviewPart =
            moshi.adapter(PostReviewRequest::class.java).getMultiPart(
                formDataName = "reviewRequest",
                fileName = "reviewRequest",
                request = request,
            )
        runCatching {
            reviewDataSource.postReview(
                reviewRequest = postReviewPart,
                reviewPhotos = imageParts,
            )
        }.onFailure {
            throw handleReviewException(it)
        }
    }

    private fun handleReviewException(throwable: Throwable): Throwable {
        return when (throwable) {
            is HttpException -> {
                handleHttpException(throwable)
            }
            else -> throwable
        }
    }

    private fun handleHttpException(throwable: HttpException): Throwable {
        return when (throwable.code()) {
            HTTP_BAD_REQUEST -> ReviewException.BadRequestException(throwable.message())
            HTTP_NOT_FOUND -> ReviewException.FoodSpotNotFoundException(throwable.message())
            else -> throwable
        }
    }

    override suspend fun verifyReview(
        contents: String,
        rating: Int,
        images: List<String>,
    ) {
        TODO("Not yet implemented")
    }
}

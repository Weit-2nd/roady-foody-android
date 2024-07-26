package com.weit2nd.data.repository.review

import com.squareup.moshi.Moshi
import com.weit2nd.data.model.review.PostReviewRequest
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.review.ReviewDataSource
import com.weit2nd.data.util.getMultiPart
import com.weit2nd.domain.repository.review.ReviewRepository
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
            images
                .map { image ->
                    localImageDatasource.getImageMultipartBodyPart(
                        uri = image,
                        formDataName = "reviewPhotos",
                        imageName = System.nanoTime().toString(),
                    )
                }.takeIf { it.isNotEmpty() }
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
        reviewDataSource.postReview(
            reviewRequest = postReviewPart,
            reviewPhotos = imageParts,
        )
    }

    override suspend fun verifyReview(
        contents: String,
        rating: Int,
        images: List<String>,
    ) {
        TODO("Not yet implemented")
    }
}

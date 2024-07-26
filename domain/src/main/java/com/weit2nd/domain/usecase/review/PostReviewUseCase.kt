package com.weit2nd.domain.usecase.review

import com.weit2nd.domain.repository.review.ReviewRepository
import javax.inject.Inject

class PostReviewUseCase @Inject constructor(
    private val repository: ReviewRepository,
) {
    /**
     * 리뷰를 작성 합니다.
     *
     * @param foodSpotId 음식점 리포트 id
     * @param contents 리뷰 내용
     * @param rating 별점 1~10까지
     * @param images 리뷰 이미지 최대 3장
     */
    suspend operator fun invoke(
        foodSpotId: Long,
        contents: String,
        rating: Int,
        images: List<String>,
    ) {
        repository.postReview(
            foodSpotId = foodSpotId,
            contents = contents,
            rating = rating,
            images = images,
        )
    }
}

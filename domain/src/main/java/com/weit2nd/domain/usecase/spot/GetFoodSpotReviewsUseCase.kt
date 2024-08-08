package com.weit2nd.domain.usecase.spot

import com.weit2nd.domain.model.spot.FoodSpotReviews
import com.weit2nd.domain.model.spot.ReviewSortType
import com.weit2nd.domain.repository.spot.FoodSpotRepository
import javax.inject.Inject

class GetFoodSpotReviewsUseCase @Inject constructor(
    private val repository: FoodSpotRepository,
) {
    /**
     * 음식점의 리뷰 리스트를 조회합니다.
     * @param foodSpotsId 음식점 아이디
     * @param count 조회할 개수
     * @param lastItemId 마지막으로 조회된 요소의 아이디 (첫 페이지를 조회하는 경우 빈 값을 넣어줍니다.)
     * @param sortType 리뷰 정렬 방식 (LATEST or HIGHEST)
     */
    suspend operator fun invoke(
        foodSpotsId: Long,
        count: Int,
        lastItemId: Long?,
        sortType: ReviewSortType,
    ): FoodSpotReviews {
        return repository.getFoodSpotReviews(
            foodSpotsId = foodSpotsId,
            count = count,
            lastItemId = lastItemId,
            sortType = sortType,
        )
    }
}

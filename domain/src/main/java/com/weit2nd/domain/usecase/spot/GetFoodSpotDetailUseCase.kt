package com.weit2nd.domain.usecase.spot

import com.weit2nd.domain.model.spot.FoodSpotDetail
import com.weit2nd.domain.repository.spot.FoodSpotRepository
import javax.inject.Inject

class GetFoodSpotDetailUseCase @Inject constructor(
    private val repository: FoodSpotRepository,
) {
    /**
     * 음식점 상세 정보를 조회합니다.
     * @param foodSpotsId 음식점 아이디
     */
    suspend operator fun invoke(foodSpotsId: Long): FoodSpotDetail {
        return repository.getFoodSpotDetail(
            foodSpotsId = foodSpotsId,
        )
    }
}

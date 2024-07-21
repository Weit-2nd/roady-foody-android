package com.weit2nd.domain.usecase.spot

import com.weit2nd.domain.model.spot.FoodSpotHistories
import com.weit2nd.domain.repository.spot.FoodSpotRepository
import javax.inject.Inject

class GetFoodSpotHistoriesUseCase @Inject constructor(
    private val repository: FoodSpotRepository,
) {
    /**
     * 사용자 아이디를 통해 해당 사용자가 작성한 음식점 리포트들을 조회합니다.
     * @param userId 사용자 아이디
     * @param count 조회할 개수
     * @param lastItemId 마지막으로 조회된 요소의 아이디 (첫 페이지를 조회하는 경우 빈 값을 넣어줍니다.)
     */
    suspend fun invoke(
        userId: Long,
        count: Int,
        lastItemId: Long? = null,
    ): FoodSpotHistories {
        return repository.getFoodSpotHistories(
            userId = userId,
            count = count,
            lastItemId = lastItemId,
        )
    }
}

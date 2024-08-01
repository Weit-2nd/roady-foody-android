package com.weit2nd.domain.usecase.spot

import com.weit2nd.domain.repository.spot.FoodSpotRepository
import javax.inject.Inject

class DeleteFoodSpotHistoryUseCase @Inject constructor(
    private val repository: FoodSpotRepository,
) {
    /**
     * 리포트 작성 기록을 삭제 합니다. 자신이 작성한 리포트 기록만 삭제 가능합니다.
     * @param historyId 리포트 히스토리 Id
     */
    suspend fun invoke(historyId: Long) {
        return repository.deleteFoodSpotHistory(historyId)
    }
}

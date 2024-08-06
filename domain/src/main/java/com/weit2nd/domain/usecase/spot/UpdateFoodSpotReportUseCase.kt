package com.weit2nd.domain.usecase.spot

import com.weit2nd.domain.model.spot.OperationHour
import com.weit2nd.domain.repository.spot.FoodSpotRepository
import javax.inject.Inject

class UpdateFoodSpotReportUseCase @Inject constructor(
    private val repository: FoodSpotRepository,
) {
    /**
     * 음식점을 리포트를 수정 합니다.
     *
     * 수정이 필요 없는 필드는 null을 넣습니다.
     * @param foodSpotId 수정 하는 리포트 id
     * @param name 음식점 이름
     * @param longitude 음식점 좌표 (경도)
     * @param latitude 음식점 좌표 (위도)
     * @param open 현재 영업 중
     * @param closed 폐업
     * @param foodCategories 사용자가 선택한 음식 카테고리 id 리스트
     * @param operationHours 요일별 운영 시간 리스트
     */
    suspend operator fun invoke(
        foodSpotId: Long,
        name: String? = null,
        longitude: Double? = null,
        latitude: Double? = null,
        open: Boolean? = null,
        closed: Boolean? = null,
        foodCategories: List<Long>? = null,
        operationHours: List<OperationHour>? = null,
    ) {
        repository.updateFoodSpotReport(
            foodSpotsId = foodSpotId,
            name = name,
            longitude = longitude,
            latitude = latitude,
            open = open,
            closed = closed,
            foodCategories = foodCategories,
            operationHours = operationHours,
        )
    }
}

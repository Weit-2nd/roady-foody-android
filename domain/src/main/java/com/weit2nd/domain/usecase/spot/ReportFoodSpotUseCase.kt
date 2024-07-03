package com.weit2nd.domain.usecase.spot

import com.weit2nd.domain.repository.spot.FoodSpotRepository
import javax.inject.Inject

class ReportFoodSpotUseCase @Inject constructor(
    private val repository: FoodSpotRepository,
) {
    /**
     * 음식점을 등록 합니다.
     * @param name 음식점 이름
     * @param longitude 음식점 좌표 (경도)
     * @param latitude 음식점 좌표 (위도)
     * @param isFoodTruck 음식점이 푸드트럭 인지
     * @param open 현재 영업 중
     * @param closed 폐업
     * @param images 음식점 이미지
     */
    suspend operator fun invoke(
        name: String,
        longitude: Double,
        latitude: Double,
        isFoodTruck: Boolean,
        open: Boolean,
        closed: Boolean,
        images: List<String>,
    ) {
        repository.reportFoodSpot(
            name = name,
            longitude = longitude,
            latitude = latitude,
            isFoodTruck = isFoodTruck,
            open = open,
            closed = closed,
            images = images,
        )
    }
}

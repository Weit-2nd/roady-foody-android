package com.weit2nd.domain.usecase.spot

import com.weit2nd.domain.repository.spot.FoodSpotRepository
import javax.inject.Inject

class ReportFoodSpotUseCase @Inject constructor(
    private val repository: FoodSpotRepository,
) {

    /**
     * 음식점을 등록 합니다.
     * @see Params
     */
    suspend operator fun invoke(params: Params) {
        repository.reportFoodSpot(
            name = params.name,
            longitude = params.longitude,
            latitude = params.latitude,
            isFoodTruck = params.isFoodTruck,
            open = params.open,
            closed = params.closed,
            images = params.images,
        )
    }

    /**
     * @param name 음식점 이름
     * @param longitude 음식점 좌표 (경도)
     * @param latitude 음식점 좌표 (위도)
     * @param isFoodTruck 음식점이 푸드트럭 인지
     * @param open 현재 영업 중
     * @param closed 폐업
     * @param images 음식점 이미지
     */
    data class Params(
        val name: String,
        val longitude: Double,
        val latitude: Double,
        val isFoodTruck: Boolean,
        val open: Boolean,
        val closed: Boolean,
        val images: List<String>,
    )
}

package com.weit2nd.domain.usecase.spot

import com.weit2nd.domain.model.spot.ReportFoodSpotState
import com.weit2nd.domain.repository.spot.FoodSpotRepository
import javax.inject.Inject

class VerifyReportUseCase @Inject constructor(
    private val repository: FoodSpotRepository,
) {
    /**
     * 현재 작성한 음식점 리포트 정보가 유효한지 검사합니다.
     * @param name 음식점 이름
     * @param longitude 음식점 좌표 (경도)
     * @param latitude 음식점 좌표 (위도)
     * @param images 음식점 이미지
     */
    suspend operator fun invoke(
        name: String,
        longitude: Double,
        latitude: Double,
        images: List<String>,
    ): ReportFoodSpotState {
        return repository.verifyReport(
            name = name,
            longitude = longitude,
            latitude = latitude,
            images = images,
        )
    }
}

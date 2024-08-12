package com.weit2nd.domain.model.spot

import java.time.LocalDateTime
import java.time.LocalTime

/**
 * @param id 음식점 id
 * @param name 음식점 이름
 * @param longitude 경도
 * @param latitude 위도
 * @param movableFoodSpots 이동여부(푸드 트럭, 푸드 카트 등)
 * @param openState 운영 상태 [OPEN, CLOSED, TEMPORARILY_CLOSED]
 * @param storeClosure 폐업 여부
 * @param operationHoursList 영업 시간 리스트
 * @param foodCategoryList 음식 카테고리 리스트
 * @param foodSpotsPhotos 음식점 사진 리스트
 * @param createdDateTime 음식점 생성 일자
 */

data class FoodSpotDetail(
    val id: Long,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val movableFoodSpots: Boolean,
    val openState: FoodSpotOpenState,
    val storeClosure: Boolean,
    val operationHoursList: List<FoodSpotDetailOperationHours>,
    val foodCategoryList: List<FoodCategory>,
    val foodSpotsPhotos: List<FoodSpotPhoto>,
    val createdDateTime: LocalDateTime,
)

data class FoodSpotDetailOperationHours(
    val foodSpotsId: Long,
    val dayOfWeek: FoodSpotOperationDayOfWeek,
    val openingHours: LocalTime,
    val closingHours: LocalTime,
)

enum class FoodSpotOpenState {
    OPEN,
    CLOSED,
    TEMPORARILY_CLOSED,
    UNKNOWN,
}

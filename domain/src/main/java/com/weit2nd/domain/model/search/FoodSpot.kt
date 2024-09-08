package com.weit2nd.domain.model.search

import com.weit2nd.domain.model.spot.OperationHour
import java.time.LocalDateTime

/**
 * @param id 음식점 ID
 * @param name 음식점 이름
 * @param longitude 음식점 좌표 (경도)
 * @param latitude 음식점 좌표 (위도)
 * @param businessState 영업 상태
 * @param operationHour 영업 시간
 * @param categories 카테고리
 * @param image 대표 이미지
 * @param isFoodTruck 푸드트럭 여부
 * @param averageRating 평균 별점
 * @param reviewCount 리뷰 개수
 * @param createAt 등록 날짜
 */
data class FoodSpot(
    val id: Long,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val businessState: BusinessState,
    val operationHour: OperationHour,
    val categories: List<String>,
    val image: String,
    val isFoodTruck: Boolean,
    val averageRating: Float,
    val reviewCount: Int,
    val createAt: LocalDateTime,
)

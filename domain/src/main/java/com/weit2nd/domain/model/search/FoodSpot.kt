package com.weit2nd.domain.model.search

import java.time.LocalDateTime

/**
 * @param id 음식점 ID
 * @param name 음식점 이름
 * @param longitude 음식점 좌표 (경도)
 * @param latitude 음식점 좌표 (위도)
 * @param businessState 영업 상태
 * @param categories 카테고리
 * @param createAt 등록 날짜
 */
data class FoodSpot(
    val id: Long,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val businessState: BusinessState,
    val categories: List<String>,
    val createAt: LocalDateTime,
)

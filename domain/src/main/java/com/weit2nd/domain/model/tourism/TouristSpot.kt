package com.weit2nd.domain.model.tourism

/**
 * @param title 관광지 이름
 * @param mainAddress 주소
 * @param secondaryAddress 세부 주소 (빈 값이 올 수 있음)
 * @param longitude 관광지 좌표 (경도)
 * @param latitude 관광지 좌표 (위도)
 * @param tel 전화번호 (빈 값이 올 수 있음)
 * @param thumbnailImage 썸네일 uri (빈 값이 올 수 있음)
 * @param tourismType 관광지 유형
 */
data class TouristSpot(
    val title: String,
    val mainAddress: String,
    val secondaryAddress: String,
    val longitude: Double,
    val latitude: Double,
    val tel: String,
    val thumbnailImage: String,
    val tourismType: TourismType,
)

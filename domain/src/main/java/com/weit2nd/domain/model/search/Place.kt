package com.weit2nd.domain.model.search

/**
 * @param placeName 장소 이름
 * @param addressName 주소
 * @param roadAddressName 도로명 주소
 * @param longitude 장소 좌표 (경도)
 * @param latitude 장소 좌표 (위도)
 * @param tel 전화번호 (빈 값이 올 수 있음)
 */
data class Place(
    val placeName: String,
    val addressName: String,
    val roadAddressName: String,
    val longitude: Double,
    val latitude: Double,
    val tel: String,
)

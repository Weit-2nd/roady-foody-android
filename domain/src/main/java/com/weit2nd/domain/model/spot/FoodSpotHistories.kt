package com.weit2nd.domain.model.spot

data class FoodSpotHistories(
    val contents: List<FoodSpotHistoryContent>,
    val hasNext: Boolean,
)

data class FoodSpotHistoryContent(
    val id: Long,
    val userId: Long,
    val foodSpotsId: Long,
    val name: String,
    val longitude: Double,
    val latitude: Double,
    val createdDateTime: String,
    val reportPhotos: List<FoodSpotPhoto>,
    val categories: List<FoodSpotCategory>,
)

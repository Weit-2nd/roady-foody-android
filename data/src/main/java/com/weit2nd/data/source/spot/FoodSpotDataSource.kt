package com.weit2nd.data.source.spot

import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.service.SpotService
import okhttp3.MultipartBody
import javax.inject.Inject

class FoodSpotDataSource @Inject constructor(
    private val service: SpotService,
) {
    suspend fun reportFoodSpot(
        reportRequest: MultipartBody.Part,
        reportPhotos: List<MultipartBody.Part>?,
    ) {
        service.reportFoodSpot(
            reportRequest = reportRequest,
            reportPhotos = reportPhotos,
        )
    }

    suspend fun getFoodSpotHistories(
        userId: Long,
        count: Int,
        lastItemId: Long,
    ): FoodSpotHistoriesDTO {
        return service.getFoodSpotHistories(
            userId = userId,
            size = count,
            lastId = lastItemId,
        )
    }
}

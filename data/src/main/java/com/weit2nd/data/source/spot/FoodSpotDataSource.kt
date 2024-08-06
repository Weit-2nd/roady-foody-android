package com.weit2nd.data.source.spot

import com.weit2nd.data.model.spot.UpdateFoodSpotReportRequest
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

    suspend fun deleteFoodSpotHistory(historyId: Long) {
        service.deleteFoodSpotHistory(historyId)
    }

    suspend fun updateFoodSpotReport(
        foodSpotsId: Long,
        request: UpdateFoodSpotReportRequest,
    ) {
        service.updateFoodSpotReport(
            foodSpotsId = foodSpotsId,
            request = request,
        )
    }
}

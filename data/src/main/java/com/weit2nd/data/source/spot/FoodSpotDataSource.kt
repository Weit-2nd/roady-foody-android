package com.weit2nd.data.source.spot

import com.weit2nd.data.model.spot.FoodSpotDetailDTO
import com.weit2nd.data.model.spot.FoodSpotReviewsDTO
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

    suspend fun getFoodSpotReviews(
        foodSpotsId: Long,
        count: Int,
        lastItemId: Long?,
        sortType: String,
    ): FoodSpotReviewsDTO {
        return service.getFoodSpotReviews(
            foodSpotsId = foodSpotsId,
            size = count,
            lastId = lastItemId,
            sortType = sortType,
        )
    }

    suspend fun getFoodSpotDetail(foodSpotsId: Long): FoodSpotDetailDTO {
        return service.getFoodSpotDetail(foodSpotsId = foodSpotsId)
    }
}

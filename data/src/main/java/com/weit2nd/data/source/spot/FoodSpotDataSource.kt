package com.weit2nd.data.source.spot

import com.weit2nd.data.service.SpotService
import okhttp3.MultipartBody
import retrofit2.http.Part
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
}

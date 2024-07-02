package com.weit2nd.data.repository.spot

import com.squareup.moshi.Moshi
import com.weit2nd.data.model.spot.ReportFoodSpotRequest
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.spot.FoodSpotDataSource
import com.weit2nd.data.util.getMultiPart
import com.weit2nd.domain.repository.spot.FoodSpotRepository
import javax.inject.Inject

class FoodSpotRepositoryImpl @Inject constructor(
    private val foodSpotDataSource: FoodSpotDataSource,
    private val localImageDatasource: LocalImageDatasource,
    private val moshi: Moshi,
) : FoodSpotRepository {
    override suspend fun reportFoodSpot(
        name: String,
        longitude: Double,
        latitude: Double,
        isFoodTruck: Boolean,
        open: Boolean,
        closed: Boolean,
        images: List<String>,
    ) {
        val imageParts = images.map { image ->
            localImageDatasource.getImageMultipartBodyPart(
                uri = image,
                formDataName = "reportPhotos",
                imageName = System.nanoTime().toString(),
            )
        }.takeIf { it.isNotEmpty() }
        val request = ReportFoodSpotRequest(
            name = name,
            longitude = longitude,
            latitude = latitude,
            foodTruck = isFoodTruck,
            open = open,
            closed = closed,
        )
        val reportFoodSpotPart = moshi.adapter(ReportFoodSpotRequest::class.java).getMultiPart(
            formDataName = "reportRequest",
            fileName = "reportRequest",
            request = request,
        )
        foodSpotDataSource.reportFoodSpot(
            reportRequest = reportFoodSpotPart,
            reportPhotos = imageParts,
        )
    }

    override suspend fun verifyReport(
        name: String,
        longitude: Double,
        latitude: Double,
        images: List<String>,
    ) {
        TODO("Not yet implemented")
    }
}

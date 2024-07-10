package com.weit2nd.data.repository.spot

import com.squareup.moshi.Moshi
import com.weit2nd.data.model.spot.ReportFoodSpotRequest
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.spot.FoodSpotDataSource
import com.weit2nd.data.util.getMultiPart
import com.weit2nd.domain.exception.imageuri.NotImageException
import com.weit2nd.domain.model.spot.ReportFoodSpotState
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
        if (localImageDatasource.checkImagesUriValid(images).not()) throw NotImageException()

        val imageParts =
            images
                .map { image ->
                    localImageDatasource.getImageMultipartBodyPart(
                        uri = image,
                        formDataName = "reportPhotos",
                        imageName = System.nanoTime().toString(),
                    )
                }.takeIf { it.isNotEmpty() }
        val request =
            ReportFoodSpotRequest(
                name = name,
                longitude = longitude,
                latitude = latitude,
                foodTruck = isFoodTruck,
                open = open,
                closed = closed,
            )
        val reportFoodSpotPart =
            moshi.adapter(ReportFoodSpotRequest::class.java).getMultiPart(
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
    ): ReportFoodSpotState {
        val invalidImage =
            if (images.size > MAX_IMAGE_COUNT) {
                null
            } else {
                findInvalidImage(images)
            }
        return when {
            invalidImage != null -> {
                ReportFoodSpotState.InvalidImage(invalidImage)
            }

            verifyCoordinate(longitude, latitude).not() -> {
                ReportFoodSpotState.BadCoordinate
            }

            images.size > MAX_IMAGE_COUNT -> {
                ReportFoodSpotState.TooManyImages
            }

            verifyName(name).not() -> {
                ReportFoodSpotState.BadFoodSpotName
            }

            else -> {
                ReportFoodSpotState.Valid
            }
        }
    }

    private fun verifyName(name: String): Boolean {
        return name.isNotBlank() && foodSpotNameRegex.matches(name)
    }

    private fun verifyCoordinate(
        longitude: Double,
        latitude: Double,
    ): Boolean {
        return (longitude in MIN_COORDINATE..MAX_COORDINATE) &&
            (latitude in MIN_COORDINATE..MAX_COORDINATE)
    }

    private fun findInvalidImage(images: List<String>): String? {
        // TODO 이미지 uri 검증
        return null
    }

    companion object {
        private const val MAX_COORDINATE = 180f
        private const val MIN_COORDINATE = -180f
        private const val MAX_IMAGE_COUNT = 3
        private val foodSpotNameRegex = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9!@#\$%^&*()\\-\\+ ]{1,20}\$")
    }
}

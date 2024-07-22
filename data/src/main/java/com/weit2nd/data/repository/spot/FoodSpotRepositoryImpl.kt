package com.weit2nd.data.repository.spot

import com.squareup.moshi.Moshi
import com.weit2nd.data.model.spot.FoodSpotCategoryDTO
import com.weit2nd.data.model.spot.FoodSpotHistoriesDTO
import com.weit2nd.data.model.spot.FoodSpotHistoryContentDTO
import com.weit2nd.data.model.spot.FoodSpotReportPhotoDTO
import com.weit2nd.data.model.spot.ReportFoodSpotRequest
import com.weit2nd.data.model.spot.toRequest
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.spot.FoodSpotDataSource
import com.weit2nd.data.util.getMultiPart
import com.weit2nd.domain.exception.imageuri.NotImageException
import com.weit2nd.domain.model.spot.FoodSpotCategory
import com.weit2nd.domain.model.spot.FoodSpotHistories
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.domain.model.spot.FoodSpotPhoto
import com.weit2nd.domain.model.spot.OperationHour
import com.weit2nd.domain.model.spot.ReportFoodSpotState
import com.weit2nd.domain.repository.spot.FoodSpotRepository
import okhttp3.internal.http.HTTP_BAD_REQUEST
import okhttp3.internal.http.HTTP_NOT_FOUND
import retrofit2.HttpException
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
        foodCategories: List<Long>,
        operationHours: List<OperationHour>,
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
                foodCategories = foodCategories,
                operationHours = operationHours.map { it.toRequest() },
            )
        val reportFoodSpotPart =
            moshi.adapter(ReportFoodSpotRequest::class.java).getMultiPart(
                formDataName = "reportRequest",
                fileName = "reportRequest",
                request = request,
            )

        runCatching {
            foodSpotDataSource.reportFoodSpot(
                reportRequest = reportFoodSpotPart,
                reportPhotos = imageParts,
            )
        }.onFailure { throwable ->
            throw handleException(throwable)
        }
    }

    private fun handleException(throwable: Throwable) =
        if (throwable is HttpException) {
            val errorMessage = throwable.message()
            when (throwable.code()) {
                HTTP_BAD_REQUEST -> IllegalStateException(errorMessage)
                HTTP_NOT_FOUND -> IllegalStateException(errorMessage)
                else -> throwable
            }
        } else {
            throwable
        }

    override suspend fun verifyReport(
        name: String,
        longitude: Double?,
        latitude: Double?,
        foodCategories: List<Long>,
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

            foodCategories.isEmpty() -> {
                ReportFoodSpotState.NoFoodCategory
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
        longitude: Double?,
        latitude: Double?,
    ): Boolean {
        if (longitude == null || latitude == null) return false
        return (longitude in MIN_COORDINATE..MAX_COORDINATE) &&
            (latitude in MIN_COORDINATE..MAX_COORDINATE)
    }

    private fun findInvalidImage(images: List<String>): String? {
        return localImageDatasource.findInvalidImage(images)
    }

    override suspend fun getFoodSpotHistories(
        userId: Long,
        count: Int,
        lastItemId: Long?,
    ): FoodSpotHistories {
        return foodSpotDataSource
            .getFoodSpotHistories(
                userId = userId,
                count = count,
                lastItemId = lastItemId,
            ).toFoodSpotHistories()
    }

    private fun FoodSpotHistoriesDTO.toFoodSpotHistories() =
        FoodSpotHistories(
            contents = contents.map { it.toFoodSpotHistoryContent() },
            hasNext = hasNext,
        )

    private fun FoodSpotHistoryContentDTO.toFoodSpotHistoryContent() =
        FoodSpotHistoryContent(
            id = id,
            userId = userId,
            foodSpotsId = foodSpotsId,
            name = name,
            longitude = longitude,
            latitude = latitude,
            createdDateTime = createdDateTime,
            reportPhotos = reportPhotos.map { it.toFoodSpotPhoto() },
            categories = categories.map { it.toFoodSpotCategory() },
        )

    private fun FoodSpotReportPhotoDTO.toFoodSpotPhoto() =
        FoodSpotPhoto(
            id = id,
            url = url,
        )

    private fun FoodSpotCategoryDTO.toFoodSpotCategory() =
        FoodSpotCategory(
            id = id,
            name = name,
        )

    companion object {
        private const val MAX_COORDINATE = 180f
        private const val MIN_COORDINATE = -180f
        private const val MAX_IMAGE_COUNT = 3
        private val foodSpotNameRegex = Regex("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9!@#\$%^&*()\\-\\+ ]{1,20}\$")
    }
}

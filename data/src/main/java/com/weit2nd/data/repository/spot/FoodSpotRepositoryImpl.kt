package com.weit2nd.data.repository.spot

import com.squareup.moshi.Moshi
import com.weit2nd.data.model.spot.FoodSpotReviewContentDTO
import com.weit2nd.data.model.spot.FoodSpotReviewPhotoDTO
import com.weit2nd.data.model.spot.FoodSpotReviewUserInfoDTO
import com.weit2nd.data.model.spot.FoodSpotReviewsDTO
import com.weit2nd.data.model.spot.ReportFoodSpotRequest
import com.weit2nd.data.model.spot.UpdateFoodSpotReportRequest
import com.weit2nd.data.model.spot.toRequest
import com.weit2nd.data.source.localimage.LocalImageDatasource
import com.weit2nd.data.source.spot.FoodSpotDataSource
import com.weit2nd.data.util.getMultiPart
import com.weit2nd.domain.exception.DeleteFoodSpotHistoryException
import com.weit2nd.domain.exception.imageuri.NotImageException
import com.weit2nd.domain.exception.spot.UpdateFoodSpotReportException
import com.weit2nd.domain.model.spot.FoodSpotPhoto
import com.weit2nd.domain.model.spot.FoodSpotReview
import com.weit2nd.domain.model.spot.FoodSpotReviewUserInfo
import com.weit2nd.domain.model.spot.FoodSpotReviews
import com.weit2nd.domain.model.spot.OperationHour
import com.weit2nd.domain.model.spot.ReportFoodSpotState
import com.weit2nd.domain.model.spot.ReviewSortType
import com.weit2nd.domain.repository.spot.FoodSpotRepository
import okhttp3.internal.http.HTTP_BAD_REQUEST
import okhttp3.internal.http.HTTP_CONFLICT
import okhttp3.internal.http.HTTP_FORBIDDEN
import okhttp3.internal.http.HTTP_NOT_FOUND
import okhttp3.internal.http.HTTP_TOO_MANY_REQUESTS
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
            localImageDatasource.getImageMultipartBodyParts(
                uris = images,
                formDataName = "reportPhotos",
            )
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
            throw handleReportFoodSpotException(throwable)
        }
    }

    private fun handleReportFoodSpotException(throwable: Throwable) =
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

    override suspend fun deleteFoodSpotHistory(historyId: Long) {
        runCatching {
            foodSpotDataSource.deleteFoodSpotHistory(historyId)
        }.onFailure {
            throw handleDeleteFoodSpotException(it)
        }
    }

    private fun handleDeleteFoodSpotException(throwable: Throwable) =
        if (throwable is HttpException) {
            val errorMessage = throwable.message()
            when (throwable.code()) {
                HTTP_FORBIDDEN -> DeleteFoodSpotHistoryException.NotHistoryOwnerException(errorMessage)
                HTTP_BAD_REQUEST,
                HTTP_NOT_FOUND,
                -> DeleteFoodSpotHistoryException.HistoryNotFoundException(errorMessage)
                else -> throwable
            }
        } else {
            throwable
        }

    override suspend fun updateFoodSpotReport(
        foodSpotsId: Long,
        name: String?,
        longitude: Double?,
        latitude: Double?,
        open: Boolean?,
        closed: Boolean?,
        foodCategories: List<Long>?,
        operationHours: List<OperationHour>?,
    ) {
        val request =
            UpdateFoodSpotReportRequest(
                name = name,
                longitude = longitude,
                latitude = latitude,
                open = open,
                closed = closed,
                foodCategories = foodCategories,
                operationHours =
                    operationHours?.map {
                        it.toRequest()
                    },
            )
        runCatching {
            foodSpotDataSource.updateFoodSpotReport(
                foodSpotsId = foodSpotsId,
                request = request,
            )
        }.onFailure {
            throw handleUpdateFoodSpotReportException(it)
        }
    }

    private fun handleUpdateFoodSpotReportException(throwable: Throwable) =
        if (throwable is HttpException) {
            val errorMessage = throwable.message()
            when (throwable.code()) {
                HTTP_BAD_REQUEST ->
                    UpdateFoodSpotReportException.InvalidFoodSpotNameException(errorMessage)
                HTTP_NOT_FOUND ->
                    UpdateFoodSpotReportException.NotFoundFoodCategoryException(errorMessage)
                HTTP_CONFLICT ->
                    UpdateFoodSpotReportException.FoodSpotAlreadyClosedException(errorMessage)
                HTTP_TOO_MANY_REQUESTS ->
                    UpdateFoodSpotReportException.TooManyReportRequestException(errorMessage)
                else -> throwable
            }
        } else {
            throwable
        }

    override suspend fun getFoodSpotReviews(
        foodSpotsId: Long,
        count: Int,
        lastItemId: Long?,
        sortType: ReviewSortType,
    ): FoodSpotReviews {
        return foodSpotDataSource
            .getFoodSpotReviews(
                foodSpotsId = foodSpotsId,
                count = count,
                lastItemId = lastItemId,
                sortType = sortType.name,
            ).toFoodSpotReviews()
    }

    private fun FoodSpotReviewsDTO.toFoodSpotReviews() =
        FoodSpotReviews(
            reviews = contents.map { it.toFoodSpotReview() },
            hasNext = hasNext,
        )

    private fun FoodSpotReviewContentDTO.toFoodSpotReview() =
        FoodSpotReview(
            id = id,
            foodSpotsId = foodSpotsId,
            userInfo = userInfo.toFoodSpotReviewUserInfo(),
            contents = contents,
            rate = rate,
            photos = photos.map { it.toFoodSpotReviewPhoto() },
            createdAt = createdAt,
        )

    private fun FoodSpotReviewUserInfoDTO.toFoodSpotReviewUserInfo() =
        FoodSpotReviewUserInfo(
            id = id,
            nickname = nickname,
            profileImage = profileImage,
        )

    private fun FoodSpotReviewPhotoDTO.toFoodSpotReviewPhoto() =
        FoodSpotPhoto(
            id = id,
            image = image,
        )

    companion object {
        private const val MAX_COORDINATE = 180f
        private const val MIN_COORDINATE = -180f
        private const val MAX_IMAGE_COUNT = 3
        private val foodSpotNameRegex = Regex("^[가-힣a-zA-Z0-9.,'·&\\-\\s]{1,20}\$")
    }
}

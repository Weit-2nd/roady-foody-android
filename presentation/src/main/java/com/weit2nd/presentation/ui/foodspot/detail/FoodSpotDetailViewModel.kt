package com.weit2nd.presentation.ui.foodspot.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.spot.FoodSpotDetailOperationHours
import com.weit2nd.domain.model.spot.FoodSpotReview
import com.weit2nd.domain.model.spot.ReviewSortType
import com.weit2nd.domain.usecase.search.SearchPlaceWithCoordinateUseCase
import com.weit2nd.domain.usecase.spot.GetFoodSpotDetailUseCase
import com.weit2nd.domain.usecase.spot.GetFoodSpotReviewsUseCase
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.model.foodspot.OperationHour
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.model.reivew.ExpendableReview
import com.weit2nd.presentation.navigation.FoodSpotDetailRoutes
import com.weit2nd.presentation.navigation.dto.FoodSpotForReviewDTO
import com.weit2nd.presentation.navigation.dto.FoodSpotReviewDTO
import com.weit2nd.presentation.navigation.dto.toFoodCategoryDTO
import com.weit2nd.presentation.navigation.dto.toRatingCountDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class FoodSpotDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getFoodSpotDetailUseCase: GetFoodSpotDetailUseCase,
    private val getFoodSpotReviewsUseCase: GetFoodSpotReviewsUseCase,
    private val searchPlaceWithCoordinateUseCase: SearchPlaceWithCoordinateUseCase,
) : BaseViewModel<FoodSpotDetailState, FoodSpotDetailSideEffect>() {
    override val container: Container<FoodSpotDetailState, FoodSpotDetailSideEffect> =
        container(FoodSpotDetailState())

    private val foodSpotId = savedStateHandle.get<Long>(FoodSpotDetailRoutes.FOOD_SPOT_ID_KEY)

    fun onCreate() {
        if (foodSpotId != null) {
            FoodSpotDetailIntent.LoadFoodSpotDetail(foodSpotId).post()
        } else {
            FoodSpotDetailIntent.NavToBack.post()
        }
    }

    fun onImageClick(
        images: List<String>,
        position: Int,
    ) {
        // TODO ImageViewScreen 연결
    }

    fun onOperationHourClick(currentOperationHourOpenState: Boolean) {
        FoodSpotDetailIntent.ChangeOperationHoursOpenState(currentOperationHourOpenState.not()).post()
    }

    fun onPostReviewClick() {
        FoodSpotDetailIntent.NavToPostReview.post()
    }

    fun onMapReady(map: KakaoMap) {
        FoodSpotDetailIntent.OnMapReady(map).post()
    }

    fun onReviewContentsClick(position: Int) {
        FoodSpotDetailIntent
            .ChangeReviewContentExpendState(
                position = position,
                expandState = false,
            ).post()
    }

    fun onReviewContentsReadMoreClick(position: Int) {
        FoodSpotDetailIntent
            .ChangeReviewContentExpendState(
                position = position,
                expandState = true,
            ).post()
    }

    fun onReviewReadMoreClick() {
        FoodSpotDetailIntent.NavToFoodSpotReview.post()
    }

    private fun FoodSpotDetailIntent.post() =
        intent {
            when (this@post) {
                is FoodSpotDetailIntent.LoadFoodSpotDetail -> {
                    reduce {
                        state.copy(
                            shouldRetry = false,
                            isLoading = true,
                        )
                    }

                    runCatching {
                        val foodSpotReviewsDeferred =
                            viewModelScope.async {
                                getFoodSpotReviewsUseCase(
                                    foodSpotsId = id,
                                    count = DEFAULT_REVIEW_COUNT,
                                    lastItemId = null,
                                    sortType = ReviewSortType.LATEST,
                                )
                            }
                        val detailDeferred =
                            viewModelScope.async {
                                getFoodSpotDetailUseCase(id)
                            }
                        val foodSpotReviews = foodSpotReviewsDeferred.await()
                        val detail = detailDeferred.await()
                        reduce {
                            state.copy(
                                name = detail.name,
                                position =
                                    LatLng.from(
                                        detail.latitude,
                                        detail.longitude,
                                    ),
                                movableFoodSpots = detail.movableFoodSpots,
                                openState = detail.openState,
                                storeClosure = detail.storeClosure,
                                operationHours = detail.operationHoursList.map { it.toOperationHour() },
                                todayCloseTime = getTodayCloseTime(detail.operationHoursList),
                                foodCategoryList = detail.foodCategoryList,
                                foodSpotsPhotos = detail.foodSpotsPhotos.map { it.image },
                                reviewCount = detail.reviewInfo.reviewCount,
                                averageRating = detail.reviewInfo.average,
                                ratingCounts = detail.ratingCounts,
                                reviews = foodSpotReviews.reviews.map { it.toReview() },
                                hasMoreReviews = foodSpotReviews.hasNext,
                            )
                        }
                        val address =
                            searchPlaceWithCoordinateUseCase(
                                coordinate =
                                    Coordinate(
                                        latitude = detail.latitude,
                                        longitude = detail.longitude,
                                    ),
                            ).let { place ->
                                place.roadAddressName.takeIf { it.isNotBlank() } ?: place.addressName
                            }
                        reduce {
                            state.copy(
                                address = address,
                            )
                        }
                        state.map?.let { map ->
                            postSideEffect(
                                FoodSpotDetailSideEffect.MoveAndDrawMarker(
                                    map = map,
                                    position = state.position,
                                ),
                            )
                        }
                    }.onFailure {
                        reduce {
                            state.copy(
                                shouldRetry = true,
                            )
                        }
                    }
                    reduce {
                        state.copy(
                            isLoading = false,
                        )
                    }
                }

                is FoodSpotDetailIntent.ChangeOperationHoursOpenState -> {
                    reduce {
                        state.copy(
                            isOperationHoursOpen = updatedState,
                        )
                    }
                }

                FoodSpotDetailIntent.NavToBack -> {
                    postSideEffect(FoodSpotDetailSideEffect.NavToBack)
                }

                is FoodSpotDetailIntent.OnMapReady -> {
                    reduce {
                        state.copy(
                            map = map,
                        )
                    }
                    postSideEffect(
                        FoodSpotDetailSideEffect.MoveAndDrawMarker(
                            map = map,
                            position = state.position,
                        ),
                    )
                }

                FoodSpotDetailIntent.NavToPostReview -> {
                    val id = foodSpotId ?: return@intent
                    val foodSpotForReviewDTO =
                        FoodSpotForReviewDTO(
                            id = id,
                            name = state.name,
                        )
                    postSideEffect(
                        FoodSpotDetailSideEffect.NavToPostReview(
                            foodSpotForReviewDTO = foodSpotForReviewDTO,
                        ),
                    )
                }

                is FoodSpotDetailIntent.ChangeReviewContentExpendState -> {
                    val updatedReviews =
                        state.reviews
                            .mapIndexed { index, review ->
                                if (index == position) {
                                    review.copy(
                                        isExpended = expandState,
                                    )
                                } else {
                                    review
                                }
                            }
                    reduce {
                        state.copy(
                            reviews = updatedReviews,
                        )
                    }
                }

                FoodSpotDetailIntent.NavToFoodSpotReview -> {
                    val id = foodSpotId ?: return@intent
                    val foodSpotReviewDTO =
                        FoodSpotReviewDTO(
                            id = id,
                            name = state.name,
                            foodSpotsPhotos = state.foodSpotsPhotos,
                            movableFoodSpots = state.movableFoodSpots,
                            categories = state.foodCategoryList.map { it.toFoodCategoryDTO() },
                            reviewCount = state.reviewCount,
                            averageRating = state.averageRating,
                            ratingCounts = state.ratingCounts.map { it.toRatingCountDTO() },
                        )
                    postSideEffect(FoodSpotDetailSideEffect.NavToFoodSpotReview(foodSpotReviewDTO))
                }
            }
        }

    private fun getTodayCloseTime(operationHours: List<FoodSpotDetailOperationHours>): LocalTime? {
        val todayDayOfWeek = LocalDate.now().dayOfWeek
        return operationHours
            .find {
                it.dayOfWeek.dayOfWeek == todayDayOfWeek
            }?.closingHours
    }

    companion object {
        private const val DEFAULT_REVIEW_COUNT = 3
        private val operationHourFormat = DateTimeFormatter.ofPattern("HH:mm")
    }

    private fun FoodSpotDetailOperationHours.toOperationHour() =
        OperationHour(
            dayOfWeek = dayOfWeek.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            open = openingHours.format(operationHourFormat),
            close = closingHours.format(operationHourFormat),
        )

    private fun FoodSpotReview.toReview(): ExpendableReview =
        ExpendableReview(
            review =
                Review(
                    userId = id,
                    nickname = userInfo.nickname,
                    profileImage = userInfo.profileImage,
                    date = createdAt,
                    rating = rate / 2f,
                    reviewImages = photos.map { it.image },
                    contents = contents,
                ),
            isExpended = false,
        )
}

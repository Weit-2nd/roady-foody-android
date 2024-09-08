package com.weit2nd.presentation.ui.foodspot.detail

import androidx.lifecycle.SavedStateHandle
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
import com.weit2nd.presentation.navigation.FoodSpotDetailRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.viewmodel.container
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
        // TODO PostReviewScreen 연결
    }

    fun onMapReady(map: KakaoMap) {
        FoodSpotDetailIntent.OnMapReady(map).post()
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
                        val foodSpotReviews =
                            getFoodSpotReviewsUseCase(
                                foodSpotsId = id,
                                count = DEFAULT_REVIEW_COUNT,
                                lastItemId = null,
                                sortType = ReviewSortType.LATEST,
                            )
                        val detail = getFoodSpotDetailUseCase(id)
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
                                name = detail.name,
                                position =
                                    LatLng.from(
                                        detail.latitude,
                                        detail.longitude,
                                    ),
                                movableFoodSpots = detail.movableFoodSpots,
                                openState = detail.openState,
                                address = address,
                                storeClosure = detail.storeClosure,
                                operationHours = detail.operationHoursList.map { it.toOperationHour() },
                                foodCategoryList = detail.foodCategoryList,
                                foodSpotsPhotos = detail.foodSpotsPhotos.map { it.image },
                                reviews = foodSpotReviews.reviews.map { it.toReview() },
                                hasMoreReviews = foodSpotReviews.hasNext,
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
            }
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

    private fun FoodSpotReview.toReview(): Review =
        Review(
            userId = id,
            nickname = userInfo.nickname,
            profileImage = userInfo.profileImage,
            date = createdAt,
            rating = rate / 2f,
            reviewImages = photos.map { it.image },
            contents = contents,
        )
}

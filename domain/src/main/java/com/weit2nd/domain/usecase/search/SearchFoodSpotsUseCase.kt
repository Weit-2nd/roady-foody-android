package com.weit2nd.domain.usecase.search

import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.FoodSpot
import com.weit2nd.domain.repository.search.SearchFoodSpotsRepository
import javax.inject.Inject

class SearchFoodSpotsUseCase @Inject constructor(
    private val repository: SearchFoodSpotsRepository,
) {
    /**
     * 지도 중앙을 중심으로 음식점을 검색합니다.
     * @param centerCoordinate 기준 좌표
     * @param radius 검색 반경
     * @param name 음식점 이름. null 이면 전체를 대상으로 함
     * @param categoryIds 음식점 카테고리. null 이면 전체를 대상으로 함
     */
    suspend operator fun invoke(
        centerCoordinate: Coordinate,
        radius: Int,
        name: String?,
        categoryIds: List<Long>,
    ): List<FoodSpot> {
        return repository.getFoodSpots(
            centerCoordinate,
            radius,
            name,
            categoryIds,
        )
    }
}

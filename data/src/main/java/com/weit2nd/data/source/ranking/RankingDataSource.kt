package com.weit2nd.data.source.ranking

import com.weit2nd.data.model.ranking.RankingItemDTO
import com.weit2nd.data.service.RankingService
import javax.inject.Inject

class RankingDataSource @Inject constructor(
    private val service: RankingService,
) {
    suspend fun getTotalRanking(
        startRank: Int,
        size: Int,
    ): List<RankingItemDTO> {
        return service.getTotalRanking(
            size = size,
            start = startRank,
        )
    }

    suspend fun getReviewRanking(
        startRank: Int,
        size: Int,
    ): List<RankingItemDTO> {
        return service.getReviewRanking(
            size = size,
            start = startRank,
        )
    }

    suspend fun getReportRanking(
        startRank: Int,
        size: Int,
    ): List<RankingItemDTO> {
        return service.getReportRanking(
            size = size,
            start = startRank,
        )
    }

    suspend fun getLikeRanking(
        startRank: Int,
        size: Int,
    ): List<RankingItemDTO> {
        return service.getLikeRanking(
            size = size,
            start = startRank,
        )
    }
}

package com.weit2nd.data.repository.ranking

import com.weit2nd.data.model.ranking.RankingItemDTO
import com.weit2nd.data.source.ranking.RankingDataSource
import com.weit2nd.domain.model.RankingItem
import com.weit2nd.domain.model.ranking.RankingType
import com.weit2nd.domain.repository.ranking.RankingRepository
import javax.inject.Inject

class RankingRepositoryImpl @Inject constructor(
    private val dataSource: RankingDataSource,
) : RankingRepository {
    override suspend fun getRanking(
        rankingType: RankingType,
        startRank: Int,
        size: Int,
    ): List<RankingItem> {
        val rankingItems =
            when (rankingType) {
                RankingType.TOTAL -> {
                    dataSource.getTotalRanking(
                        startRank,
                        size,
                    )
                }
                RankingType.REVIEW -> {
                    dataSource.getReviewRanking(
                        startRank,
                        size,
                    )
                }
                RankingType.REPORT -> {
                    dataSource.getReportRanking(
                        startRank,
                        size,
                    )
                }
                RankingType.LIKE -> {
                    dataSource.getLikeRanking(
                        startRank,
                        size,
                    )
                }
            }
        return rankingItems.map { it.toRankingItem() }
    }

    private fun RankingItemDTO.toRankingItem() =
        RankingItem(
            ranking = ranking,
            userNickname = userNickname,
            userId = userId,
            profileImageUrl = profileImageUrl,
            rankChange = rankChange,
        )
}

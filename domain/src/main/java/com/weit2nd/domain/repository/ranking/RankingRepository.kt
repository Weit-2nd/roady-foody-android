package com.weit2nd.domain.repository.ranking

import com.weit2nd.domain.model.RankingItem
import com.weit2nd.domain.model.ranking.RankingType

interface RankingRepository {
    suspend fun getRanking(
        rankingType: RankingType,
        startRank: Int,
        size: Int,
    ): List<RankingItem>
}

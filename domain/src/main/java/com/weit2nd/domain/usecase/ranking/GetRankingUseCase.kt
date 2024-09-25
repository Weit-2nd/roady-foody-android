package com.weit2nd.domain.usecase.ranking

import com.weit2nd.domain.model.RankingItem
import com.weit2nd.domain.model.ranking.RankingType
import com.weit2nd.domain.repository.ranking.RankingRepository
import javax.inject.Inject

class GetRankingUseCase @Inject constructor(
    private val repository: RankingRepository,
) {
    /**
     * 랭킹 정보를 가져 옵니다.
     * @param rankingType 랭킹 유형
     * @param startRank 랭킹 검색 시작 지점
     * @param size 가져올 랭킹 사이즈
     */
    suspend operator fun invoke(
        rankingType: RankingType,
        startRank: Int,
        size: Int,
    ): List<RankingItem> {
        return repository.getRanking(
            rankingType = rankingType,
            startRank = startRank,
            size = size,
        )
    }
}

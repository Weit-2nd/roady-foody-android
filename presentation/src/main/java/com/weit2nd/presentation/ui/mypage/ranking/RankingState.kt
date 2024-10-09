package com.weit2nd.presentation.ui.mypage.ranking

import com.weit2nd.domain.model.RankingItem
import com.weit2nd.domain.model.ranking.RankingType

data class RankingState(
    val rankingTypes: List<RankingType> =
        listOf(
            RankingType.TOTAL,
            RankingType.REPORT,
            RankingType.REVIEW,
            RankingType.LIKE,
        ),
    val selectedRankingType: RankingType = RankingType.TOTAL,
    val rankingItems: List<RankingItem> = emptyList(),
)

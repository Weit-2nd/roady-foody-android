package com.weit2nd.presentation.ui.mypage.ranking

import com.weit2nd.domain.model.ranking.RankingType

sealed interface RankingIntent {
    data object CloseDialog : RankingIntent

    data class LoadNextRank(
        val rankingType: RankingType,
        val lastRank: Int?,
    ) : RankingIntent
}

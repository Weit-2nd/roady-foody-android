package com.weit2nd.presentation.ui.mypage.ranking

sealed interface RankingSideEffect {
    data object CloseDialog : RankingSideEffect

    data class ShowErrorMessage(
        val message: String,
    ) : RankingSideEffect
}

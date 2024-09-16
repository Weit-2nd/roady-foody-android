package com.weit2nd.presentation.ui.mypage

import com.weit2nd.presentation.navigation.dto.ReviewHistoryDTO

sealed class MyPageSideEffect {
    data class ShowToastMessage(
        val message: String,
    ) : MyPageSideEffect()

    data object NavToLogin : MyPageSideEffect()

    data object NavToBack : MyPageSideEffect()

    data class NavToReviewHistory(
        val reviewHistoryDTO: ReviewHistoryDTO,
    ) : MyPageSideEffect()
}

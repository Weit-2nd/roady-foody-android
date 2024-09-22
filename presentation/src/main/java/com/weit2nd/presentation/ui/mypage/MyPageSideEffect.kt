package com.weit2nd.presentation.ui.mypage

import com.weit2nd.presentation.navigation.dto.UserInfoDTO

sealed class MyPageSideEffect {
    data class ShowToastMessage(
        val message: String,
    ) : MyPageSideEffect()

    data object NavToLogin : MyPageSideEffect()

    data object NavToBack : MyPageSideEffect()

    data class NavToReviewHistory(
        val userInfoDTO: UserInfoDTO,
    ) : MyPageSideEffect()

    data class NavToFoodSpotHistory(
        val userId: Long,
    ) : MyPageSideEffect()

    data class NavToFoodSpotDetail(
        val foodSpotId: Long,
    ) : MyPageSideEffect()

    data class NavToUserInfoEdit(
        val userInfoDTO: UserInfoDTO,
    ) : MyPageSideEffect()
}

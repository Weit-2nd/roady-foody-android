package com.weit2nd.domain.usecase.user

import com.weit2nd.domain.model.UserInfo
import com.weit2nd.domain.repository.user.UserRepository
import javax.inject.Inject

class GetMyUserInfoUseCase @Inject constructor(
    private val repository: UserRepository,
) {
    /**
     * 사용자의 정보를 가져옵니다.
     * (닉네임, 프로필 사진, 코인 개수, 뱃지, 남은 리포트 수, 랭킹)
     */
    suspend operator fun invoke(): UserInfo {
        return repository.getMyUserInfo()
    }
}

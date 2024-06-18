package com.weit2nd.domain.usecase.pickimage

import com.weit2nd.domain.repository.pickimage.PickImageRepository
import javax.inject.Inject

class PickSingleImageUseCase @Inject constructor(
    private val repository: PickImageRepository,
) {

    /**
     * 이미지 선택 화면을 띄워주고 선택한 이미지를 반환합니다.
     * 선택한 이미지가 없으면 Null을 반환합니다.
     */
    suspend operator fun invoke(): String? {
        return repository.pickImage()
    }
}

package com.weit2nd.domain.usecase.pickimage

import com.weit2nd.domain.repository.pickimage.PickImageRepository
import javax.inject.Inject

class PickMultipleImagesUseCase @Inject constructor(
    private val repository: PickImageRepository,
) {
    /**
     * 이미지 선택 화면을 띄워주고 선택한 이미지들을 반환합니다.
     * @param maximumSelect 최대 선택 가능한 이미지 개수
     */
    suspend operator fun invoke(maximumSelect: Int): List<String> {
        return repository.pickImages(
            maximumSelect = maximumSelect,
        )
    }
}

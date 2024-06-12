package com.weit2nd.domain.usecase.localimage

import com.weit2nd.domain.model.localimage.LocalImage
import com.weit2nd.domain.repository.localimage.LocalImageRepository
import javax.inject.Inject

class GetLocalImagesUseCase @Inject constructor(
    private val repository: LocalImageRepository,
) {
    /**
     * 내부 저장소의 이미지를 반환 하는 메소드
     * @param path 이미지를 가져올 경로. null 또는 빈 문자열이면 전체를 조회
     * @param count 이미지를 가져올 개수. null이면 전체를 가져옴
     * @param offset 이미지를 가져올 시작 인덱스
     */
    suspend operator fun invoke(
        path: String? = null,
        count: Int? = null,
        offset: Int = 0,
    ): List<LocalImage> {
        return repository.getImages(
            path = path,
            count = count,
            offset = offset,
        )
    }
}

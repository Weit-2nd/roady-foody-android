package com.weit2nd.domain.usecase.localimage

import com.weit2nd.domain.model.localimage.LocalAlbum
import com.weit2nd.domain.repository.localimage.LocalImageRepository
import javax.inject.Inject

class GetLocalAlbumsUseCase @Inject constructor(
    private val repository: LocalImageRepository,
) {
    /**
     * 내부 저장소의 앨범 정보를 반환하는 메소드
     */
    suspend operator fun invoke(): List<LocalAlbum> {
        return repository.getAlbums()
    }
}

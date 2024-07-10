package com.weit2nd.domain.usecase.terms

import com.weit2nd.domain.model.terms.TermDetail
import com.weit2nd.domain.repository.terms.TermsRepository
import javax.inject.Inject

class GetTermDetailUseCase @Inject constructor(
    private val repository: TermsRepository,
) {
    /**
     * 특정 약관의 상세 정보를 가져 옵니다.
     * @param termId 약관 ID
     */
    suspend operator fun invoke(termId: Long): TermDetail {
        return repository.getTermDetail(termId)
    }
}

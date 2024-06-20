package com.weit2nd.domain.usecase.terms

import com.weit2nd.domain.model.terms.Term
import com.weit2nd.domain.repository.terms.TermsRepository
import javax.inject.Inject

class GetTermsUseCase @Inject constructor(
    private val repository: TermsRepository
) {

    /**
     * 약관 목록을 가져옵니다.
     */
    suspend operator fun invoke(): List<Term> {
        return repository.getTerms()
    }
}

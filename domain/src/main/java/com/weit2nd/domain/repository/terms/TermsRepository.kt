package com.weit2nd.domain.repository.terms

import com.weit2nd.domain.model.terms.Term
import com.weit2nd.domain.model.terms.TermDetail

interface TermsRepository {

    suspend fun getTerms(): List<Term>

    suspend fun getTermDetail(
        termId: Long,
    ): TermDetail
}

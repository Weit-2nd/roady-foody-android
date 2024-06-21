package com.weit2nd.data.source

import com.weit2nd.data.model.terms.TermDTO
import com.weit2nd.data.model.terms.TermDetailDTO
import com.weit2nd.data.service.TermsService
import javax.inject.Inject

class TermsDataSource @Inject constructor(
    private val service: TermsService,
) {
    suspend fun getTerms(): List<TermDTO> {
        return service.getTerms().terms
    }

    suspend fun getTermDetail(
        id: Long,
    ): TermDetailDTO {
        return service.getTermDetail(
            id = id,
        )
    }
}

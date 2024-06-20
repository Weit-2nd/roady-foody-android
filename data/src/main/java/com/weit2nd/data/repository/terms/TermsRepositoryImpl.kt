package com.weit2nd.data.repository.terms

import com.weit2nd.data.model.terms.TermDTO
import com.weit2nd.data.model.terms.TermDetailDTO
import com.weit2nd.data.source.TermsDataSource
import com.weit2nd.domain.model.terms.Term
import com.weit2nd.domain.model.terms.TermDetail
import com.weit2nd.domain.repository.terms.TermsRepository
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val dataSource: TermsDataSource,
) : TermsRepository {
    override suspend fun getTerms(): List<Term> {
        return dataSource.getTerms().toTerms()
    }

    override suspend fun getTermDetail(termId: Long): TermDetail {
        return dataSource.getTermDetail(
            id = termId
        ).toTermDetail()
    }

    private fun List<TermDTO>.toTerms(): List<Term> =
        this.map { it.toTerm() }

    private fun TermDTO.toTerm(): Term =
        Term(
            title = title,
            isRequired = isRequired,
        )

    private fun TermDetailDTO.toTermDetail() =
        TermDetail(
            id = id,
            title = title,
            isRequired = isRequired,
            content = content,
        )
}

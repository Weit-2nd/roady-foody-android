package com.weit2nd.data.repository.terms

import com.weit2nd.data.model.terms.TermDTO
import com.weit2nd.data.model.terms.TermDetailDTO
import com.weit2nd.data.source.TermsDataSource
import com.weit2nd.domain.exception.term.TermIdNotFoundException
import com.weit2nd.domain.model.terms.Term
import com.weit2nd.domain.model.terms.TermDetail
import com.weit2nd.domain.repository.terms.TermsRepository
import okhttp3.internal.http.HTTP_NOT_FOUND
import retrofit2.HttpException
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val dataSource: TermsDataSource,
) : TermsRepository {
    override suspend fun getTerms(): List<Term> {
        return dataSource.getTerms().toTerms()
    }

    override suspend fun getTermDetail(termId: Long): TermDetail {
        val result = runCatching {
            dataSource.getTermDetail(
                id = termId
            ).toTermDetail()
        }.onFailure {
            if (it is HttpException && it.code() == HTTP_NOT_FOUND) {
                throw TermIdNotFoundException()
            }
        }
        return result.getOrThrow()
    }

    private fun List<TermDTO>.toTerms(): List<Term> =
        this.map { it.toTerm() }

    private fun TermDTO.toTerm(): Term =
        Term(
            id = id,
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

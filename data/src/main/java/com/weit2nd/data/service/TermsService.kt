package com.weit2nd.data.service

import com.weit2nd.data.model.terms.TermDetailDTO
import com.weit2nd.data.model.terms.TermsDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface TermsService {

    @GET("/api/v1/terms")
    suspend fun getTerms(): TermsDTO

    @GET("/api/v1/terms/{termId}")
    suspend fun getTermDetail(
        @Path("termId") id: Long,
    ): TermDetailDTO
}

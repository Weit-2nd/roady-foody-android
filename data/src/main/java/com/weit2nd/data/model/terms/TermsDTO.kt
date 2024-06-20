package com.weit2nd.data.model.terms

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TermsDTO(
    @field:Json(name = "allTermsSize") val allTermsSize: Int,
    @field:Json(name = "requiredTermsSize") val requiredTermsSize: Int,
    @field:Json(name = "optionalTermsSize") val optionalTermsSize: Int,
    @field:Json(name = "terms") val terms: List<TermDTO>
)

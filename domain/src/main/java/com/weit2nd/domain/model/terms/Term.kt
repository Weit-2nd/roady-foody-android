package com.weit2nd.domain.model.terms

/**
 * @param id 약관 ID
 * @param title 약관 제목
 * @param isRequired 필수 여부. true 라면 꼭 동의를 받아야 하는 약관
 */
data class Term(
    val id: Long,
    val title: String,
    val isRequired: Boolean,
)

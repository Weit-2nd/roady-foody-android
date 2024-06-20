package com.weit2nd.domain.model.terms

/**
 * @param id 약관 ID
 * @param title 약관 제목
 * @param isRequired 필수 여부. true 라면 꼭 동의를 받아야 하는 약관
 */
data class Term(
    val id: Long = 1L, // TODO 현재 약관 아뒤가 안내려 와서 기본값을 넣었는데 API 수정되는대로 제거
    val title: String,
    val isRequired: Boolean,
)

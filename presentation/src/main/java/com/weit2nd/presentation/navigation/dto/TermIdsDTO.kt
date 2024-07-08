package com.weit2nd.presentation.navigation.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TermIdsDTO(
    val ids: List<Long>,
) : Parcelable

fun TermIdsDTO.toTermIds(): List<Long> = ids

fun List<Long>.toTermIdsDTO(): TermIdsDTO = TermIdsDTO(this)

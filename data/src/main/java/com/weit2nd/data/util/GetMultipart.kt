package com.weit2nd.data.util

import com.squareup.moshi.JsonAdapter
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

private val defaultMediaType = "application/json".toMediaType()

fun <T> JsonAdapter<T>.getMultiPart(
    formDataName: String,
    fileName: String,
    request: T,
    mediaType: MediaType = defaultMediaType,
): MultipartBody.Part {
    val requestBody = this.toJson(request).toRequestBody(mediaType)
    return MultipartBody.Part.createFormData(
        name = formDataName,
        filename = fileName,
        body = requestBody,
    )
}

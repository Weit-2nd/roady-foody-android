package com.weit2nd.domain.exception.imageuri

sealed class ImageUriException : Throwable() {

    class EmptyUriException(override val message: String = "빈 uri값입니다.") : ImageUriException()
    class NotImageException(override val message: String = "이미지가 아닙니다.") : ImageUriException()
}

package com.weit2nd.domain.exception

sealed class SearchPlaceWithCoordinateException(
    message: String,
) : Throwable(message) {
    class CanNotChangeToAddressException(
        message: String,
    ) : SearchPlaceWithCoordinateException(message)

    class ExternalApiException(
        message: String,
    ) : SearchPlaceWithCoordinateException(message)
}

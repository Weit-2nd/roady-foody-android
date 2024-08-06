package com.weit2nd.domain.exception.spot

sealed class UpdateFoodSpotReportException(
    message: String,
) : Throwable(message) {
    class InvalidFoodSpotNameException(
        message: String,
    ) : UpdateFoodSpotReportException(message)

    class NotFoundFoodCategoryException(
        message: String,
    ) : UpdateFoodSpotReportException(message)

    class FoodSpotAlreadyClosedException(
        message: String,
    ) : UpdateFoodSpotReportException(message)

    class TooManyReportRequestException(
        message: String,
    ) : UpdateFoodSpotReportException(message)
}

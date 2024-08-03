package com.weit2nd.domain.exception

sealed class DeleteFoodSpotHistoryException(
    message: String,
) : Throwable(message) {
    class NotHistoryOwnerException(
        message: String,
    ) : DeleteFoodSpotHistoryException(message)

    class HistoryNotFoundException(
        message: String,
    ) : DeleteFoodSpotHistoryException(message)
}

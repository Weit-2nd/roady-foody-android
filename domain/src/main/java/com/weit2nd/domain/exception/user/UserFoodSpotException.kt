package com.weit2nd.domain.exception.user

sealed class UserFoodSpotException : Exception() {
    class NoMoreFoodSpotException : UserFoodSpotException()
}

package com.weit2nd.data.source

import com.weit2nd.data.model.UserDTO
import com.weit2nd.data.util.ActivityProvider
import kotlinx.coroutines.delay
import javax.inject.Inject

class LoginDataSource @Inject constructor(
    private val activityProvider: ActivityProvider,
) {

    suspend fun login(): UserDTO {
        delay(1000)
        return UserDTO(
            name = "으아악"
        )
    }
}

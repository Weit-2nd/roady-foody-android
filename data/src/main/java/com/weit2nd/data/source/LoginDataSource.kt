package com.weit2nd.data.source

import com.weit2nd.data.model.UserDTO
import kotlinx.coroutines.delay

class LoginDataSource {
    suspend fun login(): UserDTO {
        delay(1000)
        return UserDTO(
            name = "으아악"
        )
    }
}

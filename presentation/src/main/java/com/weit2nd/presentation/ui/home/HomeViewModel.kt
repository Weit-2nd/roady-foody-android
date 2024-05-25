package com.weit2nd.presentation.ui.home

import androidx.lifecycle.SavedStateHandle
import com.weit2nd.presentation.base.BaseViewModel
import com.weit2nd.presentation.navigation.HomeNavRoutes
import com.weit2nd.presentation.navigation.dto.UserDTO
import com.weit2nd.presentation.navigation.dto.toUser
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<HomeState, HomeSideEffect>() {
    override val container =
        container<HomeState, HomeSideEffect>(
            HomeState(
                user = checkNotNull(
                    savedStateHandle.get<UserDTO>(HomeNavRoutes.USER_STATE_KEY)?.toUser()
                )
            )
        )
}

package com.weit2nd.presentation.ui.select.location

import com.weit2nd.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SelectLocationViewModel @Inject constructor() :
    BaseViewModel<SelectLocationState, SelectLocationSideEffect>() {
    override val container =
        container<SelectLocationState, SelectLocationSideEffect>(SelectLocationState())
}

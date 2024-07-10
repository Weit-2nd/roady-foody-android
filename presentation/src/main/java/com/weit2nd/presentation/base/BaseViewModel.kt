package com.weit2nd.presentation.base

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost

abstract class BaseViewModel<S : Any, E : Any> :
    ViewModel(),
    ContainerHost<S, E>

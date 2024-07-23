package com.weit2nd.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun <T : Any> NavController.ObserveSavedState(
    lifecycleOwner: LifecycleOwner,
    key: String,
    observe: (T) -> Unit,
) {
    val previousValue = remember { mutableStateOf<T?>(null) }
    this
        .currentBackStackEntryAsState()
        .value
        ?.savedStateHandle
        ?.getLiveData<T>(key)
        ?.observe(lifecycleOwner) { newValue ->
            if (previousValue.value != newValue) {
                previousValue.value = newValue
                observe(newValue)
            }
        }
}

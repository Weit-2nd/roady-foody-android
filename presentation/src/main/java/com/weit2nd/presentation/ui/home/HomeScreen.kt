package com.weit2nd.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
) {
    val state = vm.collectAsState()
    Surface {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = state.value.user.name,
            )
        }
    }
}

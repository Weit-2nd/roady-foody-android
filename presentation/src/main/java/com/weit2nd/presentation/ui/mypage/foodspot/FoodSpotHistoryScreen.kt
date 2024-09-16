package com.weit2nd.presentation.ui.mypage.foodspot

import android.widget.Toast
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.BackTopBar
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun FoodSpotHistoryScreen(
    vm: FoodSpotHistoryViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val state by vm.collectAsState()
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            FoodSpotHistorySideEffect.NavToBack -> {
                navToBack()
            }

            FoodSpotHistorySideEffect.ShowNetworkErrorMessage -> {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.error_network),
                        Toast.LENGTH_SHORT,
                    ).show()
            }
        }
    }

    val lastVisibleItemIndex by remember {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo
                .lastOrNull()
                ?.index ?: 0
        }
    }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    LaunchedEffect(lastVisibleItemIndex) {
        vm.onLastVisibleItemChanged(lastVisibleItemIndex)
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(id = R.string.food_spot_history_title),
                onClickBackBtn = vm::onNavigationClick,
            )
        },
    ) {
        // foodSpots
    }
}

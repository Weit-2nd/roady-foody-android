package com.weit2nd.presentation.ui.mypage.foodspot

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.BackTopBar
import com.weit2nd.presentation.ui.common.TotalCount
import com.weit2nd.presentation.ui.mypage.FoodSpotItem
import com.weit2nd.presentation.ui.theme.Gray4
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun FoodSpotHistoryScreen(
    vm: FoodSpotHistoryViewModel = hiltViewModel(),
    navToBack: () -> Unit,
    navToFoodSpotDetail: (Long) -> Unit,
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

            is FoodSpotHistorySideEffect.NavToFoodSpotDetail -> {
                navToFoodSpotDetail(sideEffect.foodSpotId)
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
        FoodSpotHistoryContent(
            modifier =
                Modifier.padding(
                    top = it.calculateTopPadding(),
                ),
            totalCount = state.totalCount,
            foodSpots = state.foodSpots,
            lazyListState = lazyListState,
            onFoodSpotContentClick = vm::onFoodSpotContentClick,
        )
    }
}

@Composable
private fun FoodSpotHistoryContent(
    modifier: Modifier = Modifier,
    totalCount: Int,
    foodSpots: List<FoodSpotHistoryContent>,
    lazyListState: LazyListState = rememberLazyListState(),
    onFoodSpotContentClick: (Long) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        TotalCount(
            modifier = Modifier.padding(horizontal = 16.dp),
            count = totalCount,
        )
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(
            color = Gray4,
        )
        LazyColumn(
            state = lazyListState,
        ) {
            itemsIndexed(foodSpots) { index, foodSpot ->

                FoodSpotItem(
                    modifier = Modifier.clickable { onFoodSpotContentClick(foodSpot.foodSpotsId) },
                    foodSpot = foodSpot,
                )

                if (index < foodSpots.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Gray4,
                    )
                }
            }
        }
    }
}

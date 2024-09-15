package com.weit2nd.presentation.ui.mypage.review

import android.widget.Toast
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
import com.weit2nd.presentation.R
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.ui.common.BackTopBar
import com.weit2nd.presentation.ui.common.ReviewItem
import com.weit2nd.presentation.ui.common.TotalCount
import com.weit2nd.presentation.ui.theme.Gray4
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun ReviewHistoryScreen(
    vm: ReviewHistoryViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val state by vm.collectAsState()
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            ReviewHistorySideEffect.NavToBack -> {
                navToBack()
            }
            ReviewHistorySideEffect.ShowNetworkErrorMessage -> {
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.error_network),
                        Toast.LENGTH_SHORT,
                    ).show()
            }
        }
    }

    val firstVisibleItemIndex by
        remember {
            derivedStateOf {
                lazyListState.firstVisibleItemIndex
            }
        }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    LaunchedEffect(firstVisibleItemIndex) {
        vm.onFirstVisibleItemChanged(firstVisibleItemIndex)
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(id = R.string.review_history_title),
                onClickBackBtn = vm::onNavigationClick,
            )
        },
    ) {
        ReviewHistoryContent(
            modifier = Modifier.padding(it),
            reviews = state.reviews,
            lazyListState = lazyListState,
        )
    }
}

@Composable
fun ReviewHistoryContent(
    modifier: Modifier = Modifier,
    reviews: List<Review>,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    Column(
        modifier = modifier,
    ) {
        TotalCount(
            modifier = Modifier.padding(horizontal = 16.dp),
            count = reviews.size,
        )
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(
            color = Gray4,
        )
        LazyColumn(
            state = lazyListState,
        ) {
            itemsIndexed(reviews) { index, review ->
                ReviewItem(
                    review = review,
                    onImageClick = { _, _ ->
                        // TODO 이미지 상세 이동
                    },
                )
                if (index < reviews.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = Gray4,
                    )
                }
            }
        }
    }
}

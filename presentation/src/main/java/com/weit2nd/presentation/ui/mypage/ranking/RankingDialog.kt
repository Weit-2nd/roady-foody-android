package com.weit2nd.presentation.ui.mypage.ranking

import android.widget.Toast
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.RankingItem
import com.weit2nd.domain.model.ranking.RankingType
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.BottomButton
import com.weit2nd.presentation.ui.common.CommonDialogColumn
import com.weit2nd.presentation.ui.common.ProfileImage
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray4
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun RankingDialog(
    modifier: Modifier = Modifier,
    vm: RankingViewModel = hiltViewModel(),
    onDismiss: (() -> Unit)? = null,
    onCloseButtonClick: () -> Unit,
    dialogProperties: DialogProperties = DialogProperties(),
) {
    val state by vm.collectAsState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            RankingSideEffect.CloseDialog -> onCloseButtonClick()
            is RankingSideEffect.ShowErrorMessage -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    val lazyListState = rememberLazyListState()
    val lastVisibleItemIndex by
        remember {
            derivedStateOf {
                lazyListState.layoutInfo.visibleItemsInfo
                    .lastOrNull()
                    ?.index ?: 0
            }
        }
    val localConfiguration = LocalConfiguration.current
    val rankingItemsHeight by remember {
        derivedStateOf {
            (localConfiguration.screenHeightDp / 2).dp
        }
    }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    LaunchedEffect(lastVisibleItemIndex) {
        vm.onLastVisibleItemChanged(lastVisibleItemIndex)
    }

    CommonDialogColumn(
        modifier = modifier,
        onDismiss = onDismiss,
        dialogProperties = dialogProperties,
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.ranking_title),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(32.dp))
        RankingTabRow(
            modifier = Modifier.fillMaxWidth(),
            rankingTypes = state.rankingTypes,
            selectedRankingType = state.selectedRankingType,
            onTabChanged = vm::onTabChanged,
        )
        RankingItems(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(rankingItemsHeight),
            contentPadding =
                PaddingValues(
                    top = 8.dp,
                ),
            state = lazyListState,
            rankingItems = state.rankingItems,
        )
        Spacer(modifier = Modifier.height(16.dp))
        BottomButton(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                    ),
            onClick = vm::onCloseButtonClick,
            text = stringResource(id = R.string.ranking_close),
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun RankingTabRow(
    modifier: Modifier = Modifier,
    rankingTypes: List<RankingType>,
    selectedRankingType: RankingType,
    onTabChanged: (RankingType) -> Unit,
) {
    TabRow(
        modifier = modifier,
        selectedTabIndex = rankingTypes.indexOf(selectedRankingType),
        containerColor = MaterialTheme.colorScheme.surface,
        divider = {
            HorizontalDivider(
                thickness = 3.dp,
                color = Gray4,
            )
        },
    ) {
        rankingTypes.forEach { rankingType ->
            Tab(
                selected = rankingType == selectedRankingType,
                onClick = { onTabChanged(rankingType) },
                selectedContentColor = MaterialTheme.colorScheme.onSurface,
                unselectedContentColor = Gray1,
            ) {
                Text(
                    text = stringResource(id = rankingType.getTitleRes()),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

private fun RankingType.getTitleRes() =
    when (this) {
        RankingType.TOTAL -> R.string.ranking_total
        RankingType.REVIEW -> R.string.ranking_review
        RankingType.REPORT -> R.string.ranking_report
        RankingType.LIKE -> R.string.ranking_like
    }

@Composable
private fun RankingItems(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    rankingItems: List<RankingItem>,
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
    ) {
        items(
            items = rankingItems,
            key = { it.userId },
        ) { rankingItem ->
            RankingItem(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 16.dp,
                            horizontal = 24.dp,
                        ),
                rankingItem = rankingItem,
            )
        }
    }
}

@Composable
private fun RankingItem(
    modifier: Modifier = Modifier,
    rankingItem: RankingItem,
) {
    val rankingTextSize = MaterialTheme.typography.titleMedium.fontSize.value
    val rankingTextWidth =
        with(LocalDensity.current) {
            rankingTextSize.sp.toDp()
        } * 2
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.width(rankingTextWidth),
            text = rankingItem.ranking.toString(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.width(16.dp))
        ProfileImage(
            modifier = Modifier.size(32.dp),
            imgUri = rankingItem.profileImageUrl.toUri(),
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = rankingItem.userNickname,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.width(16.dp))
        RankChangeIcon(
            modifier = Modifier.size(32.dp),
            rankChange = rankingItem.rankChange,
        )
    }
}

@Composable
private fun RankChangeIcon(
    modifier: Modifier = Modifier,
    rankChange: Int,
) {
    val rankIconRes =
        when {
            rankChange > 0 -> R.drawable.ic_rank_up
            rankChange < 0 -> R.drawable.ic_rank_down
            else -> R.drawable.ic_rank_idle
        }
    val rankIconTint =
        when {
            rankChange > 0 -> MaterialTheme.colorScheme.primary
            rankChange < 0 -> MaterialTheme.colorScheme.tertiary
            else -> Gray1
        }
    Icon(
        modifier = modifier,
        painter = painterResource(id = rankIconRes),
        contentDescription = null,
        tint = rankIconTint,
    )
}

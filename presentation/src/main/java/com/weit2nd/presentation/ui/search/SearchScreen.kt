package com.weit2nd.presentation.ui.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place
import com.weit2nd.domain.model.search.SearchHistory
import com.weit2nd.presentation.R
import com.weit2nd.presentation.model.foodspot.SearchPlaceResult
import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO
import com.weit2nd.presentation.ui.common.SearchTopBar
import com.weit2nd.presentation.ui.theme.DarkGray
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray4
import com.weit2nd.presentation.ui.theme.Gray5
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import com.weit2nd.presentation.util.getDistanceString
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchScreen(
    vm: SearchViewModel = hiltViewModel(),
    navToHome: (PlaceSearchDTO) -> Unit,
    navToBack: () -> Unit,
) {
    val state by vm.collectAsState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            SearchSideEffect.NavToBack -> {
                navToBack()
            }
            is SearchSideEffect.ShowToastMessage -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
            is SearchSideEffect.NavToHome -> {
                navToHome(sideEffect.placeSearch)
            }
        }
    }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Column {
                SearchTopBar(
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                    searchWords = state.searchWords,
                    onClear = vm::onSearchWordsClear,
                    onSearchButtonClick = vm::onSearchButtonClick,
                    onSearchWordsChanged = vm::onSearchWordsChanged,
                    onNavigationButtonClick = vm::onNavigationButtonClick,
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    color = Gray4,
                )
            }
        },
    ) {
        SearchContent(
            modifier = Modifier.padding(it),
            state = state,
            onHistoryClick = vm::onHistoryClick,
            onHistoryRemove = vm::onHistoryRemove,
            onPlaceClick = vm::onSearchResultClick,
        )
    }
}

@Composable
private fun SearchContent(
    modifier: Modifier = Modifier,
    state: SearchState,
    onHistoryClick: (SearchHistory) -> Unit,
    onHistoryRemove: (SearchHistory) -> Unit,
    onPlaceClick: (Place) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        if (state.histories.isNotEmpty()) {
            item {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(id = R.string.search_place_history),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
        itemsIndexed(state.histories) { index, history ->
            if (index > 0) {
                HorizontalDivider(
                    color = Gray4,
                    thickness = 1.dp,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                )
            }
            SearchHistoryItem(
                modifier = Modifier.fillMaxSize(),
                history = history,
                onClick = {
                    onHistoryClick(history)
                },
                onRemove = {
                    onHistoryRemove(history)
                },
            )
        }
        if (state.histories.isNotEmpty() && state.searchResults.isNotEmpty()) {
            item {
                HorizontalDivider(
                    color = Gray5,
                    thickness = 8.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        itemsIndexed(state.searchResults) { index, searchPlaceResult ->
            if (index > 0) {
                HorizontalDivider(
                    color = Gray4,
                    thickness = 1.dp,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                )
            }
            SearchPlaceItem(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                name = searchPlaceResult.place.placeName,
                address = searchPlaceResult.place.roadAddressName.takeIf { it.isNotEmpty() } ?: searchPlaceResult.place.addressName,
                distanceMeter = searchPlaceResult.distance,
                onClick = {
                    onPlaceClick(searchPlaceResult.place)
                },
            )
        }
    }
}

@Composable
private fun SearchPlaceItem(
    modifier: Modifier = Modifier,
    name: String,
    address: String,
    distanceMeter: Int,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            modifier.clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.ic_search_result_marker),
            tint = Color.Unspecified,
            contentDescription = "검색 결과",
        )
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
        ) {
            Text(
                text = name,
                color = Color.Black,
                fontSize = 18.sp,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = address,
                color = Color.Gray,
                fontSize = 12.sp,
            )
        }
        Text(
            text = getDistanceString(LocalContext.current, distanceMeter),
            style = MaterialTheme.typography.bodyMedium,
            color = DarkGray,
        )
    }
}

@Composable
private fun SearchHistoryItem(
    modifier: Modifier = Modifier,
    history: SearchHistory,
    onClick: () -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val iconRes =
            if (history.isPlace) {
                R.drawable.ic_search_history_marker
            } else {
                R.drawable.ic_search_glass
            }
        val tint =
            if (history.isPlace) {
                Color.Unspecified
            } else {
                Gray1
            }
        Icon(
            modifier =
                Modifier
                    .padding(horizontal = 16.dp)
                    .size(32.dp),
            painter = painterResource(id = iconRes),
            tint = tint,
            contentDescription = "검색 결과",
        )
        Text(
            modifier =
                Modifier
                    .padding(vertical = 16.dp)
                    .weight(1f)
                    .clickable {
                        onClick()
                    },
            text = history.words,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = onRemove,
        ) {
            Icon(
                modifier = Modifier.padding(0.dp),
                painter = painterResource(id = R.drawable.ic_input_delete_filled),
                tint = Color.Unspecified,
                contentDescription = "제거",
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    RoadyFoodyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            SearchContent(
                state =
                    SearchState(
                        histories =
                            listOf(
                                SearchHistory(
                                    "안녕하세요",
                                    Coordinate(0.0, 0.0),
                                    true,
                                ),
                                SearchHistory(
                                    "감사해요",
                                    Coordinate(0.0, 0.0),
                                    false,
                                ),
                                SearchHistory(
                                    "잘있어요",
                                    Coordinate(0.0, 0.0),
                                    true,
                                ),
                            ),
                        searchResults =
                            listOf(
                                SearchPlaceResult(
                                    Place(
                                        placeName = "aaa",
                                        addressName = "aaa",
                                        roadAddressName = "Aaa",
                                        longitude = 0.0,
                                        latitude = 0.0,
                                        tel = "",
                                    ),
                                    100,
                                ),
                                SearchPlaceResult(
                                    Place(
                                        placeName = "aaa",
                                        addressName = "aaa",
                                        roadAddressName = "Aaa",
                                        longitude = 0.0,
                                        latitude = 0.0,
                                        tel = "",
                                    ),
                                    1234,
                                ),
                                SearchPlaceResult(
                                    Place(
                                        placeName = "aaa",
                                        addressName = "aaa",
                                        roadAddressName = "Aaa",
                                        longitude = 0.0,
                                        latitude = 0.0,
                                        tel = "",
                                    ),
                                    500000,
                                ),
                            ),
                    ),
                onHistoryRemove = {},
                onHistoryClick = {},
                onPlaceClick = {},
            )
        }
    }
}

@Preview
@Composable
private fun SearchHistoryItemPreview() {
    RoadyFoodyTheme {
        SearchHistoryItem(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            history =
                SearchHistory(
                    "명륜진사",
                    Coordinate(0.0, 0.0),
                    true,
                ),
            onClick = {},
            onRemove = {},
        )
    }
}

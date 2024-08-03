package com.weit2nd.presentation.ui.search

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.search.Place
import com.weit2nd.presentation.R
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SearchScreen(
    vm: SearchViewModel = hiltViewModel(),
    navToMap: () -> Unit,
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
        }
    }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchTopBar(
                modifier = Modifier.background(Color.White),
                searchWords = state.searchWords,
                onClear = vm::onSearchWordsClear,
                onSearchButtonClick = vm::onSearchButtonClick,
                onSearchWordsChanged = vm::onSearchWordsChanged,
                onNavigationButtonClick = vm::onNavigationButtonClick,
            )
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
fun SearchTopBar(
    modifier: Modifier = Modifier,
    textFieldEnabled: Boolean = true,
    searchWords: String,
    onClear: () -> Unit,
    onSearchButtonClick: () -> Unit,
    onSearchWordsChanged: (String) -> Unit,
    onNavigationButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onNavigationButtonClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_navigate),
                contentDescription = "",
            )
        }
        SearchTextField(
            modifier = Modifier.weight(1f),
            enabled = textFieldEnabled,
            searchWords = searchWords,
            onSearchWordsChanged = onSearchWordsChanged,
            onClear = onClear,
            onSearchButtonClick = onSearchButtonClick,
        )
    }
}

@Composable
private fun SearchContent(
    modifier: Modifier = Modifier,
    state: SearchState,
    onHistoryClick: (String) -> Unit,
    onHistoryRemove: (String) -> Unit,
    onPlaceClick: (Place) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        if (state.histories.isNotEmpty()) {
            item {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "최근 검색",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        itemsIndexed(state.histories) { index, history ->
            if (index > 0) {
                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            SearchHistoryItem(
                modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
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
                    color = Color.Gray,
                    thickness = 4.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
        if (state.searchResults.isNotEmpty()) {
            item {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = "검색 결과",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        itemsIndexed(state.searchResults) { index, place ->
            if (index > 0) {
                HorizontalDivider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            SearchPlaceItem(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                name = place.placeName,
                address = place.roadAddressName.takeIf { it.isNotEmpty() } ?: place.addressName,
                onClick = {
                    onPlaceClick(place)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    searchWords: String,
    onSearchWordsChanged: (String) -> Unit,
    onClear: () -> Unit,
    onSearchButtonClick: () -> Unit,
) {
    val interactionSource =
        remember {
            MutableInteractionSource()
        }
    val hasFocus by interactionSource.collectIsFocusedAsState()

    BasicTextField(
        modifier = modifier,
        value = searchWords,
        onValueChange = onSearchWordsChanged,
        interactionSource = interactionSource,
        keyboardOptions =
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
            ),
        keyboardActions =
            KeyboardActions(
                onSearch = {
                    onSearchButtonClick()
                },
            ),
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = searchWords,
            innerTextField = innerTextField,
            enabled = enabled,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = remember { MutableInteractionSource() },
            placeholder = {
                Text(
                    text = "장소를 입력해!",
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_glass),
                    contentDescription = "장소 검색 바",
                )
            },
            trailingIcon = {
                if (searchWords.isNotEmpty() && hasFocus) {
                    IconButton(
                        onClick = onClear,
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_input_delete_filled),
                            contentDescription = "검색어 정리",
                            tint = Color.Unspecified,
                        )
                    }
                }
            },
            // TODO Material Theme를 적용하고 나면 제거
            colors =
                TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    errorContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    disabledIndicatorColor = Color.White,
                    focusedIndicatorColor = Color(0xFF555555),
                ),
        )
    }
}

@Composable
private fun SearchPlaceItem(
    modifier: Modifier = Modifier,
    name: String,
    address: String,
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
        Column {
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
        Icon(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.ic_search_glass),
            contentDescription = "검색 결과",
        )
    }
}

@Composable
private fun SearchHistoryItem(
    modifier: Modifier = Modifier,
    history: String,
    onClick: () -> Unit,
    onRemove: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier =
            Modifier
                .weight(1f)
                .clickable {
                    onClick()
                },
            text = history,
            fontSize = 18.sp,
        )
        IconButton(
            modifier = Modifier.size(48.dp),
            onClick = onRemove,
        ) {
            Icon(
                modifier = Modifier.padding(0.dp),
                painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                contentDescription = "제거",
            )
        }
    }
}

@Preview
@Composable
private fun SearchContentPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        SearchContent(
            state =
                SearchState(
                    histories = listOf("안녕하세요", "감사해요", "잘있어요"),
                    searchResults =
                        listOf(
                            Place(
                                placeName = "aaa",
                                addressName = "aaa",
                                roadAddressName = "Aaa",
                                longitude = 0.0,
                                latitude = 0.0,
                                tel = "",
                            ),
                            Place(
                                placeName = "aaa",
                                addressName = "aaa",
                                roadAddressName = "Aaa",
                                longitude = 0.0,
                                latitude = 0.0,
                                tel = "",
                            ),
                            Place(
                                placeName = "aaa",
                                addressName = "aaa",
                                roadAddressName = "Aaa",
                                longitude = 0.0,
                                latitude = 0.0,
                                tel = "",
                            ),
                        ),
                ),
            onHistoryRemove = {},
            onHistoryClick = {},
            onPlaceClick = {},
        )
    }
}

@Preview
@Composable
private fun SearchHistoryItemPreview() {
    SearchHistoryItem(
        modifier =
        Modifier
            .fillMaxWidth()
            .background(Color.White),
        history = "명륜진사",
        onClick = {},
        onRemove = {},
    )
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    SearchTextField(
        modifier = Modifier.fillMaxWidth(),
        searchWords = "안녕하세요",
        onSearchWordsChanged = {},
        onClear = {},
        onSearchButtonClick = {},
    )
}

@Preview
@Composable
private fun SearchTopBarPerview() {
    SearchTopBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        searchWords = "감사해요",
        onClear = {},
        onSearchButtonClick = {},
        onSearchWordsChanged = {},
        onNavigationButtonClick = {},
    )
}

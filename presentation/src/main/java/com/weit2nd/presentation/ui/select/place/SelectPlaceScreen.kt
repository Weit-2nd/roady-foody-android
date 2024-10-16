package com.weit2nd.presentation.ui.select.place

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.search.Place
import com.weit2nd.presentation.R
import com.weit2nd.presentation.navigation.SelectPlaceMapRoutes
import com.weit2nd.presentation.navigation.dto.PlaceDTO
import com.weit2nd.presentation.navigation.dto.toPlace
import com.weit2nd.presentation.ui.common.BackTopBar
import com.weit2nd.presentation.ui.common.BottomButton
import com.weit2nd.presentation.ui.theme.Black
import com.weit2nd.presentation.ui.theme.DarkGray
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.Gray4
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import com.weit2nd.presentation.util.ObserveSavedState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SelectPlaceScreen(
    vm: SelectPlaceViewModel = hiltViewModel(),
    navToMap: (Coordinate) -> Unit,
    onSelectPlace: (Place) -> Unit,
    navController: NavController,
    navToBack: () -> Unit,
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SelectPlaceSideEffect.SelectPlace -> {
                onSelectPlace(sideEffect.place)
            }

            SelectPlaceSideEffect.NavToBack -> {
                navToBack()
            }

            is SelectPlaceSideEffect.NavToMap -> {
                navToMap(sideEffect.coordinate)
            }
        }
    }

    Scaffold(
        topBar = {
            BackTopBar(
                modifier = Modifier.background(MaterialTheme.colorScheme.surface),
                title = stringResource(R.string.select_place_screen_toolbar_title),
                onClickBackBtn = vm::onClickBackBtn,
            )
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
            ) {
                LocationTextField(
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = vm::onValueChange,
                    onSearch = vm::onLocationSearch,
                )

                BottomButton(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                    onClick = vm::onClickSearchWithMapBtn,
                    text = stringResource(R.string.select_place_find_with_map),
                    textStyle = MaterialTheme.typography.titleSmall,
                )

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(state.value.searchResults) { item ->
                        SearchPlaceItem(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .height(72.dp),
                            place = item,
                            onClickPlace = vm::onClickPlace,
                        )
                        HorizontalDivider(color = Gray4)
                    }
                }
            }
        },
    )

    val lifecycleOwner = LocalLifecycleOwner.current
    navController.ObserveSavedState<PlaceDTO>(
        lifecycleOwner = lifecycleOwner,
        key = SelectPlaceMapRoutes.SELECT_PLACE_KEY,
    ) {
        vm.onSelectPlace(it.toPlace())
    }
}

@Composable
private fun LocationTextField(
    modifier: Modifier,
    onValueChange: (String) -> Unit,
    onSearch: () -> Unit,
) {
    var userInput by remember { mutableStateOf(TextFieldValue()) }
    TextField(
        modifier = modifier,
        value = userInput,
        keyboardOptions =
            KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search,
            ),
        keyboardActions =
            KeyboardActions(
                onSearch = { onSearch() },
            ),
        placeholder = {
            Text(
                text = stringResource(R.string.select_place_search_placeholder),
                style = MaterialTheme.typography.bodyLarge,
                color = Gray2,
            )
        },
        colors =
            TextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Gray2,
                disabledIndicatorColor = Gray2,
                focusedIndicatorColor = DarkGray,
            ),
        onValueChange = { newValue ->
            userInput = newValue
            onValueChange(newValue.text)
        },
        leadingIcon = {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Filled.Search,
                contentDescription = null,
                tint = Gray2,
            )
        },
    )
}

@Composable
private fun SearchPlaceItem(
    modifier: Modifier = Modifier,
    place: Place,
    onClickPlace: (Place) -> Unit,
) {
    Column(
        modifier =
            modifier
                .clickable {
                    onClickPlace(place)
                },
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = place.placeName,
            style = MaterialTheme.typography.titleSmall,
            color = Black,
        )
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = place.addressName,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LocationTextFieldPreview() {
    RoadyFoodyTheme {
        LocationTextField(modifier = Modifier, onValueChange = { _ -> }, onSearch = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchPlaceItemPreview() {
    RoadyFoodyTheme {
        SearchPlaceItem(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(72.dp),
            place =
                Place(
                    "김포국제공항 국내선",
                    "서울 강서구 공항동 1373",
                    "",
                    0.0,
                    0.0,
                    "",
                ),
        ) {}
    }
}

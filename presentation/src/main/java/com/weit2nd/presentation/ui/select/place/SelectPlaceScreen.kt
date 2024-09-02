package com.weit2nd.presentation.ui.select.place

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.weit2nd.domain.model.search.Place
import com.weit2nd.presentation.navigation.SelectPlaceMapRoutes
import com.weit2nd.presentation.navigation.dto.PlaceDTO
import com.weit2nd.presentation.navigation.dto.toPlace
import com.weit2nd.presentation.ui.theme.DarkGray
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import com.weit2nd.presentation.util.ObserveSavedState
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun SelectPlaceScreen(
    vm: SelectPlaceViewModel = hiltViewModel(),
    navToMap: () -> Unit,
    onSelectPlace: (Place) -> Unit,
    navController: NavController,
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is SelectPlaceSideEffect.SelectPlace -> {
                onSelectPlace(sideEffect.place)
            }
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
    ) {
        LocationTextField(
            modifier = Modifier.fillMaxWidth(),
            onValueChange = vm::onValueChange,
            onSearch = vm::onLocationSearch,
        )
        SearchWithMapButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            navToMap = navToMap,
        )
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(state.value.searchResults) { item ->
                SearchPlaceItem(item, vm::onClickPlace)
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    navController.ObserveSavedState<PlaceDTO>(
        lifecycleOwner = lifecycleOwner,
        key = SelectPlaceMapRoutes.SELECT_PLACE_KEY,
    ) {
        vm.onClickPlace(it.toPlace())
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
                text = "지번, 도로명, 건물명으로 검색",
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
private fun SearchWithMapButton(
    modifier: Modifier = Modifier,
    navToMap: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = { navToMap() },
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
    ) {
        Text(
            text = "지도에서 찾기",
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Composable
private fun SearchPlaceItem(
    place: Place,
    onClickPlace: (Place) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClickPlace(place)
                },
    ) {
        Text(
            text = place.placeName,
            fontSize = 20.sp,
        )
        Text(
            text = place.addressName,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
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
private fun SearchWithMapButtonPreview() {
    RoadyFoodyTheme {
        SearchWithMapButton {}
    }
}

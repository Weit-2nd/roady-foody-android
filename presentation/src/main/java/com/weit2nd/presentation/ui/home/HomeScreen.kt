package com.weit2nd.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.weit2nd.presentation.R
import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO
import com.weit2nd.presentation.ui.common.SearchTopBar
import com.weit2nd.presentation.ui.map.MapScreen
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    navToFoodSpotReport: () -> Unit,
    navToMyPage: () -> Unit,
    navToSearch: (PlaceSearchDTO) -> Unit,
    navToBack: () -> Unit,
    navToFoodSpotDetail: (Long) -> Unit,
) {
    val state by vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            HomeSideEffect.NavToFoodSpotReport -> {
                navToFoodSpotReport()
            }
            is HomeSideEffect.NavToSearch -> {
                navToSearch(sideEffect.placeSearchDTO)
            }
            HomeSideEffect.NavToBack -> {
                navToBack()
            }
            is HomeSideEffect.NavToFoodSpotDetail -> {
                navToFoodSpotDetail(sideEffect.foodSpotId)
            }
            HomeSideEffect.NavToMyPage -> {
                navToMyPage()
            }
        }
    }
    Scaffold(
        topBar = {
            SearchTopBar(
                modifier = Modifier.background(Color.White),
                searchWords = state.searchWords,
                textFieldEnabled = false,
                readOnly = true,
                onTextFieldClick = vm::onSearchPlaceClick,
                onNavigationButtonClick = vm::onNavigateButtonClick,
            )
        },
    ) {
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(it),
        ) {
            MapScreen(
                modifier = Modifier.fillMaxSize(),
                initialPosition = state.initialLatLng,
                onCameraMoveEnd = vm::onCameraMoved,
                onMarkerClick = vm::onFoodSpotMarkerClick,
            )
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
            ) {
                SearchBar(
                    modifier =
                        Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.surface),
                    searchWords = state.searchWords,
                    profileImage = state.profileImage,
                    onSearchBarClick = vm::onSearchPlaceClick,
                    onProfileClick = vm::onProfileClick,
                )
                FoodSpotReportButton(
                    modifier = Modifier.align(Alignment.BottomStart),
                    onClick = vm::onClickReportBtn,
                )
            }
        }
    }
}

@Composable
fun FoodSpotReportButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        colors =
            ButtonDefaults.buttonColors().copy(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                disabledContainerColor = Gray2,
            ),
        contentPadding = PaddingValues(8.dp),
    ) {
        Row {
            Icon(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = R.drawable.ic_pencil),
                contentDescription = "report foodSpot",
                tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.home_post_food_spot),
                style =
                    MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    searchWords: String,
    profileImage: String,
    onSearchBarClick: () -> Unit,
    onProfileClick: () -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        Row(
            modifier =
                Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                    end = 8.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val words =
                searchWords.ifBlank {
                    stringResource(id = R.string.home_search_bar_placeholder)
                }
            val textColor =
                if (searchWords.isNotBlank()) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    Gray1
                }
            Text(
                modifier =
                    Modifier.clickable {
                        onSearchBarClick()
                    },
                text = words,
                maxLines = 1,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
            )
            Spacer(modifier = Modifier.width(8.dp))
            AsyncImage(
                modifier =
                    Modifier
                        .size(24.dp)
                        .clip(RoundedCornerShape(100))
                        .clickable {
                            onProfileClick()
                        },
                model = profileImage,
                contentDescription = "navigate to profile",
                contentScale = ContentScale.Crop,
                fallback = painterResource(id = R.drawable.ic_input_delete_filled),
            )
        }
    }
}

@Preview
@Composable
private fun FoodSpotReportButtonPreview() {
    RoadyFoodyTheme {
        FoodSpotReportButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun SearchBarPreview() {
    RoadyFoodyTheme {
        SearchBar(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surface),
            searchWords = "",
            profileImage = "",
            onSearchBarClick = {},
            onProfileClick = {},
        )
    }
}

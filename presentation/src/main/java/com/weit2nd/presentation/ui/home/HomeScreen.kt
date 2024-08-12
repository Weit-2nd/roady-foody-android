package com.weit2nd.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.navigation.dto.PlaceSearchDTO
import com.weit2nd.presentation.ui.common.SearchTopBar
import com.weit2nd.presentation.ui.map.MapScreen
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
            modifier = Modifier.fillMaxSize().padding(it),
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
                Button(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = navToMyPage,
                    shape = RoundedCornerShape(100),
                ) {
                    Text(text = "마페")
                }
                FloatingActionButton(
                    modifier = Modifier.align(Alignment.BottomStart),
                    onClick = vm::onClickReportBtn,
                ) {
                    Icon(Icons.Filled.Add, "report food-spot")
                }
            }
        }
    }
}

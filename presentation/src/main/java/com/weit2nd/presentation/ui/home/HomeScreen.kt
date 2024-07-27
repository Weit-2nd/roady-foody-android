package com.weit2nd.presentation.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.ui.map.MapScreen
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen(
    vm: HomeViewModel = hiltViewModel(),
    navToFoodSpotReport: () -> Unit,
    navToMyPage: () -> Unit,
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            HomeSideEffect.NavToFoodSpotReport -> navToFoodSpotReport()
        }
    }
    Surface {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            MapScreen(modifier = Modifier.fillMaxSize())
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

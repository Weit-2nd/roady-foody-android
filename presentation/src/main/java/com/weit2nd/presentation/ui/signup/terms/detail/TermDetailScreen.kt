package com.weit2nd.presentation.ui.signup.terms.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.HtmlText
import com.weit2nd.presentation.util.showToast
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun TermDetailScreen(
    navToBack: () -> Unit,
    vm: TermDetailViewModel = hiltViewModel(),
) {
    val state by vm.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            TermDetailSideEffect.NavToBack -> {
                navToBack()
            }
            is TermDetailSideEffect.ShowToast -> {
                context.showToast(sideEffect.messageRes)
            }
        }
    }
    Scaffold(
        topBar = { 
            TermDetailTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = state.title,
                onNavigateClick = vm::onNavigateClick,
            )
        },
    ) {
        Box {
            TermContents(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                text = state.contents,
            )
            if (state.isRetryNeeded) {
                Button(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = vm::onRetryClick,
                ) {
                    Text(text = "다시 시도")
                }
            }
        }
    }
}

// TODO 디자인이 만들어지면 공통 composable로 빼자
@Composable
private fun TermDetailTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onNavigateClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            modifier = Modifier.padding(4.dp),
            onClick = onNavigateClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_navigate),
                contentDescription = "navigate to back"
            )
        }
        Text(text = title)
    }
}

@Composable
private fun TermContents(
    modifier: Modifier = Modifier,
    text: String
) {
    HtmlText(
        modifier = modifier,
        text = text,
    )
}

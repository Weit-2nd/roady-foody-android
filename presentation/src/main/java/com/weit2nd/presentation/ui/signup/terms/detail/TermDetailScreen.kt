package com.weit2nd.presentation.ui.signup.terms.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.BackTopBar
import com.weit2nd.presentation.ui.common.BottomButton
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
            BackTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.term_detail_topbar_title),
                onClickBackBtn = vm::onNavigateClick,
            )
        },
        content = { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                        .verticalScroll(scrollState),
            ) {
                TermContents(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),
                    text = state.contents,
                )
                if (state.isRetryNeeded) {
                    BottomButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = vm::onRetryClick,
                        text = stringResource(R.string.retry),
                    )
                }
            }
        },
    )
}

@Composable
private fun TermContents(
    modifier: Modifier = Modifier,
    text: String,
) {
    HtmlText(
        modifier = modifier,
        text = text,
    )
}

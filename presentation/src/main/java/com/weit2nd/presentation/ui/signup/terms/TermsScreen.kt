package com.weit2nd.presentation.ui.signup.terms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.terms.Term
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun TermsScreen(
    vm: TermsViewModel = hiltViewModel(),
    navToSignUp: (termIds: List<Long>) -> Unit,
    navToTermDetail: (Long) -> Unit,
) {
    val state = vm.collectAsState()
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is TermsSideEffect.NavToTermDetail -> {
                navToTermDetail(sideEffect.termId)
            }

            is TermsSideEffect.NavToSignUp -> {
                navToSignUp(sideEffect.termIds)
            }
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween,
    ) {
        Column {
            Text(
                text = "약관을 확인해주세요.",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
            )
            HorizontalDivider(
                color = Color.DarkGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp),
            )
            AgreeAllCheckbox(
                modifier = Modifier.fillMaxWidth(),
                isChecked = state.value.agreeAll,
                onCheckedChange = vm::onCheckedAllAgreeChange,
            )
            HorizontalDivider(
                color = Color.DarkGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp),
            )

            LazyColumn {
                items(state.value.termStatuses) { termStatus ->
                    TermCheckbox(
                        modifier = Modifier.fillMaxWidth(),
                        term = termStatus.term,
                        isChecked = termStatus.isChecked,
                        onCheckedChange = vm::onCheckedBoxChange,
                        onDetailBtnClicked = vm::onDetailBtnClicked,
                    )
                }
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.value.canProceed,
            onClick = vm::onSignUpBtnClicked,
        ) {
            Text(text = "다음")
        }
    }
}

@Composable
fun AgreeAllCheckbox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(it) },
        )
        Text(
            text = "전체동의",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
fun TermCheckbox(
    modifier: Modifier = Modifier,
    term: Term,
    isChecked: Boolean,
    onCheckedChange: (term: Term, isChecked: Boolean) -> Unit,
    onDetailBtnClicked: (Long) -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(term, it) },
            )
            val termPrefix = if (term.isRequired) "[필수]" else "[선택]"
            Text(text = termPrefix)
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = term.title)
        }
        IconButton(onClick = { onDetailBtnClicked(term.id) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "NavigateToTermDetailButton",
            )
        }
    }
}

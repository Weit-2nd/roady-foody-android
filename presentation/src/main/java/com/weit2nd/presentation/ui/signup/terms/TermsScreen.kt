package com.weit2nd.presentation.ui.signup.terms

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.terms.Term
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.BottomButton
import com.weit2nd.presentation.ui.common.TitleTopBar
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.Gray3
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
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

    Scaffold(topBar = {
        TitleTopBar(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
            title = stringResource(R.string.term_screen_topbar_title),
        )
    }, content = { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                DescriptionText(modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.padding(vertical = 16.dp))

                AgreeAllCheckbox(
                    modifier = Modifier.fillMaxWidth(),
                    isChecked = state.value.agreeAll,
                    onCheckedChange = vm::onCheckedAllAgreeChange,
                )

                HorizontalDivider(
                    color = Gray3,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 16.dp),
                )

                LazyColumn {
                    items(state.value.termStatuses) { termStatus ->
                        TermCheckbox(
                            modifier = Modifier.fillMaxWidth(),
                            term = termStatus.term,
                            isChecked = termStatus.isChecked,
                            onTermAgreementChange = vm::onTermAgreementCheckBoxChange,
                            onDetailBtnClicked = vm::onDetailBtnClicked,
                        )
                    }
                }
            }

            BottomButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = vm::onSignUpBtnClicked,
                enabled = state.value.canProceed,
                text = stringResource(R.string.term_screen_bottom_btn),
            )
        }
    })
}

@Composable
private fun DescriptionText(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.term_screen_description),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = stringResource(R.string.term_screen_sub_description),
            style = MaterialTheme.typography.titleSmall,
            color = Gray1,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgreeAllCheckbox(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { onCheckedChange(it) },
                colors = CheckboxDefaults.colors(uncheckedColor = Gray3),
            )
        }
        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
        Text(
            text = stringResource(R.string.term_screen_agree_all),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermCheckbox(
    modifier: Modifier = Modifier,
    term: Term,
    isChecked: Boolean,
    onTermAgreementChange: (term: Term, isChecked: Boolean) -> Unit,
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
            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onTermAgreementChange(term, it) },
                    colors =
                        CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.secondary,
                            uncheckedColor = Gray3,
                        ),
                )
            }
            RequiredTermLabel(
                modifier = Modifier.padding(horizontal = 8.dp),
                isRequired = term.isRequired,
            )
            Text(
                text = term.title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        IconButton(onClick = { onDetailBtnClicked(term.id) }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                tint = Gray1,
                contentDescription = "NavigateToTermDetailButton",
            )
        }
    }
}

@Composable
private fun RequiredTermLabel(
    modifier: Modifier = Modifier,
    isRequired: Boolean,
) {
    Text(
        modifier =
            modifier
                .border(BorderStroke(1.dp, Gray2), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
        text = if (isRequired) stringResource(R.string.term_required) else stringResource(R.string.term_optional),
        style = MaterialTheme.typography.bodyMedium,
        color = Gray1,
    )
}

@Composable
@Preview(showBackground = true)
private fun DescriptionPreview() {
    RoadyFoodyTheme {
        DescriptionText(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
@Preview(showBackground = true)
private fun AgreeAllPreview() {
    RoadyFoodyTheme {
        AgreeAllCheckbox(modifier = Modifier.fillMaxWidth(), isChecked = true) {}
    }
}

@Composable
@Preview(showBackground = true)
private fun RequiredTermLabelPreview() {
    RoadyFoodyTheme {
        RequiredTermLabel(modifier = Modifier, isRequired = false)
    }
}

@Composable
@Preview(showBackground = true)
private fun TermCheckBoxPreview() {
    RoadyFoodyTheme {
        TermCheckbox(
            modifier = Modifier.fillMaxWidth(),
            term = Term(0, "약관 제목입니다", true),
            isChecked = true,
            onTermAgreementChange = { _, _ -> },
        ) { _ -> }
    }
}

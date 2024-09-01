package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.DarkGray
import com.weit2nd.presentation.ui.theme.Gray2
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    textFieldEnabled: Boolean = true,
    readOnly: Boolean = false,
    searchWords: String,
    onClear: (() -> Unit)? = null,
    onTextFieldClick: (() -> Unit)? = null,
    onSearchButtonClick: (() -> Unit)? = null,
    onSearchWordsChanged: ((String) -> Unit)? = null,
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
            modifier =
                Modifier
                    .weight(1f)
                    .clickable {
                        onTextFieldClick?.invoke()
                    },
            enabled = textFieldEnabled,
            searchWords = searchWords,
            onSearchWordsChanged = {
                onSearchWordsChanged?.invoke(it)
            },
            onClear = {
                onClear?.invoke()
            },
            onSearchButtonClick = {
                onSearchButtonClick?.invoke()
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
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
        enabled = enabled,
        readOnly = readOnly,
        onValueChange = onSearchWordsChanged,
        interactionSource = interactionSource,
        textStyle =
            MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface,
            ),
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
                    text = stringResource(id = R.string.search_top_bar_hint),
                    color = Gray2,
                    style = MaterialTheme.typography.bodyLarge,
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
            colors =
                TextFieldDefaults.colors(
                    disabledContainerColor = Color.White,
                    errorContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.White,
                    disabledIndicatorColor = Color.White,
                    focusedIndicatorColor = DarkGray,
                ),
        )
    }
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    RoadyFoodyTheme {
        SearchTextField(
            modifier = Modifier.fillMaxWidth(),
            searchWords = "안녕하세요",
            onSearchWordsChanged = {},
            onClear = {},
            onSearchButtonClick = {},
        )
    }
}

@Preview
@Composable
private fun SearchTopBarPerview() {
    RoadyFoodyTheme {
        SearchTopBar(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(Color.White),
            searchWords = "감사해요",
            onClear = {},
            onSearchButtonClick = {},
            onSearchWordsChanged = {},
            onNavigationButtonClick = {},
        )
    }
}

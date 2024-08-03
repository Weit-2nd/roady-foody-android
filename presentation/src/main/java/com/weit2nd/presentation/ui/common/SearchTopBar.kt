package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.weit2nd.presentation.R

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    textFieldEnabled: Boolean = true,
    searchWords: String,
    onClear: () -> Unit,
    onSearchButtonClick: () -> Unit,
    onSearchWordsChanged: (String) -> Unit,
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
            modifier = Modifier.weight(1f),
            enabled = textFieldEnabled,
            searchWords = searchWords,
            onSearchWordsChanged = onSearchWordsChanged,
            onClear = onClear,
            onSearchButtonClick = onSearchButtonClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
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
        onValueChange = onSearchWordsChanged,
        interactionSource = interactionSource,
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
                    text = "장소를 입력해!",
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_glass),
                    contentDescription = "장소 검색 바",
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
            // TODO Material Theme를 적용하고 나면 제거
            colors =
            TextFieldDefaults.colors(
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                unfocusedIndicatorColor = Color.White,
                disabledIndicatorColor = Color.White,
                focusedIndicatorColor = Color(0xFF555555),
            ),
        )
    }
}

@Preview
@Composable
private fun SearchTextFieldPreview() {
    SearchTextField(
        modifier = Modifier.fillMaxWidth(),
        searchWords = "안녕하세요",
        onSearchWordsChanged = {},
        onClear = {},
        onSearchButtonClick = {},
    )
}

@Preview
@Composable
private fun SearchTopBarPerview() {
    SearchTopBar(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        searchWords = "감사해요",
        onClear = {},
        onSearchButtonClick = {},
        onSearchWordsChanged = {},
        onNavigationButtonClick = {},
    )
}

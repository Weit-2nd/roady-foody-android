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

@Composable
fun TermsScreen(
    vm: TermsViewModel = hiltViewModel()
) {
    val state = vm.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "약관을 확인해주세요.",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
            )
            HorizontalDivider(
                color = Color.DarkGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            AllAgreeCheckbox(
                modifier = Modifier.fillMaxWidth()
            )
            HorizontalDivider(
                color = Color.DarkGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn {
                items(state.value.terms) { term ->
                    TermCheckbox(modifier = Modifier.fillMaxWidth(), term = term)
                }
            }
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { },
        ) {
            Text(text = "다음")
        }
    }
}

@Composable
fun AllAgreeCheckbox(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = false, onCheckedChange = {})
        Text(
            text = "전체동의",
            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold)
        )
    }
}

@Composable
fun TermCheckbox(
    modifier: Modifier = Modifier,
    term: Term,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = false, onCheckedChange = {})
            Text(text = term.isRequired.toString())
            Spacer(modifier = Modifier.padding(4.dp))
            Text(text = term.title)
        }
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "NavigateToTermDetailButton"
            )
        }
    }
}

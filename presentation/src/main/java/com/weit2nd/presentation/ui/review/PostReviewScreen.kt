package com.weit2nd.presentation.ui.review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun PostReviewScreen(
    vm: PostReviewViewModel = hiltViewModel(),
    navToBack: () -> Unit,
) {
    val state by vm.collectAsState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            PostReviewSideEffect.NavToBack -> {
                navToBack()
            }
            is PostReviewSideEffect.ShowToast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    Scaffold {
        PostReviewContent(
            modifier = Modifier.padding(it).padding(16.dp),
            state = state,
            onPostReviewButtonClick = vm::onPostReviewButtonClick,
            onPickImageButtonClick = vm::onPickImageButtonClick,
            onRatingChanged = vm::onRatingChanged,
            onReviewChanged = vm::onReviewChanged,
        )
    }
}

@Composable
private fun PostReviewContent(
    modifier: Modifier = Modifier,
    state: PostReviewState,
    onPostReviewButtonClick: () -> Unit,
    onPickImageButtonClick: () -> Unit,
    onRatingChanged: (Float) -> Unit,
    onReviewChanged: (String) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = state.foodSpotName,
            color = Color.Black,
        )
        Spacer(modifier = Modifier.height(8.dp))
        RatingBar(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            value = state.rating,
            style = RatingBarStyle.Fill(),
            stepSize = StepSize.HALF,
            size = 24.dp,
            spaceBetween = 0.dp,
            isIndicator = false,
            onValueChange = onRatingChanged,
            onRatingChanged = {},
        )
        Spacer(modifier = Modifier.height(8.dp))
        ReviewTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(120.dp),
            content = state.content,
            contentMaxLength = state.maxLength,
            onValueChange = onReviewChanged,
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 사진 추가
        // 리뷰 작성 본문
        Button(
            modifier =
                Modifier
                    .fillMaxWidth(),
            onClick = onPostReviewButtonClick,
        ) {
            Text(text = "리뷰 작성")
        }
    }
}

@Composable
private fun ReviewTextField(
    modifier: Modifier = Modifier,
    content: String,
    contentMaxLength: Int,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
        value = content,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        placeholder = {
            Text(text = "리뷰를 남겨주세요! (최대 ${contentMaxLength}자)")
        },
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
    )
}

@Preview
@Composable
private fun PostReviewScreenPreview() {
    PostReviewContent(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
        state =
            PostReviewState(
                foodSpotName = "네임네임",
            ),
        onPostReviewButtonClick = { },
        onPickImageButtonClick = { },
        onRatingChanged = { },
        onReviewChanged = { },
    )
}

package com.weit2nd.presentation.ui.review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.weit2nd.presentation.ui.common.CancelableImage
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
            modifier = Modifier.padding(it),
            state = state,
            onPostReviewButtonClick = vm::onPostReviewButtonClick,
            onPickImageButtonClick = vm::onPickImageButtonClick,
            onDeleteImageButtonClick = vm::onDeleteImageButtonClick,
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
    onDeleteImageButtonClick: (String) -> Unit,
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
        ImagePicker(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            selectedImages = state.selectedImages,
            onDeleteImage = onDeleteImageButtonClick,
            onPickImage = onPickImageButtonClick,
        )
        Spacer(modifier = Modifier.height(8.dp))
        ReviewTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 16.dp),
            content = state.content,
            contentMaxLength = state.maxLength,
            onValueChange = onReviewChanged,
        )
        Spacer(modifier = Modifier.height(8.dp))
        // 사진 추가
        // 리뷰 작성 본문
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            onClick = onPostReviewButtonClick,
        ) {
            Text(text = "리뷰 작성")
        }
    }
}

@Composable
private fun ImagePicker(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues,
    selectedImages: List<String>,
    onDeleteImage: (String) -> Unit,
    onPickImage: () -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            IconButton(
                modifier =
                    Modifier
                        .size(100.dp)
                        .background(Color.LightGray),
                onClick = onPickImage,
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "select_image",
                )
            }
        }
        items(selectedImages) { image ->
            CancelableImage(
                modifier = Modifier.size(100.dp),
                imgUri = image,
                onDeleteImage = onDeleteImage,
            )
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
                .background(Color.White),
        state =
            PostReviewState(
                foodSpotName = "네임네임",
            ),
        onPostReviewButtonClick = { },
        onPickImageButtonClick = { },
        onDeleteImageButtonClick = { },
        onRatingChanged = { },
        onReviewChanged = { },
    )
}

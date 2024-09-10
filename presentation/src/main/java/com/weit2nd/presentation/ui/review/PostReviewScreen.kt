package com.weit2nd.presentation.ui.review

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults.ContainerBox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.StepSize
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.AddImageButton
import com.weit2nd.presentation.ui.common.BackTopBar
import com.weit2nd.presentation.ui.common.BottomButton
import com.weit2nd.presentation.ui.common.CancelableImage
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray3
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
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
    Scaffold(
        topBar = {
            BackTopBar(
                title = state.foodSpotName,
                onClickBackBtn = vm::onNavigationButtonClick,
            )
        },
    ) {
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
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.post_review_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        RatingBar(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            value = state.rating,
            painterFilled = painterResource(id = R.drawable.ic_star),
            painterEmpty = painterResource(id = R.drawable.ic_star_border),
            stepSize = StepSize.ONE,
            size = 36.dp,
            spaceBetween = 2.dp,
            isIndicator = false,
            onValueChange = onRatingChanged,
            onRatingChanged = {},
        )
        Spacer(modifier = Modifier.height(40.dp))
        ImagePicker(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            selectedImages = state.selectedImages,
            onDeleteImage = onDeleteImageButtonClick,
            onPickImage = onPickImageButtonClick,
        )
        Spacer(modifier = Modifier.height(16.dp))
        ReviewTextField(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(horizontal = 16.dp),
            content = state.content,
            maxLength = state.maxLength,
            onValueChange = onReviewChanged,
        )
        Spacer(modifier = Modifier.weight(1f))
        // 사진 추가
        // 리뷰 작성 본문
        BottomButton(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 24.dp,
                    ),
            onClick = onPostReviewButtonClick,
            text = stringResource(id = R.string.post_review_confirm),
        )
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
            AddImageButton(
                modifier =
                    Modifier
                        .size(80.dp)
                        .clickable {
                            onPickImage()
                        },
            )
        }
        items(selectedImages) { image ->
            CancelableImage(
                modifier = Modifier.size(80.dp),
                imgUri = image,
                onDeleteImage = onDeleteImage,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewTextField(
    modifier: Modifier = Modifier,
    content: String,
    maxLength: Int,
    onValueChange: (String) -> Unit,
) {
    val interactionSource =
        remember {
            MutableInteractionSource()
        }
    Box(
        modifier = modifier,
    ) {
        BasicTextField(
            modifier = Modifier.fillMaxSize(),
            value = content,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = content,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.post_review_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Gray1,
                    )
                },
                container = {
                    ContainerBox(
                        enabled = true,
                        isError = maxLength <= content.length,
                        interactionSource = interactionSource,
                        colors =
                            TextFieldDefaults.colors(
                                disabledContainerColor = MaterialTheme.colorScheme.surface,
                                errorContainerColor = MaterialTheme.colorScheme.surface,
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedIndicatorColor = Gray3,
                                disabledIndicatorColor = Gray3,
                                focusedIndicatorColor = Gray3,
                                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            ),
                        shape = RoundedCornerShape(12.dp),
                        focusedBorderThickness = 1.dp,
                        unfocusedBorderThickness = 1.dp,
                    )
                },
                contentPadding =
                    PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 44.dp,
                    ),
            )
        }
        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            text =
            stringResource(
                id = R.string.post_review_content_length,
                content.length,
                maxLength,
            ),
            style = MaterialTheme.typography.labelSmall,
            color = Gray1,
        )
    }
}

@Preview
@Composable
private fun PostReviewScreenPreview() {
    RoadyFoodyTheme {
        PostReviewContent(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White),
            state =
                PostReviewState(
                    foodSpotName = "네임네임",
                    content = "adsf",
                    maxLength = 300,
                ),
            onPostReviewButtonClick = { },
            onPickImageButtonClick = { },
            onDeleteImageButtonClick = { },
            onRatingChanged = { },
            onReviewChanged = { },
        )
    }
}

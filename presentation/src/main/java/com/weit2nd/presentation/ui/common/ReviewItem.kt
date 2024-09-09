package com.weit2nd.presentation.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.weit2nd.presentation.R
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray4
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import com.weit2nd.presentation.util.LocalDateTimeFormatter.reviewDateFormatter
import java.time.LocalDateTime
import kotlin.math.ceil

@Composable
fun ReviewItem(
    modifier: Modifier = Modifier,
    review: Review,
    isContentExpended: Boolean = false,
    onImageClick: (images: List<String>, position: Int) -> Unit,
    onOptionClick: () -> Unit = {},
    onContentClick: () -> Unit = {},
    onReadMoreClick: () -> Unit = {},
) {
    var isContentOverflow by remember {
        mutableStateOf(false)
    }
    val contentMaxLine =
        if (isContentExpended) {
            Int.MAX_VALUE
        } else {
            2
        }
    val isReadMoreVisible by remember {
        derivedStateOf {
            isContentExpended.not() && isContentOverflow
        }
    }

    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ReviewProfile(
                profileImage = review.profileImage,
                name = review.nickname,
                date = review.date,
                rating = ceil(review.rating).toInt(),
            )
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = onOptionClick,
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = "리뷰 옵션",
                    tint = Gray1,
                )
            }
        }

        if (review.reviewImages.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            ReviewImages(
                modifier = Modifier.fillMaxWidth(),
                images = review.reviewImages,
                onImageClick = {
                    onImageClick(review.reviewImages, it)
                },
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier =
            Modifier
                .padding(
                    horizontal = 16.dp,
                )
                .fillMaxWidth()
                .clickable {
                    onContentClick()
                },
            text = review.contents,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = contentMaxLine,
            overflow = TextOverflow.Ellipsis,
            onTextLayout = {
                isContentOverflow = it.hasVisualOverflow
            },
        )
        if (isReadMoreVisible) {
            Text(
                modifier =
                Modifier
                    .padding(
                        horizontal = 16.dp,
                    )
                    .clickable {
                        onReadMoreClick()
                    },
                text = stringResource(id = R.string.review_read_more),
                style = MaterialTheme.typography.labelMedium,
                color = Gray1,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ReviewProfile(
    modifier: Modifier = Modifier,
    profileImage: String?,
    name: String,
    date: LocalDateTime,
    rating: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileImage(
            modifier = Modifier.size(32.dp),
            imgUri = profileImage?.toUri(),
        )
        Spacer(
            modifier = Modifier.width(8.dp),
        )
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.size(8.dp),
                    painter = painterResource(id = R.drawable.ic_star),
                    contentDescription = "rating",
                    tint = MaterialTheme.colorScheme.tertiary,
                )
                Spacer(
                    modifier = Modifier.width(4.dp),
                )
                Text(
                    text = rating.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(
                    modifier = Modifier.width(4.dp),
                )
                Text(
                    text = reviewDateFormatter.format(date),
                    style = MaterialTheme.typography.labelMedium,
                    color = Gray1,
                )
            }
        }
    }
}

@Composable
private fun ReviewImages(
    modifier: Modifier = Modifier,
    images: List<String>,
    contentPadding: PaddingValues =
        PaddingValues(
            horizontal = 16.dp,
        ),
    onImageClick: (position: Int) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        itemsIndexed(images) { index, image ->
            AsyncImage(
                modifier =
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .size(80.dp)
                    .clickable {
                        onImageClick(index)
                    },
                model = image,
                contentScale = ContentScale.Crop,
                contentDescription = "$image reviewImage",
                fallback = painterResource(id = R.drawable.ic_input_delete_filled),
                placeholder = ColorPainter(Gray4),
            )
            if (images.lastIndex != index) {
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun ReviewItemPreview() {
    RoadyFoodyTheme {
        ReviewItem(
            modifier = Modifier.background(MaterialTheme.colorScheme.surface),
            review =
                Review(
                    userId = 1,
                    profileImage = null,
                    nickname = "모르는개산책",
                    date = LocalDateTime.now(),
                    rating = 4f,
                    reviewImages = listOf("", "", ""),
                    contents =
                        """
                        가나다가나다가나다가나다가나다가나다가나다
                        가나다가나다가나다가나다가나다가나다가나다
                        가나다가나다가나다가나다가나다가나다가나다
                        가나다가나다가나다가나다가나다가나다가나다
                        """.trimIndent(),
                ),
            onImageClick = { _, _ -> },
        )
    }
}

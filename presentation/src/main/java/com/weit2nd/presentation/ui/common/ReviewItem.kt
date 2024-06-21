package com.weit2nd.presentation.ui.common

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarStyle
import com.gowtham.ratingbar.StepSize
import com.weit2nd.domain.model.Review
import com.weit2nd.presentation.R
import com.weit2nd.presentation.util.LocalDateTimeFormatter.reviewDateFormatter
import com.weit2nd.presentation.util.toPainter
import java.time.LocalDateTime

@Composable
fun ReviewItem(
    modifier: Modifier = Modifier,
    review: Review,
    onImageClick: (images: List<String>, position: Int) -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            UserInfo(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                profileImage = review.profileImage,
                nickname = review.nickname,
            )
            ReviewDateAndRating(
                date = review.date,
                rating = review.rating,
            )
        }

        ReviewImages(
            context = context,
            images = review.reviewImages,
            onImageClick = onImageClick,
        )

        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = review.reviewDetail,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black
            ),
        )
    }
}

@Composable
private fun UserInfo(
    modifier: Modifier = Modifier,
    profileImage: String,
    nickname: String
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        ProfileImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp),
            imgUri = profileImage.toUri(),
            defaultImage = painterResource(R.drawable.ic_launcher_background),
        )
        Text(
            text = nickname,
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Black
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun ReviewDateAndRating(
    modifier: Modifier = Modifier,
    date: LocalDateTime,
    rating: Float
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = reviewDateFormatter.format(date),
            style = TextStyle(
                fontSize = 12.sp,
                color = Color.Black
            ),
        )
        RatingBar(
            value = rating,
            style = RatingBarStyle.Fill(),
            stepSize = StepSize.HALF,
            size = 12.dp,
            spaceBetween = 0.dp,
            isIndicator = true,
            onValueChange = {},
            onRatingChanged = {},
        )
    }
}

@Composable
private fun ReviewImages(
    modifier: Modifier = Modifier,
    context: Context,
    images: List<String>,
    onImageClick: (images: List<String>, position: Int) -> Unit,
) {
    LazyRow(
        modifier = modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(images) { index, image ->
            Image(
                modifier = modifier.clickable { onImageClick(images, index) },
                painter = image.toUri().toPainter(context.contentResolver),
                contentDescription = "$image reviewImage"
            )
        }
    }
}

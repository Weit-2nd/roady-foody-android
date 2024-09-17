package com.weit2nd.presentation.ui.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.weit2nd.domain.model.spot.FoodCategory
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray4
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import com.weit2nd.presentation.util.LocalDateTimeFormatter
import java.time.LocalDateTime

@Composable
fun FoodSpotItem(
    modifier: Modifier = Modifier,
    foodSpot: FoodSpotHistoryContent,
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = foodSpot.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
//                if (isFoodTruck) { // todo api 수정되면 푸드트럭 여부 화면에 반영
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Icon(
//                        painter = painterResource(id = R.drawable.ic_truck),
//                        contentDescription = "food truck",
//                        tint = MaterialTheme.colorScheme.secondary,
//                    )
//                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            FoodCategory(categories = foodSpot.categories)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = LocalDateTimeFormatter.reviewDateFormatter.format(foodSpot.createdDateTime),
                style = MaterialTheme.typography.labelMedium,
                color = Gray1,
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        AsyncImage(
            modifier =
                Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
            model = foodSpot.reportPhotos.firstOrNull()?.image,
            contentScale = ContentScale.Crop,
            contentDescription = "foodspot image",
            placeholder = ColorPainter(Gray4),
            fallback = painterResource(id = R.drawable.ic_input_delete_filled),
        )
    }
}

@Composable
private fun FoodCategory(
    modifier: Modifier = Modifier,
    categories: List<FoodCategory>,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        for ((index, category) in categories.withIndex()) {
            if (index > 2) {
                Icon(
                    modifier = Modifier.size(16.dp),
                    painter = painterResource(id = R.drawable.ic_more),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "more_category_icon",
                )
                break
            }
            SuggestionChip(
                modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 24.dp),
                onClick = {},
                shape = RoundedCornerShape(16.dp),
                colors =
                    SuggestionChipDefaults.suggestionChipColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                    ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                label = {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                },
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun FoodCategoryPreview() {
    RoadyFoodyTheme {
        FoodCategory(
            categories =
                listOf(
                    FoodCategory(
                        0,
                        "포장마차",
                    ),
                    FoodCategory(
                        0,
                        "붕어빵",
                    ),
                    FoodCategory(
                        0,
                        "면",
                    ),
                    FoodCategory(
                        0,
                        "포장마차",
                    ),
                ),
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun FoodSpotItemPreview() {
    RoadyFoodyTheme {
        FoodSpotItem(
            modifier = Modifier.fillMaxWidth(),
            foodSpot =
                FoodSpotHistoryContent(
                    id = 0,
                    userId = 10,
                    foodSpotsId = 12,
                    name = "ExampleName",
                    longitude = 188.8,
                    latitude = 45.55,
                    createdDateTime = LocalDateTime.now(),
                    reportPhotos = listOf(),
                    categories =
                        listOf(
                            FoodCategory(
                                0,
                                "포장마차",
                            ),
                            FoodCategory(
                                0,
                                "포장마차",
                            ),
                            FoodCategory(
                                0,
                                "포장마차",
                            ),
                            FoodCategory(
                                0,
                                "포장마차",
                            ),
                        ),
                ),
        )
    }
}

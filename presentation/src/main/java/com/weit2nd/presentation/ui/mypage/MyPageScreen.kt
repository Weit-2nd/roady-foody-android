package com.weit2nd.presentation.ui.mypage

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.domain.model.spot.FoodSpotPhoto
import com.weit2nd.presentation.R
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.navigation.dto.ReviewHistoryDTO
import com.weit2nd.presentation.ui.common.BackTopBar
import com.weit2nd.presentation.ui.common.CommonAlertDialog
import com.weit2nd.presentation.ui.common.EditableProfileImage
import com.weit2nd.presentation.ui.common.LoadingDialogScreen
import com.weit2nd.presentation.ui.common.ReviewItem
import com.weit2nd.presentation.ui.theme.DarkGray
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.Gray4
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun MyPageScreen(
    navToLogin: () -> Unit,
    navToReviewHistory: (ReviewHistoryDTO) -> Unit,
    navToBack: () -> Unit,
    navToFoodSpotHistory: (Long) -> Unit,
    vm: MyPageViewModel = hiltViewModel(),
) {
    val state by vm.collectAsState()
    val context = LocalContext.current
    vm.collectSideEffect { sideEffect ->
        when (sideEffect) {
            MyPageSideEffect.NavToLogin -> {
                navToLogin()
            }

            is MyPageSideEffect.ShowToastMessage -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            is MyPageSideEffect.NavToReviewHistory -> {
                navToReviewHistory(sideEffect.reviewHistoryDTO)
            }

            is MyPageSideEffect.NavToFoodSpotHistory -> {
                navToFoodSpotHistory(sideEffect.userId)
            }

            MyPageSideEffect.NavToBack -> {
                navToBack()
            }
        }
    }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    if (state.isLoading) {
        LoadingDialogScreen()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.my_page_toolbar_title),
                onClickBackBtn = vm::onBackButtonClick,
            )
        },
    ) {
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(it),
        ) {
            item {
                MyPageContent(
                    profileImage = state.profileImage,
                    nickname = state.nickname,
                    coin = state.coin,
                    foodSpotHistory = state.foodSpotHistory,
                    review = state.review,
                    onLogoutButtonClick = vm::onLogoutButtonClick,
                    onWithdrawButtonClick = vm::onWithdrawButtonClick,
                    onLogoutConfirm = vm::onLogoutConfirm,
                    onWithdrawConfirm = vm::onWithdrawConfirm,
                    onLogoutDialogClose = vm::onLogoutDialogClose,
                    onWithdrawDialogClose = vm::onWithdrawDialogClose,
                    isLogoutDialogShown = state.isLogoutDialogShown,
                    isWithdrawDialogShown = state.isWithdrawDialogShown,
                    onFoodSpotHistoryClick = vm::onFoodSpotHistoryClick,
                    onReviewHistoryClick = vm::onReviewHistoryClick,
                    foodSpotCount = state.foodSpotCount,
                    reviewCount = state.reviewCount,
                )
            }
        }
    }
}

@Composable
private fun MyPageContent(
    modifier: Modifier = Modifier,
    profileImage: String?,
    nickname: String,
    coin: Int,
    foodSpotHistory: FoodSpotHistoryContent?,
    review: Review?,
    onLogoutButtonClick: () -> Unit,
    onWithdrawButtonClick: () -> Unit,
    onLogoutConfirm: () -> Unit,
    onWithdrawConfirm: () -> Unit,
    onLogoutDialogClose: () -> Unit,
    onWithdrawDialogClose: () -> Unit,
    isLogoutDialogShown: Boolean,
    isWithdrawDialogShown: Boolean,
    onFoodSpotHistoryClick: () -> Unit,
    onReviewHistoryClick: () -> Unit,
    foodSpotCount: Int,
    reviewCount: Int,
) {
    Box(
        modifier = modifier,
    ) {
        if (isLogoutDialogShown) {
            CommonAlertDialog(
                modifier = Modifier.align(Alignment.Center),
                title = stringResource(R.string.my_page_logout),
                contents = stringResource(R.string.logout_dialog_content),
                onPositiveButtonClick = onLogoutConfirm,
                onNegativeButtonClick = onLogoutDialogClose,
            )
        }
        if (isWithdrawDialogShown) {
            CommonAlertDialog(
                modifier = Modifier.align(Alignment.Center),
                title = stringResource(R.string.account_deletion_dialog_title),
                contents = stringResource(R.string.account_deletion_dialog_content),
                onPositiveButtonClick = onWithdrawConfirm,
                onNegativeButtonClick = onWithdrawDialogClose,
            )
        }

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            EditableProfileImage(
                modifier = Modifier.size(160.dp),
                imgUri = profileImage?.toUri(),
            )
            CoinText(coin = coin)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = nickname,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Gray4)
            Spacer(modifier = Modifier.height(16.dp))

            ReportedFoodSpot(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                foodSpotHistory = foodSpotHistory,
                onFoodSpotHistoryClick = onFoodSpotHistoryClick,
                foodSpotCount = foodSpotCount,
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Gray4)
            Spacer(modifier = Modifier.height(16.dp))

            WrittenReview(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                review = review,
                onReviewHistoryClick = onReviewHistoryClick,
                reviewCount = reviewCount,
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Gray4)

            LogoutButton(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { onLogoutButtonClick() },
            )
        }

        Text(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .clickable { onWithdrawButtonClick() }
                    .padding(16.dp),
            text = stringResource(R.string.my_page_account_deletion),
            style = MaterialTheme.typography.labelLarge,
            color = Gray1,
        )
    }
}

@Composable
private fun CoinText(
    modifier: Modifier = Modifier,
    coin: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(32.dp),
            painter = painterResource(id = R.drawable.ic_coin),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "coinIcon",
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = NumberFormat.getInstance(Locale.getDefault()).format(coin),
            style = MaterialTheme.typography.headlineSmall,
            color = DarkGray,
        )
    }
}

@Composable
private fun ReportedFoodSpot(
    modifier: Modifier = Modifier,
    foodSpotHistory: FoodSpotHistoryContent?,
    onFoodSpotHistoryClick: () -> Unit,
    foodSpotCount: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.my_page_reported_food_spot_title, foodSpotCount),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )

        if (foodSpotHistory != null) {
            IconButton(onClick = { onFoodSpotHistoryClick() }) {
                Icon(
                    modifier =
                        Modifier
                            .size(32.dp)
                            .offset(y = 2.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    tint = DarkGray,
                    contentDescription = "",
                )
            }
        }
    }
    if (foodSpotHistory != null) {
        FoodSpotItem(foodSpot = foodSpotHistory)
    } else {
        Text(
            modifier = Modifier.padding(vertical = 32.dp),
            text = stringResource(R.string.my_page_no_reported_food_spot),
            style = MaterialTheme.typography.titleLarge,
            color = Gray1,
        )
    }
}

@Composable
private fun WrittenReview(
    modifier: Modifier = Modifier,
    review: Review?,
    onReviewHistoryClick: () -> Unit,
    reviewCount: Int,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.my_page_written_review_title, reviewCount),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (review != null) {
            IconButton(onClick = { onReviewHistoryClick() }) {
                Icon(
                    modifier =
                        Modifier
                            .size(32.dp)
                            .offset(y = 2.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    tint = DarkGray,
                    contentDescription = "",
                )
            }
        }
    }
    if (review != null) {
        ReviewItem(review = review, onImageClick = { _, _ -> })
    } else {
        Text(
            modifier = Modifier.padding(vertical = 32.dp),
            text = stringResource(R.string.my_page_no_written_review),
            style = MaterialTheme.typography.titleLarge,
            color = Gray1,
        )
    }
}

@Composable
private fun LogoutButton(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.padding(4.dp).size(24.dp),
            painter = painterResource(id = R.drawable.ic_logout),
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = "logoutIcon",
        )
        Text(
            text = stringResource(R.string.my_page_logout),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageContentPreview() {
    RoadyFoodyTheme {
        MyPageContent(
            modifier = Modifier.fillMaxSize(),
            profileImage = null,
            nickname = "nicknameExample",
            coin = 10000,
            foodSpotHistory =
                FoodSpotHistoryContent(
                    id = 8229,
                    userId = 3828,
                    foodSpotsId = 3783,
                    name = "음식점 이름이에요",
                    longitude = 8.9,
                    latitude = 10.11,
                    createdDateTime = LocalDateTime.now(),
                    reportPhotos = listOf(FoodSpotPhoto(0, "")),
                    categories =
                        listOf(
                            com.weit2nd.domain.model.spot
                                .FoodCategory(0, "포장마차"),
                        ),
                ),
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
            onLogoutButtonClick = {},
            onWithdrawButtonClick = {},
            onLogoutConfirm = {},
            onWithdrawConfirm = {},
            onLogoutDialogClose = {},
            onWithdrawDialogClose = {},
            isLogoutDialogShown = false,
            isWithdrawDialogShown = false,
            onReviewHistoryClick = {},
            onFoodSpotHistoryClick = {},
            reviewCount = 2,
            foodSpotCount = 3,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyPageNoContentPreview() {
    RoadyFoodyTheme {
        MyPageContent(
            modifier = Modifier.fillMaxSize(),
            profileImage = null,
            nickname = "가나다라마가가가가가가가가가가가",
            coin = 10000000,
            foodSpotHistory = null,
            review = null,
            onLogoutButtonClick = {},
            onWithdrawButtonClick = {},
            onLogoutConfirm = {},
            onWithdrawConfirm = {},
            onLogoutDialogClose = {},
            onWithdrawDialogClose = {},
            isLogoutDialogShown = false,
            isWithdrawDialogShown = false,
            onReviewHistoryClick = {},
            onFoodSpotHistoryClick = {},
            reviewCount = 0,
            foodSpotCount = 0,
        )
    }
}

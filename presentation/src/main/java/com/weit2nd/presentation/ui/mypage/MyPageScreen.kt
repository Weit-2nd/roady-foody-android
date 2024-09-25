package com.weit2nd.presentation.ui.mypage

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.weit2nd.domain.model.Badge
import com.weit2nd.domain.model.spot.FoodSpotHistoryContent
import com.weit2nd.domain.model.spot.FoodSpotPhoto
import com.weit2nd.presentation.R
import com.weit2nd.presentation.model.foodspot.Review
import com.weit2nd.presentation.navigation.dto.UserInfoDTO
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
    navToReviewHistory: (UserInfoDTO) -> Unit,
    navToBack: () -> Unit,
    navToFoodSpotHistory: (Long) -> Unit,
    navToFoodSpotDetail: (Long) -> Unit,
    navToUserInfoEdit: (UserInfoDTO) -> Unit,
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
                navToReviewHistory(sideEffect.userInfoDTO)
            }

            is MyPageSideEffect.NavToFoodSpotHistory -> {
                navToFoodSpotHistory(sideEffect.userId)
            }

            MyPageSideEffect.NavToBack -> {
                navToBack()
            }

            is MyPageSideEffect.NavToFoodSpotDetail -> {
                navToFoodSpotDetail(sideEffect.foodSpotId)
            }

            is MyPageSideEffect.NavToUserInfoEdit -> {
                navToUserInfoEdit(sideEffect.userInfoDTO)
            }
        }
    }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    Box {
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
                        badge = state.badge,
                        ranking = state.myRanking,
                        foodSpotHistory = state.foodSpotHistory,
                        review = state.review,
                        restReportCount = state.restDailyReportCreationCount,
                        onLogoutButtonClick = vm::onLogoutButtonClick,
                        onWithdrawButtonClick = vm::onWithdrawButtonClick,
                        onLogoutConfirm = vm::onLogoutConfirm,
                        onWithdrawConfirm = vm::onWithdrawConfirm,
                        onLogoutDialogClose = vm::onLogoutDialogClose,
                        onWithdrawDialogClose = vm::onWithdrawDialogClose,
                        isLogoutDialogShown = state.isLogoutDialogShown,
                        isWithdrawDialogShown = state.isWithdrawDialogShown,
                        onFoodSpotHistoryClick = vm::onFoodSpotHistoryClick,
                        onFoodSpotContentClick = vm::onFoodSpotContentClick,
                        onReviewHistoryClick = vm::onReviewHistoryClick,
                        onRankingButtonClick = vm::onRankingButtonClick,
                        onUserInfoEditButtonClick = vm::onUserInfoEditButtonClick,
                        foodSpotCount = state.foodSpotCount,
                        reviewCount = state.reviewCount,
                    )
                }
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
    badge: Badge,
    ranking: Int,
    restReportCount: Int,
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
    onFoodSpotContentClick: (Long) -> Unit,
    onReviewHistoryClick: () -> Unit,
    onRankingButtonClick: () -> Unit,
    onUserInfoEditButtonClick: () -> Unit,
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
            ProfileInfo(
                modifier = Modifier.align(Alignment.Start),
                profileImage = profileImage?.toUri(),
                coin = coin,
                badge = badge,
                ranking = ranking,
                onRankingButtonClick = onRankingButtonClick,
                onUserInfoEditButtonClick = onUserInfoEditButtonClick,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = nickname,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.my_page_rest_report_count, restReportCount),
                style = MaterialTheme.typography.titleSmall,
                color = Gray1,
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
                onFoodSpotContentClick = onFoodSpotContentClick,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileInfo(
    modifier: Modifier = Modifier,
    profileImage: Uri?,
    coin: Int,
    badge: Badge,
    ranking: Int,
    onRankingButtonClick: () -> Unit,
    onUserInfoEditButtonClick: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(modifier = Modifier.width(24.dp))
        EditableProfileImage(
            modifier = Modifier.size(160.dp),
            imgUri = profileImage,
            onProfileImageClick = onUserInfoEditButtonClick,
        )
        Spacer(modifier = Modifier.width(28.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            UserInfoItem(
                title = stringResource(R.string.my_page_coin_title),
                content = NumberFormat.getInstance(Locale.getDefault()).format(coin),
                textColor = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary,
            )
            UserInfoItem(
                title = stringResource(R.string.my_page_badge_title),
                content = badge.toUi(LocalContext.current),
                textColor = MaterialTheme.colorScheme.onSecondary,
                backgroundColor = MaterialTheme.colorScheme.secondary,
            )
            Row {
                UserInfoItem(
                    title = stringResource(R.string.my_page_ranking_title),
                    content = stringResource(R.string.my_page_ranking_content, ranking),
                    textColor = MaterialTheme.colorScheme.primary,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    borderColor = MaterialTheme.colorScheme.primary,
                )
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    IconButton(
                        onClick = { onRankingButtonClick() },
                        modifier = Modifier.offset(y = (-2).dp),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            tint = Gray1,
                            contentDescription = "NavigateToRankingButton",
                        )
                    }
                }
            }
        }
    }
}

private fun Badge.toUi(context: Context) =
    when (this) {
        Badge.BEGINNER -> context.getString(R.string.badge_beginner)
        Badge.INTERMEDIATE -> context.getString(R.string.badge_intermediate)
        Badge.EXPERT -> context.getString(R.string.badge_expert)
        Badge.SUPER_EXPERT -> context.getString(R.string.badge_super_expert)
        Badge.UNKNOWN -> ""
    }

@Composable
private fun ReportedFoodSpot(
    modifier: Modifier = Modifier,
    foodSpotHistory: FoodSpotHistoryContent?,
    onFoodSpotHistoryClick: () -> Unit,
    onFoodSpotContentClick: (Long) -> Unit,
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
        FoodSpotItem(
            modifier = Modifier.clickable { onFoodSpotContentClick(foodSpotHistory.foodSpotsId) },
            foodSpot = foodSpotHistory,
        )
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
            modifier =
                Modifier
                    .padding(4.dp)
                    .size(24.dp),
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
                    isFoodTruck = true,
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
            onFoodSpotContentClick = { _ -> },
            badge = Badge.BEGINNER,
            ranking = 2,
            restReportCount = 3,
            onRankingButtonClick = {},
            onUserInfoEditButtonClick = {},
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
            onFoodSpotContentClick = { _ -> },
            badge = Badge.BEGINNER,
            ranking = 2,
            restReportCount = 0,
            onRankingButtonClick = {},
            onUserInfoEditButtonClick = {},
        )
    }
}

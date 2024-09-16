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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import com.weit2nd.presentation.ui.common.ReviewItem
import com.weit2nd.presentation.ui.theme.DarkGray
import com.weit2nd.presentation.ui.theme.Gray1
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

            MyPageSideEffect.NavToBack -> {
                navToBack()
            }
        }
    }

    LaunchedEffect(Unit) {
        vm.onCreate()
    }

    Scaffold(
        topBar = {
            BackTopBar(
                title = stringResource(R.string.my_page_toolbar_title),
                onClickBackBtn = vm::onBackButtonClick,
            )
        },
    ) {
        MyPageContent(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(it),
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
        )
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
) {
    Box(
        modifier = modifier,
    ) {
        if (isLogoutDialogShown) {
            CommonAlertDialog(
                modifier = Modifier.align(Alignment.Center),
                title = "로그아웃",
                contents = "진짜 해요?",
                onPositiveButtonClick = onLogoutConfirm,
                onNegativeButtonClick = onLogoutDialogClose,
                positiveButtonText = "맞아",
                negativeButtonText = "잘못 눌렀어",
            )
        }
        if (isWithdrawDialogShown) {
            CommonAlertDialog(
                modifier = Modifier.align(Alignment.Center),
                title = "회원탈퇴",
                contents = "진짜 해요?",
                onPositiveButtonClick = onWithdrawConfirm,
                onNegativeButtonClick = onWithdrawDialogClose,
                positiveButtonText = "맞아",
                negativeButtonText = "잘못 눌렀어",
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = nickname,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "내가 제보한 음식점 ()",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    tint = DarkGray,
                    contentDescription = "",
                )
            }
            if (foodSpotHistory != null) {
                FoodSpotItem(foodSpot = foodSpotHistory)
            } else {
                Text(
                    modifier = Modifier.padding(vertical = 32.dp),
                    text = "제보한 음식점이 없습니다.",
                    style = MaterialTheme.typography.titleLarge,
                    color = Gray1,
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "내가 쓴 리뷰 ()",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    tint = DarkGray,
                    contentDescription = "",
                )
            }
            if (review != null) {
                ReviewItem(review = review, onImageClick = { _, _ -> })
            } else {
                Text(
                    modifier = Modifier.padding(vertical = 32.dp),
                    text = "작성한 리뷰가 없습니다.",
                    style = MaterialTheme.typography.titleLarge,
                    color = Gray1,
                )
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable { onLogoutButtonClick() },
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    modifier = Modifier.padding(4.dp),
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
        )
    }
}

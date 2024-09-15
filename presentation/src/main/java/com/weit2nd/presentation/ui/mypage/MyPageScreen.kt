package com.weit2nd.presentation.ui.mypage

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.weit2nd.presentation.R
import com.weit2nd.presentation.ui.common.BackTopBar
import com.weit2nd.presentation.ui.common.CommonAlertDialog
import com.weit2nd.presentation.ui.common.EditableProfileImage
import com.weit2nd.presentation.ui.theme.DarkGray
import com.weit2nd.presentation.ui.theme.Gray1
import com.weit2nd.presentation.ui.theme.RoadyFoodyTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MyPageScreen(
    navToLogin: () -> Unit,
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
        modifier = modifier.padding(horizontal = 16.dp),
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
            HorizontalDivider()
        }
        Row(
            modifier =
                Modifier
                    .align(Alignment.BottomStart)
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

        Text(
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .clickable { onWithdrawButtonClick() }
                    .padding(bottom = 8.dp),
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

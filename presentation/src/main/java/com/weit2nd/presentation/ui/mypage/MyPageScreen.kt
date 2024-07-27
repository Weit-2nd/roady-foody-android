package com.weit2nd.presentation.ui.mypage

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.weit2nd.presentation.ui.common.CommonAlertDialog
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MyPageScreen(
    navToLogin: () -> Unit,
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
        }
    }
    Scaffold {
        MyPageContent(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(it),
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
        contentAlignment = Alignment.Center,
    ) {
        if (isLogoutDialogShown) {
            CommonAlertDialog(
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
                title = "회원탈퇴",
                contents = "진짜 해요?",
                onPositiveButtonClick = onWithdrawConfirm,
                onNegativeButtonClick = onWithdrawDialogClose,
                positiveButtonText = "맞아",
                negativeButtonText = "잘못 눌렀어",
            )
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = onLogoutButtonClick,
            ) {
                Text(
                    text = "로그아웃",
                )
            }
            Button(
                onClick = onWithdrawButtonClick,
            ) {
                Text(
                    text = "회원탈퇴",
                )
            }
        }
    }
}

@Preview
@Composable
private fun MyPageContentPreview() {
    MyPageContent(
        modifier = Modifier.fillMaxSize(),
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

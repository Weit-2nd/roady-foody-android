package com.weit2nd.presentation.navigation

import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.weit2nd.domain.model.User
import com.weit2nd.presentation.navigation.type.UserType
import com.weit2nd.presentation.navigation.dto.toUserDTO
import com.weit2nd.presentation.ui.home.HomeScreen
import com.weit2nd.presentation.ui.login.LoginScreen
import com.weit2nd.presentation.ui.select.picture.SelectPictureScreen


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = LoginNavRoutes.GRAPH,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        loginComposable(navController)
        homeComposable(navController)
        selectPictureComposable(navController)
    }
}

private fun NavGraphBuilder.loginComposable(
    navController: NavHostController,
) {
    composable(LoginNavRoutes.GRAPH) {
        LoginScreen(
            navToHome = { user ->
                navController.navigateToHome(user) {
                    popUpTo(LoginNavRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.homeComposable(
    navController: NavHostController,
) {
    composable(
        route = "${HomeNavRoutes.GRAPH}/{${HomeNavRoutes.USER_STATE_KEY}}",
        arguments = listOf(navArgument(HomeNavRoutes.USER_STATE_KEY) { type = UserType() }),
    ) {
        HomeScreen()
    }
}

private fun NavGraphBuilder.selectPictureComposable(
    navController: NavHostController,
) {
    composable(SelectPictureRoutes.GRAPH) {
        SelectPictureScreen()
    }
}

private fun NavHostController.navigateToHome(
    user: User,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val userJson = Uri.encode(Gson().toJson(user.toUserDTO()))
    navigate("${HomeNavRoutes.GRAPH}/$userJson", builder)
}

object LoginNavRoutes {
    const val GRAPH = "login"
}

object HomeNavRoutes {
    const val GRAPH = "home"
    const val USER_STATE_KEY = "user"
}

object SelectPictureRoutes {
    const val GRAPH = "select_picture"
}

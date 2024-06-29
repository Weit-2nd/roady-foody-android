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
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.User
import com.weit2nd.presentation.navigation.dto.toCoordinateDTO
import com.weit2nd.presentation.navigation.dto.toImageViewerDTO
import com.weit2nd.presentation.navigation.type.UserType
import com.weit2nd.presentation.navigation.dto.toUserDTO
import com.weit2nd.presentation.navigation.type.CoordinateType
import com.weit2nd.presentation.navigation.type.ImageViewerDataType
import com.weit2nd.presentation.ui.common.imageviewer.ImageViewerData
import com.weit2nd.presentation.ui.common.imageviewer.ImageViewerScreen
import com.weit2nd.presentation.ui.home.HomeScreen
import com.weit2nd.presentation.ui.login.LoginScreen
import com.weit2nd.presentation.ui.select.location.SelectLocationScreen
import com.weit2nd.presentation.ui.select.location.map.SelectLocationMapScreen
import com.weit2nd.presentation.ui.signup.SignUpScreen
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
        signUpComposable(navController)
        homeComposable(navController)
        selectPictureComposable(navController)
        selectLocationComposable(navController)
        selectLocationMapComposable(navController)
        imageViewerComposable(navController)
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
            },
            navToSignUp = {
                navController.navigate(SignUpNavRoutes.GRAPH) {
                    popUpTo(LoginNavRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.signUpComposable(
    navController: NavHostController,
) {
    composable(route = SignUpNavRoutes.GRAPH) {
        SignUpScreen(
            navToHome = { user ->
                navController.navigateToHome(user) {
                    popUpTo(SignUpNavRoutes.GRAPH) {
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

private fun NavGraphBuilder.selectLocationComposable(
    navController: NavHostController,
) {
    composable(SelectLocationRoutes.GRAPH) {
        SelectLocationScreen(
            navToMap = {
                navController.navigateToSelectLocationMap() {
                    popUpTo(SelectLocationMapRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.selectLocationMapComposable(
    navController: NavHostController,
) {
    composable(
        route = "${SelectLocationMapRoutes.GRAPH}/{${SelectLocationMapRoutes.INITIAL_POSITION_KEY}}",
        arguments = listOf(navArgument(SelectLocationMapRoutes.INITIAL_POSITION_KEY) {
            type = CoordinateType()
        }),
    ) {
        SelectLocationMapScreen()
    }
}

private fun NavGraphBuilder.imageViewerComposable(
    navController: NavHostController,
) {
    composable(
        route = "${ImageViewerRoutes.GRAPH}/{${ImageViewerRoutes.IMAGES_VIEWER_DATA_KEY}}",
        arguments = listOf(navArgument(ImageViewerRoutes.IMAGES_VIEWER_DATA_KEY) {
            type = ImageViewerDataType()
        }),
    ) {
        ImageViewerScreen(
            onExitBtnClick = { navController.popBackStack() }
        )
    }
}

private fun NavHostController.navigateToHome(
    user: User,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val userJson = Uri.encode(Gson().toJson(user.toUserDTO()))
    navigate("${HomeNavRoutes.GRAPH}/$userJson", builder)
}

private fun NavHostController.navigateToSelectLocationMap(
    coordinate: Coordinate = Coordinate(37.56, 126.94),
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val coordinateJson = Uri.encode(Gson().toJson(coordinate.toCoordinateDTO()))
    navigate("${SelectLocationMapRoutes.GRAPH}/$coordinateJson", builder)
}

private fun NavHostController.navigateToImageViewer(
    images: List<String> = listOf(),
    position: Int = 0,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val data = ImageViewerData(images, position)
    val imageDataJson = Uri.encode(Gson().toJson(data.toImageViewerDTO()))
    navigate("${ImageViewerRoutes.GRAPH}/$imageDataJson", builder)
}

object LoginNavRoutes {
    const val GRAPH = "login"
}

object SignUpNavRoutes {
    const val GRAPH = "signup"
}

object HomeNavRoutes {
    const val GRAPH = "home"
    const val USER_STATE_KEY = "user"
}

object SelectPictureRoutes {
    const val GRAPH = "select_picture"
}

object SelectLocationRoutes {
    const val GRAPH = "select_location"
}

object SelectLocationMapRoutes {
    const val GRAPH = "select_location_map"
    const val INITIAL_POSITION_KEY = "initial_position"
}

object ImageViewerRoutes {
    const val GRAPH = "image_viewer"
    const val IMAGES_VIEWER_DATA_KEY = "image_viewer_data"
}

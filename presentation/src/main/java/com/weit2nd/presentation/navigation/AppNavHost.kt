package com.weit2nd.presentation.navigation

import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.weit2nd.domain.model.Coordinate
import com.weit2nd.domain.model.User
import com.weit2nd.presentation.navigation.dto.FoodSpotForReviewDTO
import com.weit2nd.presentation.navigation.dto.toCoordinateDTO
import com.weit2nd.presentation.navigation.dto.toImageViewerDTO
import com.weit2nd.presentation.navigation.dto.toPlaceDTO
import com.weit2nd.presentation.navigation.dto.toTermIdsDTO
import com.weit2nd.presentation.navigation.dto.toUserDTO
import com.weit2nd.presentation.navigation.type.CoordinateType
import com.weit2nd.presentation.navigation.type.FoodSpotForReviewType
import com.weit2nd.presentation.navigation.type.ImageViewerDataType
import com.weit2nd.presentation.navigation.type.TermIdsType
import com.weit2nd.presentation.navigation.type.UserType
import com.weit2nd.presentation.ui.common.imageviewer.ImageViewerData
import com.weit2nd.presentation.ui.common.imageviewer.ImageViewerScreen
import com.weit2nd.presentation.ui.foodspot.report.FoodSpotReportScreen
import com.weit2nd.presentation.ui.home.HomeScreen
import com.weit2nd.presentation.ui.login.LoginScreen
import com.weit2nd.presentation.ui.mypage.MyPageScreen
import com.weit2nd.presentation.ui.review.PostReviewScreen
import com.weit2nd.presentation.ui.search.SearchScreen
import com.weit2nd.presentation.ui.select.picture.SelectPictureScreen
import com.weit2nd.presentation.ui.select.place.SelectPlaceScreen
import com.weit2nd.presentation.ui.select.place.map.SelectPlaceMapScreen
import com.weit2nd.presentation.ui.signup.SignUpScreen
import com.weit2nd.presentation.ui.signup.terms.TermsScreen
import com.weit2nd.presentation.ui.signup.terms.detail.TermDetailScreen
import com.weit2nd.presentation.ui.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = SplashRoutes.GRAPH,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
    ) {
        splashComposable(navController)
        loginComposable(navController)
        termsComposable(navController)
        signUpComposable(navController)
        homeComposable(navController)
        selectPictureComposable(navController)
        selectPlaceComposable(navController)
        selectPlaceMapComposable(navController)
        termDetailComposable(navController)
        imageViewerComposable(navController)
        foodSpotReportComposable(navController)
        myPageComposable(navController)
        postReviewComposable(navController)
        searchComposable(navController)
    }
}

private fun NavGraphBuilder.splashComposable(navController: NavHostController) {
    composable(SplashRoutes.GRAPH) {
        SplashScreen(
            navToLogin = {
                navController.navigateToLogin {
                    popUpTo(SplashRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            },
            navToHome = {
                navController.navigateToHome(User("임시")) {
                    popUpTo(SplashRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            },
        )
    }
}

private fun NavGraphBuilder.loginComposable(navController: NavHostController) {
    composable(LoginNavRoutes.GRAPH) {
        LoginScreen(
            navToHome = { user ->
                navController.navigateToHome(user) {
                    popUpTo(LoginNavRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            },
            navToTermAgreement = {
                navController.navigate(TermsRoutes.GRAPH) {
                    popUpTo(LoginNavRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            },
        )
    }
}

private fun NavGraphBuilder.termsComposable(navController: NavHostController) {
    composable(route = TermsRoutes.GRAPH) {
        TermsScreen(
            navToSignUp = { agreedTermIds ->
                navController.navigateToSignUp(agreedTermIds) {
                    popUpTo(TermsRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            },
            navToTermDetail = { id ->
                navController.navigateToTermDetail(id)
            },
        )
    }
}

private fun NavGraphBuilder.termDetailComposable(navController: NavHostController) {
    composable(
        "${TermDetailRoutes.GRAPH}/{${TermDetailRoutes.TERM_ID}}",
        arguments = listOf(navArgument(TermDetailRoutes.TERM_ID) { type = NavType.LongType }),
    ) {
        TermDetailScreen(
            navToBack = {
                navController.popBackStack()
            },
        )
    }
}

private fun NavGraphBuilder.signUpComposable(navController: NavHostController) {
    composable(
        route = "${SignUpNavRoutes.GRAPH}/{${SignUpNavRoutes.TERM_IDS}}",
        arguments = listOf(navArgument(SignUpNavRoutes.TERM_IDS) { type = TermIdsType() }),
    ) {
        SignUpScreen(
            navToHome = { user ->
                navController.navigateToHome(user) {
                    popUpTo("${SignUpNavRoutes.GRAPH}/{${SignUpNavRoutes.TERM_IDS}}") {
                        inclusive = true
                    }
                }
            },
        )
    }
}

private fun NavGraphBuilder.homeComposable(navController: NavHostController) {
    composable(
        route = "${HomeNavRoutes.GRAPH}/{${HomeNavRoutes.USER_STATE_KEY}}",
        arguments = listOf(navArgument(HomeNavRoutes.USER_STATE_KEY) { type = UserType() }),
    ) {
        HomeScreen(
            navToFoodSpotReport = {
                navController.navigate(FoodSpotReportRoutes.GRAPH)
            },
            navToMyPage = {
                navController.navigateToMyPage()
            },
        )
    }
}

private fun NavGraphBuilder.selectPictureComposable(navController: NavHostController) {
    composable(SelectPictureRoutes.GRAPH) {
        SelectPictureScreen()
    }
}

private fun NavGraphBuilder.selectPlaceComposable(navController: NavHostController) {
    composable(SelectPlaceRoutes.GRAPH) {
        SelectPlaceScreen(
            navToMap = {
                navController.navigateToSelectLocationMap {
                    popUpTo(SelectPlaceMapRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            },
            onSelectPlace = { place ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    SelectPlaceRoutes.SELECT_PLACE_KEY,
                    place.toPlaceDTO(),
                )
                navController.popBackStack()
            },
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.selectPlaceMapComposable(navController: NavHostController) {
    composable(
        route = "${SelectPlaceMapRoutes.GRAPH}/{${SelectPlaceMapRoutes.INITIAL_POSITION_KEY}}",
        arguments =
            listOf(
                navArgument(SelectPlaceMapRoutes.INITIAL_POSITION_KEY) {
                    type = CoordinateType()
                },
            ),
    ) {
        SelectPlaceMapScreen(
            onSelectPlace = { place ->
                navController.previousBackStackEntry?.savedStateHandle?.set(
                    SelectPlaceMapRoutes.SELECT_PLACE_KEY,
                    place.toPlaceDTO(),
                )
                navController.popBackStack()
            },
        )
    }
}

private fun NavGraphBuilder.imageViewerComposable(navController: NavHostController) {
    composable(
        route = "${ImageViewerRoutes.GRAPH}/{${ImageViewerRoutes.IMAGES_VIEWER_DATA_KEY}}",
        arguments =
            listOf(
                navArgument(ImageViewerRoutes.IMAGES_VIEWER_DATA_KEY) {
                    type = ImageViewerDataType()
                },
            ),
    ) {
        ImageViewerScreen(
            onExitBtnClick = { navController.popBackStack() },
        )
    }
}

private fun NavGraphBuilder.foodSpotReportComposable(navController: NavHostController) {
    composable(FoodSpotReportRoutes.GRAPH) {
        FoodSpotReportScreen(
            navToSelectPlace = {
                navController.navigate(SelectPlaceRoutes.GRAPH)
            },
            navToBack = {
                navController.popBackStack()
            },
            navController = navController,
        )
    }
}

private fun NavGraphBuilder.myPageComposable(navController: NavHostController) {
    composable(MyPageRoutes.GRAPH) {
        MyPageScreen(
            navToLogin = {
                navController.navigateToLogin {
                    popUpTo(MyPageRoutes.GRAPH) {
                        inclusive = true
                    }
                }
            },
        )
    }
}

private fun NavGraphBuilder.postReviewComposable(navController: NavHostController) {
    composable(
        route = "${PostReviewRoutes.GRAPH}/{${PostReviewRoutes.FOOD_SPOT_FOR_REVIEW_KEY}}",
        arguments =
            listOf(
                navArgument(PostReviewRoutes.FOOD_SPOT_FOR_REVIEW_KEY) {
                    type = FoodSpotForReviewType()
                },
            ),
    ) {
        PostReviewScreen(
            navToBack = {
                navController.popBackStack()
            },
        )
    }
}

private fun NavGraphBuilder.searchComposable(navController: NavHostController) {
    composable(
        route = "${SearchRoutes.GRAPH}/{${SearchRoutes.INITIAL_SEARCH_WORDS_KEY}}",
        arguments =
            listOf(
                navArgument(SearchRoutes.INITIAL_SEARCH_WORDS_KEY) {
                    type = NavType.StringType
                },
            ),
    ) {
        SearchScreen(
            navToMap = { },
            navToBack = {
                navController.popBackStack()
            },
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
    navigate("${SelectPlaceMapRoutes.GRAPH}/$coordinateJson", builder)
}

private fun NavHostController.navigateToTermDetail(termId: Long) {
    navigate("${TermDetailRoutes.GRAPH}/$termId")
}

private fun NavHostController.navigateToSignUp(
    agreedTermIds: List<Long>,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val agreedTermIdsJson = Uri.encode(Gson().toJson(agreedTermIds.toTermIdsDTO()))
    navigate("${SignUpNavRoutes.GRAPH}/$agreedTermIdsJson", builder)
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

private fun NavHostController.navigateToMyPage(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(MyPageRoutes.GRAPH, builder)
}

private fun NavHostController.navigateToLogin(builder: NavOptionsBuilder.() -> Unit = {}) {
    navigate(LoginNavRoutes.GRAPH, builder)
}

private fun NavHostController.navigateToPostReview(
    foodSpotForReviewDTO: FoodSpotForReviewDTO,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    val json = Uri.encode(Gson().toJson(foodSpotForReviewDTO))
    navigate("${PostReviewRoutes.GRAPH}/$json", builder)
}

private fun NavHostController.navigateToSearch(
    initialSearchWords: String,
    builder: NavOptionsBuilder.() -> Unit = {},
) {
    navigate("${SearchRoutes.GRAPH}/$initialSearchWords", builder)
}

object SplashRoutes {
    const val GRAPH = "splash"
}

object LoginNavRoutes {
    const val GRAPH = "login"
}

object TermsRoutes {
    const val GRAPH = "terms"
}

object TermDetailRoutes {
    const val GRAPH = "term_detail"
    const val TERM_ID = "term_id"
}

object SignUpNavRoutes {
    const val GRAPH = "signup"
    const val TERM_IDS = "agreed_term_ids"
}

object HomeNavRoutes {
    const val GRAPH = "home"
    const val USER_STATE_KEY = "user"
}

object SelectPictureRoutes {
    const val GRAPH = "select_picture"
}

object SelectPlaceRoutes {
    const val GRAPH = "select_place"
    const val SELECT_PLACE_KEY = "select_place_selected_key"
}

object SelectPlaceMapRoutes {
    const val GRAPH = "select_place_map"
    const val INITIAL_POSITION_KEY = "initial_position"
    const val SELECT_PLACE_KEY = "select_place_map_selected_key"
}

object ImageViewerRoutes {
    const val GRAPH = "image_viewer"
    const val IMAGES_VIEWER_DATA_KEY = "image_viewer_data"
}

object FoodSpotReportRoutes {
    const val GRAPH = "food_spot_report"
}

object MyPageRoutes {
    const val GRAPH = "my_page"
}

object PostReviewRoutes {
    const val GRAPH = "post_review"
    const val FOOD_SPOT_FOR_REVIEW_KEY = "food_spot_for_review_key"
}

object SearchRoutes {
    const val GRAPH = "search"
    const val INITIAL_SEARCH_WORDS_KEY = "initial_search_words_key"
}

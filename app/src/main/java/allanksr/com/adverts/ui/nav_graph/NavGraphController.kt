package allanksr.com.adverts.ui.nav_graph

import allanksr.com.adverts.admob.AdsViewModel
import allanksr.com.adverts.ui.screens.AdvertScreen
import allanksr.com.adverts.ui.screens.MainScreen
import allanksr.com.adverts.ui.screens.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraphController(
    adsViewModel: AdsViewModel
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.SplashScreen.route
    ) {
        composable(
            route = Route.SplashScreen.route,
            content = {
                SplashScreen(
                    navController = navController,
                )
            }
        )
        composable(
            route = Route.MainScreen.route,
            content = {
                MainScreen(
                    navController = navController,
                    adsViewModel = adsViewModel
                )
            }
        )
        composable(
            route = Route.AdvertScreen.route,
            content = {
                AdvertScreen(
                    adsViewModel = adsViewModel
                )
            }
        )
    }
}
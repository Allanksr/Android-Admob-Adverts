package allanksr.com.adverts.ui.nav_graph

sealed class Route(val route: String) {
    object SplashScreen : Route("splash_screen")
    object MainScreen : Route("main_screen")
    object AdvertScreen : Route("advert_screen")
}
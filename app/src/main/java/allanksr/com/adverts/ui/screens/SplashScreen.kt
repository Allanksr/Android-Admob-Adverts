package allanksr.com.adverts.ui.screens

import allanksr.com.adverts.R
import allanksr.com.adverts.ui.nav_graph.Route
import allanksr.com.adverts.ui.util_composable.GlowIndicatorLoader
import allanksr.com.adverts.util.Constants.SPLASH_DELAY
import allanksr.com.adverts.util.ScreenPosition
import allanksr.com.adverts.util.toastShort
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.text.font.Font
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SplashScreen(
    navController: NavController,
) {
    val activityContext = LocalContext.current as ComponentActivity
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenState = remember { mutableStateOf(ScreenPosition.Start) }

    val offsetAnimation: Dp by animateDpAsState(
        if (screenState.value == ScreenPosition.Start) -screenWidth / 10 else 0.dp,
        tween(1500, easing = FastOutSlowInEasing)
    )

    val refreshState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        screenState.value = ScreenPosition.Finish
        refreshState.value = true
        delay(SPLASH_DELAY)
        navController.popBackStack()
        navController.navigate(Route.MainScreen.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .absoluteOffset(x = offsetAnimation),
    ) {
        AllanKsr(
            activityContext = activityContext,
            refreshState = refreshState
        )
    }
    BackHandler(
        onBack = {
            activityContext.finish()
        }
    )
}

@Composable
fun AllanKsr(
    activityContext: Context,
    refreshState: MutableState<Boolean>
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        activityContext.toastShort("Wait....")
                    },
                horizontalArrangement = Arrangement.Center,
                content = {
                    Text(
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                            fontFamily = FontFamily(Font(R.font.odibee_sans_regular)),
                            fontWeight = FontWeight.Normal,
                            fontSize = 50.sp,
                            lineHeight = 50.sp,
                            letterSpacing = 0.7.sp,
                        ),
                        text = "Allanksr"
                    )
                }
            )
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                content = {
                    GlowIndicatorLoader(
                        refreshState = refreshState,
                        refreshTriggerDistance = 8.dp
                    )
                }
            )
        }
    )
}



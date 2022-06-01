@file:OptIn(ExperimentalMaterial3Api::class)
package allanksr.com.adverts.ui.screens

import allanksr.com.adverts.admob.AdsViewModel
import allanksr.com.adverts.admob.model.AdsInterstitialState
import allanksr.com.adverts.admob.model.AdsRewardState
import allanksr.com.adverts.admob.model.AdsViewState
import allanksr.com.adverts.admob.model.LoaderState
import allanksr.com.adverts.ui.nav_graph.Route
import allanksr.com.adverts.util.Constants.logTag
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    navController: NavController,
    adsViewModel: AdsViewModel = hiltViewModel(),
) {

    val localLifecycle = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    val interstitialLoader = remember {
        mutableStateOf(AdsInterstitialState())
    }
    val rewardedLoader = remember {
        mutableStateOf(AdsRewardState())
    }
    val viewLoader = remember {
        mutableStateOf(AdsViewState())
    }

    val interstitialState = remember {
        mutableStateOf(LoaderState())
    }
    val rewardedState = remember {
        mutableStateOf(LoaderState())
    }
    val viewState = remember {
        mutableStateOf(LoaderState())
    }

    LaunchedEffect(key1 = true) {
        Log.d("$logTag LaunchedEffect", "observers")
        adsViewModel.adsInterstitialStateData.observe(localLifecycle) {
            Log.d("$logTag Interstitial", "hasLoaded: ${it.loaded}")
            scope.launch {
                delay(3000)
                interstitialState.value = LoaderState(
                    loaded = it.loaded,
                    error = it.error
                )
                interstitialLoader.value = it
            }
        }

        adsViewModel.adsRewardedAdStateData.observe(localLifecycle) {
            Log.d("$logTag Rewarded", "hasLoaded: ${it.loaded}")
            scope.launch {
                delay(5000)
                rewardedState.value = LoaderState(
                    loaded = it.loaded,
                    error = it.error
                )
                rewardedLoader.value = it
            }
        }

        adsViewModel.adsViewAdStateData.observe(localLifecycle) {
            Log.d("$logTag adView", "hasLoaded:${it.loaded}")
            scope.launch {
                delay(1000)
                viewState.value = LoaderState(
                    loaded = it.loaded,
                    error = it.error
                )
                viewLoader.value = it
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text(
                        text = "Loading ads in viewModel",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )

            ButtonLoader(
                nextScreen = {
                    if (interstitialLoader.value.interstitialAd != null) {
                        adsViewModel.adsInterstitialStateData.removeObservers(localLifecycle)
                        navController.popBackStack()
                        navController.navigate(Route.AdvertScreen.route)
                    }
                },
                advertContent = {
                    IconLoader(
                        iconState = interstitialState,
                        successDescription = "Interstitial loaded",
                        failDescription = "Interstitial fail"
                    )
                },
                advertName = "Interstitial ads"
            )

            ButtonLoader(
                nextScreen = {
                    if (rewardedLoader.value.rewardedAd != null) {
                        adsViewModel.adsRewardedAdStateData.removeObservers(localLifecycle)
                        navController.popBackStack()
                        navController.navigate(Route.AdvertScreen.route)
                    }
                },
                advertContent = {
                    IconLoader(
                        iconState = rewardedState,
                        successDescription = "Rewarded loaded",
                        failDescription = "Rewarded fail"
                    )
                },
                advertName = "Rewarded ads"
            )

            ButtonLoader(
                nextScreen = {
                    if (viewLoader.value.adView != null) {
                        adsViewModel.adsViewAdStateData.removeObservers(localLifecycle)
                        navController.popBackStack()
                        navController.navigate(Route.AdvertScreen.route)
                    }
                },
                advertContent = {
                    IconLoader(
                        iconState = viewState,
                        successDescription = "View loaded",
                        failDescription = "View fail"
                    )
                },
                advertName = "adView ads"
            )
        }
    )
}

@Composable
fun ButtonLoader(
    nextScreen: () -> Unit,
    advertContent: @Composable RowScope.() -> Unit,
    advertName: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .clickable {
                nextScreen()
            },
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(25.dp),
        border = BorderStroke(1.5.dp, color = MaterialTheme.colorScheme.primary),
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        content = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.Top,
                                content = {
                                    Text(
                                        modifier = Modifier
                                            .padding(start = 20.dp, top = 10.dp, bottom = 10.dp),
                                        text = advertName,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.Top,
                                content = {
                                    advertContent()
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun IconLoader(
    iconState: MutableState<LoaderState>,
    successDescription: String,
    failDescription: String
) {
    if (!iconState.value.error && !iconState.value.loaded) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(20.dp)
                .size(30.dp),
            color = MaterialTheme.colorScheme.onSecondary
        )
    } else {
        if (iconState.value.loaded) {
            Icon(
                modifier = Modifier
                    .padding(20.dp)
                    .size(30.dp),
                imageVector = Icons.Default.Check,
                contentDescription = successDescription,
                tint = MaterialTheme.colorScheme.secondary
            )
        } else {
            Icon(
                modifier = Modifier
                    .padding(20.dp)
                    .size(30.dp),
                imageVector = Icons.Default.Close,
                contentDescription = failDescription,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

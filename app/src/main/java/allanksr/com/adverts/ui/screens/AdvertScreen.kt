@file:OptIn(ExperimentalMaterial3Api::class)

package allanksr.com.adverts.ui.screens

import allanksr.com.adverts.admob.AdsViewModel
import allanksr.com.adverts.admob.model.AdsInterstitialState
import allanksr.com.adverts.admob.model.AdsRewardState
import allanksr.com.adverts.admob.model.AdsViewState
import allanksr.com.adverts.util.Constants.logTag
import allanksr.com.adverts.util.toastLong
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.ads.*

@Composable
fun AdvertScreen(
    adsViewModel: AdsViewModel = hiltViewModel(),
) {

    val localLifecycle = LocalLifecycleOwner.current
    val applicationContext = LocalContext.current.applicationContext
    val activityContext = LocalContext.current as ComponentActivity

    val interstitialData = remember {
        mutableStateOf(AdsInterstitialState())
    }
    val rewardedData = remember {
        mutableStateOf(AdsRewardState())
    }
    val viewData = remember {
        mutableStateOf(AdsViewState())
    }

    val showBanner = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        adsViewModel.adsInterstitialStateData.observe(localLifecycle) {
            if (it.loaded) {
                interstitialData.value = AdsInterstitialState(
                    loaded = true,
                    interstitialAd = it.interstitialAd,
                    hasShown = false,
                    error = it.error
                )
                it.interstitialAd?.fullScreenContentCallback =
                    object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {
                            interstitialData.value = AdsInterstitialState(
                                loaded = false,
                                interstitialAd = null,
                                hasShown = false,
                                error = false
                            )
                            Log.d("$logTag Interstitial", "The ad was dismissed.")
                        }

                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                            interstitialData.value = AdsInterstitialState(
                                loaded = false,
                                interstitialAd = null,
                                hasShown = false,
                                error = true
                            )
                            Log.d(
                                "$logTag Interstitial",
                                "The ad failed to show: ${adError.message}"
                            )
                        }

                        override fun onAdShowedFullScreenContent() {
                            interstitialData.value = AdsInterstitialState(
                                loaded = false,
                                interstitialAd = null,
                                hasShown = false,
                                error = false
                            )
                            Log.d("$logTag Interstitial", "The ad was shown.")
                        }
                    }
                Log.d("$logTag Interstitial", "hasLoaded: ${it.loaded}")
            }
        }

        adsViewModel.adsRewardedAdStateData.observe(localLifecycle) {
            if (it.loaded) {
                rewardedData.value = AdsRewardState(
                    loaded = true,
                    rewardedAd = it.rewardedAd,
                    hasShown = false,
                    error = it.error
                )
                it.rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        rewardedData.value = AdsRewardState(
                            loaded = false,
                            rewardedAd = null,
                            hasShown = false,
                            error = false
                        )
                        Log.d("$logTag Rewarded", "The ad was dismissed.")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        rewardedData.value = AdsRewardState(
                            loaded = false,
                            rewardedAd = null,
                            hasShown = false,
                            error = true
                        )
                        Log.d("$logTag Rewarded", "The ad failed to show: ${adError.message}")
                    }

                    override fun onAdShowedFullScreenContent() {
                        rewardedData.value = AdsRewardState(
                            loaded = false,
                            rewardedAd = null,
                            hasShown = false,
                            error = false
                        )
                        Log.d("$logTag Rewarded", "The ad was shown.")
                    }
                }
                Log.d("$logTag Rewarded", "hasLoaded: ${it.loaded}")
            }
        }

        adsViewModel.adsViewAdStateData.observe(localLifecycle) {
            it.let {
                viewData.value = AdsViewState(
                    loaded = it.loaded,
                    adView = it.adView,
                    hasShown = false,
                    error = false
                )
            }
            Log.d("$logTag adsView", "hasLoaded: ${it.loaded}")
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
                        text = "Adverts already loaded in viewModel\n show them",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )

            ButtonShow(
                advertShow = {
                    if (!interstitialData.value.hasShown && interstitialData.value.interstitialAd != null) {
                        interstitialData.value.interstitialAd?.show(
                            activityContext
                        )
                    } else {
                        applicationContext.toastLong("Interstitial has already shown or not loaded yet")
                    }
                },
                advertName = "Show interstitial"
            )

            ButtonShow(
                advertShow = {
                    if (!rewardedData.value.hasShown && rewardedData.value.rewardedAd != null) {
                        rewardedData.value.rewardedAd?.show(
                            activityContext
                        ) { rewardItem ->
                            val rewardAmount = rewardItem.amount
                            val rewardType = rewardItem.type
                            Log.d(
                                "$logTag adsView",
                                "rewardAmount: $rewardAmount rewardType: $rewardType"
                            )
                            applicationContext.toastLong("rewardAmount: $rewardAmount rewardType: $rewardType")
                        }
                    } else {
                        applicationContext.toastLong("Rewarded has already shown or not loaded yet")
                    }
                },
                advertName = "Show rewarded"
            )

            ButtonShow(
                advertShow = {
                    if (!viewData.value.hasShown && viewData.value.adView != null) {
                        showBanner.value = true
                        viewData.value = viewData.value.copy(hasShown = true)
                    } else {
                        applicationContext.toastLong("Banner has already shown or not loaded yet")
                    }
                },
                advertName = "Show banner"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                content = {
                    Column(
                        content = {
                            if (showBanner.value) {
                                AndroidView(
                                    modifier = Modifier.fillMaxWidth(),
                                    factory = {
                                        viewData.value.adView!!
                                    }
                                )
                            }
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun ButtonShow(
    advertShow: () -> Unit,
    advertName: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        content = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 10.dp,
                        top = 10.dp,
                        end = 10.dp,
                        bottom = 10.dp,
                    )
                    .clickable {
                        advertShow()
                    },
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(1.5.dp, color = MaterialTheme.colorScheme.primary),
                content = {
                    Text(
                        modifier = Modifier.padding(start = 20.dp),
                        text = advertName,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    )
}
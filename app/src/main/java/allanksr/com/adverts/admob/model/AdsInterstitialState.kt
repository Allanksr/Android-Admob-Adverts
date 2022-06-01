package allanksr.com.adverts.admob.model

import com.google.android.gms.ads.interstitial.InterstitialAd

data class AdsInterstitialState(
    val loaded: Boolean = false,
    val interstitialAd: InterstitialAd? = null,
    val hasShown: Boolean = false,
    val error: Boolean = false
)

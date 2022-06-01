package allanksr.com.adverts.admob.model

import com.google.android.gms.ads.AdView

data class AdsViewState(
    val loaded: Boolean = false,
    val adView: AdView? = null,
    val hasShown: Boolean = false,
    val error: Boolean = false
)

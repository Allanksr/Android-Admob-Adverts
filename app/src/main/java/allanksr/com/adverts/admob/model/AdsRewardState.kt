package allanksr.com.adverts.admob.model

import com.google.android.gms.ads.rewarded.RewardedAd

data class AdsRewardState(
    val loaded: Boolean = false,
    val rewardedAd: RewardedAd? = null,
    val hasShown: Boolean = false,
    val error: Boolean = false
)

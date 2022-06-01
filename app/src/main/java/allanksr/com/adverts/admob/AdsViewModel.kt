package allanksr.com.adverts.admob

import allanksr.com.adverts.BaseApp
import allanksr.com.adverts.admob.model.AdsInterstitialState
import allanksr.com.adverts.admob.model.AdsRewardState
import allanksr.com.adverts.admob.model.AdsViewState
import allanksr.com.adverts.util.Constants.AdsBannerTest
import allanksr.com.adverts.util.Constants.AdsInterstitialTest
import allanksr.com.adverts.util.Constants.AdsVideoTest
import allanksr.com.adverts.util.Constants.logTag
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class AdsViewModel @Inject constructor(
    private val baseApp: BaseApp,
) : ViewModel() {

    private val _adsInterstitialStateData = MutableLiveData<AdsInterstitialState>()
    val adsInterstitialStateData: LiveData<AdsInterstitialState> = _adsInterstitialStateData
    fun advertInterstitial() {
        InterstitialAd.load(
            baseApp,
            AdsInterstitialTest,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.i("$logTag AdsViewModel", "interstitialAd onAdLoaded:${interstitialAd.responseInfo}")
                    _adsInterstitialStateData.value = AdsInterstitialState(
                        loaded = true,
                        interstitialAd = interstitialAd,
                        hasShown = false,
                        error = false
                    )
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.i("$logTag AdsViewModel", "interstitialAd adError:${adError.responseInfo}")
                    _adsInterstitialStateData.value = AdsInterstitialState(
                        loaded = false,
                        interstitialAd = null,
                        hasShown = false,
                        error = true
                    )
                }
            }
        )
    }

    private val _adsRewardedAdStateData = MutableLiveData<AdsRewardState>()
    val adsRewardedAdStateData: LiveData<AdsRewardState> = _adsRewardedAdStateData
    fun advertRewarded() {
        RewardedAd.load(
            baseApp,
            AdsVideoTest,
            AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.i("$logTag AdsViewModel", "rewardedAd onAdLoaded:${rewardedAd.responseInfo}")
                    _adsRewardedAdStateData.value = AdsRewardState(
                        loaded = true,
                        rewardedAd = rewardedAd,
                        hasShown = false,
                        error = false
                    )
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.i("$logTag AdsViewModel", "rewardedAd onAdFailedToLoad:${loadAdError.responseInfo}")
                    _adsRewardedAdStateData.value = AdsRewardState(
                        loaded = false,
                        rewardedAd = null,
                        hasShown = false,
                        error = true
                    )
                }
            }
        )
    }


    private val _adsViewAdStateData = MutableLiveData<AdsViewState>()
    val adsViewAdStateData: LiveData<AdsViewState> = _adsViewAdStateData
    fun advertView() {
        AdView(baseApp).apply {
            val adView = this
            setAdSize(AdSize.BANNER)
            adUnitId = AdsBannerTest
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    _adsViewAdStateData.value = AdsViewState(
                        loaded = true,
                        adView = adView,
                        hasShown = false,
                        error = false
                    )
                    Log.i("$logTag AdsViewModel", "adView onAdLoaded")
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    _adsViewAdStateData.value = AdsViewState(
                        loaded = false,
                        adView = null,
                        hasShown = false,
                        error = true
                    )
                    Log.i("$logTag AdsViewModel", "adView onAdFailedToLoad: ${loadAdError.responseInfo}")
                }

                override fun onAdImpression() {
                    _adsViewAdStateData.value = AdsViewState(
                        loaded = true,
                        adView = adView,
                        hasShown = false,
                        error = false
                    )
                    Log.i("$logTag AdsViewModel", "adView onAdImpression")
                }
            }
            loadAd(AdRequest.Builder().build())
            invalidate()
        }
    }

}



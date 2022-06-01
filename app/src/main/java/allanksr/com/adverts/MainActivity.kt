package allanksr.com.adverts

import allanksr.com.adverts.admob.AdsViewModel
import allanksr.com.adverts.ui.nav_graph.NavGraphController
import allanksr.com.adverts.ui.theme.AdmobAdvertsTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val adsViewModel: AdsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adsViewModel.advertInterstitial()
        adsViewModel.advertRewarded()
        adsViewModel.advertView()

        setContent {
            AdmobAdvertsTheme(
                darkTheme = true,
                content = {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,
                        content = {
                            NavGraphController(
                                adsViewModel = adsViewModel
                            )
                        }
                    )
                }
            )
        }
    }
}

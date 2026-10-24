package com.earningdecode.fruitslice

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {

    companion object {
        private const val GAME_URL = "https://fruitsliceearningapp.netlify.app"

        // TEST interstitial ad unit ID - REPLACE with your real interstitial ad
        // unit ID later (AdMob console > Apps > Ad units > your interstitial unit).
        private const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

        // Show an interstitial at most once every 2 minutes (120000 ms).
        private const val INTERSTITIAL_INTERVAL_MS = 120000L
    }

    private lateinit var webView: WebView
    private lateinit var adView: AdView
    private var interstitialAd: InterstitialAd? = null
    private var isLoadingInterstitial = false
    private val handler = Handler(Looper.getMainLooper())

    // Repeating task: every 2 minutes, show the interstitial ONLY if one is
    // loaded; if none is loaded, try loading one instead. Never shows more
    // often than every 2 minutes.
    private val interstitialRunnable = object : Runnable {
        override fun run() {
            val ad = interstitialAd
            if (ad != null) {
                ad.show(this@MainActivity)
            } else if (!isLoadingInterstitial) {
                loadInterstitial()
            }
            handler.postDelayed(this, INTERSTITIAL_INTERVAL_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialise the Google Mobile Ads SDK.
        MobileAds.initialize(this) {}

        webView = findViewById(R.id.webView)
        adView = findViewById(R.id.adView)

        // WebView setup: full-screen game shell.
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                // Keep the game itself inside the WebView; open any other link
                // (for example YouTube / WhatsApp join links) in the outside app.
                return if (request.url.host == "fruitsliceearningapp.netlify.app") {
                    false
                } else {
                    try {
                        startActivity(Intent(Intent.ACTION_VIEW, request.url))
                    } catch (e: ActivityNotFoundException) {
                        // No app can handle this link; ignore it.
                    }
                    true
                }
            }
        }
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        webView.loadUrl(GAME_URL)

        // Banner ad (bottom of the screen, always visible).
        // NOTE: the banner ad unit ID lives in res/layout/activity_main.xml
        // (ads:adUnitId). Replace the TEST ID there with your real banner ID later.
        adView.loadAd(AdRequest.Builder().build())

        // Preload the first interstitial, then run the 2-minute timer.
        loadInterstitial()
        handler.postDelayed(interstitialRunnable, INTERSTITIAL_INTERVAL_MS)

        // Back button: go back in WebView history if possible, else exit the app.
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    private fun loadInterstitial() {
        // Don't start a second load while one is already running.
        if (isLoadingInterstitial) return
        isLoadingInterstitial = true
        InterstitialAd.load(
            this,
            INTERSTITIAL_AD_UNIT_ID,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    isLoadingInterstitial = false
                    interstitialAd = ad
                    interstitialAd?.fullScreenContentCallback =
                        object : FullScreenContentCallback() {
                            override fun onAdDismissedFullScreenContent() {
                                interstitialAd = null
                                loadInterstitial() // Preload the next one.
                            }

                            override fun onAdFailedToShowFullScreenContent(
                                adError: AdError
                            ) {
                                interstitialAd = null
                                loadInterstitial() // Try loading again for the next slot.
                            }
                        }
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    isLoadingInterstitial = false
                    interstitialAd = null
                    // Slow connection: try again in 30 seconds instead of
                    // waiting for the next 2-minute timer tick.
                    handler.postDelayed({ loadInterstitial() }, 30000)
                }
            }
        )
    }

    override fun onPause() {
        webView.onPause()
        adView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
        adView.resume()
    }

    override fun onDestroy() {
        handler.removeCallbacks(interstitialRunnable)
        adView.destroy()
        super.onDestroy()
    }
}

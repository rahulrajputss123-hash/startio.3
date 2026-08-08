package com.example.videoplayer

import android.app.Application
import android.util.Log
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.StartAppSDK

/**
 * Application class responsible for Start.io (StartApp) SDK global initialization.
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appId = "207768706"
        
        Log.d("StartIO", "Initializing Start.io SDK with App ID: $appId")

        // 1. Initialize Start.io SDK
        StartAppSDK.init(this, appId, true)

        // 2. Set Test Ads state
        StartAppSDK.setTestAdsEnabled(true)

        // Splash interstitial ad is enabled by default

        // Return/launch ads will auto trigger on launch/resume

        // 3. Optional: Configure User Consent for GDPR/COPPA if applicable
        // StartAppSDK.setUserConsent(this, "pas", System.currentTimeMillis(), true)
        
        Log.i("StartIO", "Start.io SDK Initialization Complete")
    }
}

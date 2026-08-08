package com.example.videoplayer

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.startapp.sdk.adsbase.Ad
import com.startapp.sdk.adsbase.StartAppAd
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener
import com.startapp.sdk.adsbase.adlisteners.AdEventListener
import com.startapp.sdk.adsbase.adlisteners.VideoListener
import com.startapp.sdk.adsbase.adlisteners.OnRewardListener

/**
 * Dedicated helper manager for handling Start.io Launch Interstitial
 * and Rewarded Video Ads cleanly across activities.
 */
class StartIoAdManager(private val activity: Activity) {

    private val startAppAd: StartAppAd = StartAppAd(activity)
    private var rewardedAd: StartAppAd? = null
    private var isRewardedAdLoaded = false

    companion object {
        private const val TAG = "StartIoAdManager"

        /**
         * Utility to trigger immediate App Launch Interstitial Ad
         */
        fun showLaunchInterstitial(activity: Activity, onClosed: (() -> Unit)? = null) {
            Log.d(TAG, "Requesting App Launch Interstitial Ad...")
            
            val launchAd = StartAppAd(activity)
            launchAd.loadAd(StartAppAd.AdMode.AUTOMATIC, object : AdEventListener {
                override fun onReceiveAd(ad: Ad) {
                    Log.d(TAG, "Launch Interstitial received! Displaying now...")
                    launchAd.showAd(object : AdDisplayListener {
                        override fun adHidden(ad: Ad) {
                            Log.d(TAG, "Launch Interstitial closed by user")
                            onClosed?.invoke()
                        }

                        override fun adDisplayed(ad: Ad) {
                            Log.d(TAG, "Launch Interstitial displayed on screen")
                        }

                        override fun adClicked(ad: Ad) {
                            Log.d(TAG, "User clicked Launch Interstitial")
                        }

                        override fun adNotDisplayed(ad: Ad) {
                            Log.w(TAG, "Launch Interstitial failed to display")
                            onClosed?.invoke()
                        }
                    })
                }

                override fun onFailedToReceiveAd(ad: Ad?) {
                    Log.e(TAG, "Failed to load launch interstitial: ${ad?.errorMessage}")
                    onClosed?.invoke()
                }
            })
        }
    }

    /**
     * Preload Rewarded Video Ad in advance for zero latency when user clicks "Unlock Video"
     */
    fun preloadRewardedAd(onLoaded: (() -> Unit)? = null, onError: ((String) -> Unit)? = null) {
        rewardedAd = StartAppAd(activity)
        isRewardedAdLoaded = false

        Log.d(TAG, "Preloading Rewarded Video Ad from Start.io...")

        rewardedAd?.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, object : AdEventListener {
            override fun onReceiveAd(ad: Ad) {
                isRewardedAdLoaded = true
                Log.d(TAG, "Rewarded Video Ad successfully loaded & ready to show!")
                onLoaded?.invoke()
            }

            override fun onFailedToReceiveAd(ad: Ad?) {
                isRewardedAdLoaded = false
                val errorMsg = ad?.errorMessage ?: "Unknown ad load failure"
                Log.e(TAG, "Failed to preload Rewarded Video Ad: $errorMsg")
                onError?.invoke(errorMsg)
            }
        })
    }

    /**
     * Show Rewarded Video Ad to user and trigger reward callback upon completion
     */
    fun showRewardedAdToUnlockVideo(onRewardGranted: () -> Unit, onAdClosed: () -> Unit) {
        if (rewardedAd != null && isRewardedAdLoaded) {
            Log.d(TAG, "Showing Rewarded Video Ad...")

            // Set VideoListener for completion events
            rewardedAd?.setVideoListener(VideoListener {
                Log.d(TAG, "Video Ad playback finished completely")
            })

            // Show ad with OnRewardListener
            rewardedAd?.showAd(object : OnRewardListener {
                override fun onRewardWithRewardListener(reward: Boolean) {
                    if (reward) {
                        Log.i(TAG, "🎉 User completed Rewarded Video! Granting video unlock reward.")
                        onRewardGranted()
                    } else {
                        Log.w(TAG, "User closed ad before completion. Reward NOT granted.")
                    }
                }
            })

            // Reset load status & preload next ad for continuous video streaming
            isRewardedAdLoaded = false
            preloadRewardedAd()

        } else {
            Log.w(TAG, "Rewarded ad was not ready. Preloading now...")
            Toast.makeText(activity, "Ad is loading, please wait a moment...", Toast.LENGTH_SHORT).show()
            preloadRewardedAd(onLoaded = {
                showRewardedAdToUnlockVideo(onRewardGranted, onAdClosed)
            })
        }
    }

    fun isRewardedReady(): Boolean = isRewardedAdLoaded
}

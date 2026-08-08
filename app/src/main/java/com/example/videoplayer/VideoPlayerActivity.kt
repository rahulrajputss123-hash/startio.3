package com.example.videoplayer

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.videoplayer.databinding.ActivityVideoPlayerBinding

/**
 * Custom Video Player Activity demonstrating locked video access
 * unlocked exclusively via Start.io Rewarded Video Ads.
 */
class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoPlayerBinding
    private lateinit var adManager: StartIoAdManager

    private var videoId: String = ""
    private var videoTitle: String = ""
    private var isVideoLocked: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate()
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoId = intent.getStringExtra("EXTRA_VIDEO_ID") ?: ""
        videoTitle = intent.getStringExtra("EXTRA_VIDEO_TITLE") ?: "Custom Video"
        isVideoLocked = intent.getBooleanExtra("EXTRA_IS_LOCKED", true)

        adManager = StartIoAdManager(this)

        setupVideoPlayerUI()

        if (isVideoLocked) {
            // Preload Rewarded Video Ad in advance
            adManager.preloadRewardedAd(
                onLoaded = {
                    binding.btnWatchAdToUnlock.text = "🎬 Watch Start.io Ad to Unlock Video"
                    binding.btnWatchAdToUnlock.isEnabled = true
                },
                onError = { error ->
                    binding.btnWatchAdToUnlock.text = "Retry Loading Start.io Ad"
                }
            )
        } else {
            unlockAndPlayVideo()
        }
    }

    private fun setupVideoPlayerUI() {
        binding.tvTitle.text = videoTitle

        if (isVideoLocked) {
            binding.layoutLockedOverlay.visibility = View.VISIBLE
            binding.layoutVideoContainer.visibility = View.GONE
            binding.tvLockedStatus.text = "🔒 Premium Video Content Locked"
            binding.btnWatchAdToUnlock.isEnabled = false
            binding.btnWatchAdToUnlock.text = "Loading Start.io Rewarded Ad..."

            binding.btnWatchAdToUnlock.setOnClickListener {
                triggerRewardedAdUnlock()
            }
        } else {
            unlockAndPlayVideo()
        }

        binding.btnBack.setOnClickListener { finish() }
    }

    private fun triggerRewardedAdUnlock() {
        adManager.showRewardedAdToUnlockVideo(
            onRewardGranted = {
                // Reward Callback: User completed full video ad
                Toast.makeText(this, "🎉 Video Unlocked! Enjoy HD Playback.", Toast.LENGTH_LONG).show()
                isVideoLocked = false
                unlockAndPlayVideo()
            },
            onAdClosed = {
                // Handle ad dismiss
            }
        )
    }

    private fun unlockAndPlayVideo() {
        binding.layoutLockedOverlay.visibility = View.GONE
        binding.layoutVideoContainer.visibility = View.VISIBLE
        binding.tvLockedStatus.text = "🔓 Stream Active (Unlocked via Start.io)"

        // Start playback on video player surface / ExoPlayer
        startVideoPlayback()
    }

    private fun startVideoPlayback() {
        Toast.makeText(this, "Playing: $videoTitle", Toast.LENGTH_SHORT).show()
        // Here you configure ExoPlayer or VideoView with video URL
    }
}

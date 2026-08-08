package com.example.videoplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.videoplayer.databinding.ActivityMainBinding

/**
 * Main Launcher Activity showing list of videos and triggering
 * Launch Interstitial Ad immediately on application start.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "App launched! Triggering Start.io launch interstitial...")

        // 1. Show Start.io Launch Interstitial Ad upon Application Launch
        StartIoAdManager.showLaunchInterstitial(this) {
            Log.d("MainActivity", "Launch ad flow finished, app ready for user interaction")
        }

        setupUI()
    }

    private fun setupUI() {
        binding.tvStatus.text = "Start.io App ID: 207768706 | Test Mode: ENABLED"

        binding.btnOpenVideo1.setOnClickListener {
            openVideoPlayer("vid-1", "4K Wildlife Documentary", false)
        }

        binding.btnOpenVideo2.setOnClickListener {
            openVideoPlayer("vid-2", "Cyberpunk Metropolis 2099", true)
        }

        binding.btnOpenVideo3.setOnClickListener {
            openVideoPlayer("vid-3", "Extreme Downhill Mountain Biking", true)
        }
    }

    private fun openVideoPlayer(videoId: String, videoTitle: String, isLocked: Boolean) {
        val intent = Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra("EXTRA_VIDEO_ID", videoId)
            putExtra("EXTRA_VIDEO_TITLE", videoTitle)
            putExtra("EXTRA_IS_LOCKED", isLocked)
        }
        startActivity(intent)
    }
}

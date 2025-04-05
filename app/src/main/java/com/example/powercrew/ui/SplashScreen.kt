package com.example.powercrew.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.ExoPlayer

import com.example.powercrew.R
import com.example.powercrew.databinding.ActivitySplashScreenBinding
import com.example.powercrew.ui.login.LoginViewModel
import com.example.powercrew.ui.main.MainActivity
import com.example.powercrew.utils.LocaleHelper
import com.example.powercrew.utils.PlayerManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var player: ExoPlayer
    private lateinit var playerManager: PlayerManager
    private val videoUri = "asset:///splash_animation.mp4"
    private val viewModel: LoginViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(application) }

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        LocaleHelper.loadLocale(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playVideo()
        setAnimation()
        navigateToMainActivity()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun setAnimation() {
        val appName: android.view.animation.Animation = AnimationUtils.loadAnimation(this,
            R.anim.app_name
        )
        val description: android.view.animation.Animation = AnimationUtils.loadAnimation(this,
            R.anim.description
        )
        binding.tvAppDesc.startAnimation(description)
        binding.tvAppName.startAnimation(appName)


    }
    private fun playVideo(){
        playerManager = PlayerManager(this)
        playerManager.initializePlayer(binding.viewer, videoUri)
    }
    private fun navigateToMainActivity() {
        lifecycleScope.launch {
            delay(5000)
            val intent = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    override fun onPause() {
        super.onPause()
        playerManager.pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        playerManager.resumePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.releasePlayer()
    }
}



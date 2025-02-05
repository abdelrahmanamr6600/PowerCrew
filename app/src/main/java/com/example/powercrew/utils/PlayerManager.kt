package com.example.powercrew.utils

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerManager(private val context: Context) {
    private var exoPlayer: ExoPlayer? = null

    fun initializePlayer(playerView: PlayerView, videoUri: String) {
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }


        playerView.player = exoPlayer
        val mediaItem = MediaItem.fromUri(videoUri)
        exoPlayer?.apply {
            setMediaItem(mediaItem)
            repeatMode = ExoPlayer.REPEAT_MODE_ONE // لجعل الفيديو يعيد التشغيل تلقائيًا
            prepare()
            playWhenReady = true
        }
    }


    fun releasePlayer() {
        exoPlayer?.release()
        exoPlayer = null
    }


    fun pausePlayer() {
        exoPlayer?.playWhenReady = false
    }


    fun resumePlayer() {
        exoPlayer?.playWhenReady = true
    }
}
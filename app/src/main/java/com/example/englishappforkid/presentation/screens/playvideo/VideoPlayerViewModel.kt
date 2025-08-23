package com.example.englishappforkid.presentation.playvideo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerViewModel : ViewModel() {
    lateinit var exoPlayer: ExoPlayer
        private set

    fun initializePlayer(context: Context) {
        if (!::exoPlayer.isInitialized) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
    }

    fun playVideo(uri: String) {
        val currentMediaId = exoPlayer.currentMediaItem?.mediaId
        if (currentMediaId != uri) {
            val mediaItem =
                MediaItem
                    .Builder()
                    .setUri(uri)
                    .setMediaId(uri)
                    .build()
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
        exoPlayer.playWhenReady = true
    }

    override fun onCleared() {
        super.onCleared()
        if (::exoPlayer.isInitialized) {
            exoPlayer.release()
        }
    }
}

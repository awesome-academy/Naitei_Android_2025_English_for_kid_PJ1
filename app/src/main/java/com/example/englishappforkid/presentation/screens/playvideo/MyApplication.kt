package com.example.englishappforkid

import android.app.Application
import androidx.media3.exoplayer.ExoPlayer

class MyApplication : Application() {
    lateinit var exoPlayer: ExoPlayer

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer.Builder(this).build()
    }

    override fun onTerminate() {
        super.onTerminate()
        if (::exoPlayer.isInitialized) {
            exoPlayer.release()
        }
    }
}

package com.example.englishappforkid.presentation.playvideo // <-- Sửa lại package cho đúng

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class VideoPlayerViewModel : ViewModel() {
    // Giữ instance của ExoPlayer trong ViewModel
    lateinit var exoPlayer: ExoPlayer
        private set

    fun initializePlayer(context: Context) {
        // Chỉ khởi tạo player nếu nó chưa tồn tại để tránh tạo lại
        if (!::exoPlayer.isInitialized) {
            exoPlayer = ExoPlayer.Builder(context).build()
        }
    }

    fun playVideo(uri: String) {
        // Kiểm tra xem video đang phát có phải là video mới không
        val currentMediaId = exoPlayer.currentMediaItem?.mediaId
        if (currentMediaId != uri) {
            val mediaItem =
                MediaItem
                    .Builder()
                    .setUri(uri)
                    .setMediaId(uri) // Dùng uri làm id để so sánh
                    .build()
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
        exoPlayer.playWhenReady = true
    }

    // onCleared được gọi tự động khi ViewModel bị hủy,
    // đây là nơi an toàn nhất để giải phóng tài nguyên của player.
    override fun onCleared() {
        super.onCleared()
        if (::exoPlayer.isInitialized) {
            exoPlayer.release()
        }
    }
}

package com.example.englishappforkid.presentation.playvideo

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.englishappforkid.MyApplication
import com.example.englishappforkid.data.model.VideoItem
import com.example.englishappforkid.util.VideoDownloadWorker
import java.io.File

class VideoPlayerViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {
    val exoPlayer: ExoPlayer by lazy {
        val app = getApplication<Application>() as MyApplication
        app.exoPlayer
    }

    var isFullscreen: Boolean
        get() = savedStateHandle["isFullscreen"] ?: false
        set(value) {
            savedStateHandle["isFullscreen"] = value
        }

    fun playVideo(
        videoItem: VideoItem,
        resume: Boolean = false,
    ) {
        val finalVideoItem = videoItem
        val mediaItem =
            if (finalVideoItem.localPath != null && File(finalVideoItem.localPath!!).exists()) {
                Log.d("VideoPlayerVM", "Đang phát từ tệp cục bộ: ${finalVideoItem.localPath}")
                MediaItem.fromUri(Uri.fromFile(File(finalVideoItem.localPath!!)))
            } else {
                Log.d("VideoPlayerVM", "Đang phát trực tuyến từ URL: ${finalVideoItem.videoId}")
                MediaItem.fromUri(finalVideoItem.videoId)
            }

        exoPlayer.setMediaItem(mediaItem, !resume)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    fun startDownload(videoItem: VideoItem) {
        val context = getApplication<Application>().applicationContext
        val workData =
            workDataOf(
                VideoDownloadWorker.KEY_VIDEO_ID to videoItem.id,
                VideoDownloadWorker.KEY_VIDEO_URL to videoItem.videoId,
            )

        val downloadRequest =
            OneTimeWorkRequestBuilder<VideoDownloadWorker>()
                .setInputData(workData)
                .build()

        WorkManager.getInstance(context).enqueue(downloadRequest)
        Toast.makeText(context, "Bắt đầu tải xuống...", Toast.LENGTH_SHORT).show()
    }

    override fun onCleared() {
        super.onCleared()
    }
}

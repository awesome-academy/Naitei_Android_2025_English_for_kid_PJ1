package com.example.englishappforkid.presentation.playvideo

import android.app.Application
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.englishappforkid.data.model.VideoItem
import com.example.englishappforkid.util.VideoDownloadWorker
import java.io.File

class VideoPlayerViewModel(
    application: Application,
) : AndroidViewModel(application) {
    lateinit var exoPlayer: ExoPlayer
        private set

    private val dbHelper = DBHelper(application)

    fun initializePlayer() {
        if (!::exoPlayer.isInitialized) {
            exoPlayer = ExoPlayer.Builder(getApplication()).build()
        }
    }

    /**
     * Phát video. Ưu tiên phát từ tệp cục bộ nếu có,
     * nếu không sẽ phát trực tuyến từ link Firebase (lưu trong videoId).
     */
    fun playVideo(videoItem: VideoItem) {
        val dbVideoItem = dbHelper.getVideoById(videoItem.id)
        val finalVideoItem = dbVideoItem ?: videoItem

        val mediaItem =
            if (finalVideoItem.localPath != null && File(finalVideoItem.localPath!!).exists()) {
                Log.d("VideoPlayerVM", "Đang phát từ tệp cục bộ: ${finalVideoItem.localPath}")
                MediaItem.fromUri(Uri.fromFile(File(finalVideoItem.localPath!!)))
            } else {
                Log.d("VideoPlayerVM", "Đang phát trực tuyến từ URL: ${finalVideoItem.videoId}")
                MediaItem.fromUri(finalVideoItem.videoId)
            }

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    fun startDownload(videoItem: VideoItem) {
        val context = getApplication<Application>().applicationContext

        val existingVideo = dbHelper.getVideoById(videoItem.id)
        if (existingVideo?.localPath != null && File(existingVideo.localPath!!).exists()) {
            Toast.makeText(context, "Video đã được tải xuống rồi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (existingVideo == null) {
            dbHelper.addVideo(videoItem.copy(localPath = null))
            Log.d("VideoPlayerVM", "Thêm metadata của video vào CSDL trước khi tải.")
        }

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
        if (::exoPlayer.isInitialized) {
            exoPlayer.release()
        }
    }
}

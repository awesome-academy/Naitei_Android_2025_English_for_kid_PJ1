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

class VideoPlayerViewModel(application: Application) : AndroidViewModel(application) {
    lateinit var exoPlayer: ExoPlayer
        private set

    // Thêm một thực thể của DBHelper vào ViewModel
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
        // Lấy thông tin mới nhất từ CSDL để kiểm tra xem có localPath không
        val dbVideoItem = dbHelper.getVideoById(videoItem.id)
        val finalVideoItem = dbVideoItem ?: videoItem // Nếu không có trong DB, dùng item gốc

        val mediaItem = if (finalVideoItem.localPath != null && File(finalVideoItem.localPath!!).exists()) {
            // --- NẾU CÓ TỆP CỤC BỘ ---
            Log.d("VideoPlayerVM", "Đang phát từ tệp cục bộ: ${finalVideoItem.localPath}")
            MediaItem.fromUri(Uri.fromFile(File(finalVideoItem.localPath!!)))
        } else {
            // --- NẾU KHÔNG CÓ TỆP CỤC BỘ ---
            // Sử dụng videoId (chính là link Firebase) để phát trực tuyến.
            Log.d("VideoPlayerVM", "Đang phát trực tuyến từ URL: ${finalVideoItem.videoId}")
            MediaItem.fromUri(finalVideoItem.videoId)
        }

        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = true
    }

    /**
     * Bắt đầu quá trình tải video xuống bằng WorkManager.
     */
    fun startDownload(videoItem: VideoItem) {
        val context = getApplication<Application>().applicationContext

        // Kiểm tra xem video đã được tải xuống trước đó chưa
        val existingVideo = dbHelper.getVideoById(videoItem.id)
        if (existingVideo?.localPath != null && File(existingVideo.localPath!!).exists()) {
            Toast.makeText(context, "Video đã được tải xuống rồi!", Toast.LENGTH_SHORT).show()
            return
        }

        // Nếu video chưa có trong CSDL, thêm nó vào với localPath là null
        if (existingVideo == null) {
            dbHelper.addVideo(videoItem.copy(localPath = null))
            Log.d("VideoPlayerVM", "Thêm metadata của video vào CSDL trước khi tải.")
        }

        // SỬA LỖI QUAN TRỌNG TẠI ĐÂY
        // Cung cấp đúng dữ liệu cho Worker
        val workData = workDataOf(
            // Dùng ID ngắn để định danh bản ghi trong CSDL
            VideoDownloadWorker.KEY_VIDEO_ID to videoItem.id,
            // Dùng videoId (URL) để làm nguồn tải về
            VideoDownloadWorker.KEY_VIDEO_URL to videoItem.videoId
        )

        // Tạo yêu cầu công việc (WorkRequest) để tải video
        val downloadRequest = OneTimeWorkRequestBuilder<VideoDownloadWorker>()
            .setInputData(workData)
            .build()

        // Giao nhiệm vụ cho WorkManager để bắt đầu tải xuống trong nền
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
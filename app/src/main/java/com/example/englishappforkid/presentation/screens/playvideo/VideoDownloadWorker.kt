package com.example.englishappforkid.util // hoặc một package phù hợp

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.englishappforkid.presentation.playvideo.DBHelper
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class VideoDownloadWorker(
    context: Context, // Sửa: Bỏ 'private val' vì đã có sẵn applicationContext
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        // Các key để truyền dữ liệu cho Worker
        const val KEY_VIDEO_ID = "key_video_id"     // Sửa: Dùng để truyền ID ngắn, định danh bản ghi trong DB
        const val KEY_VIDEO_URL = "key_video_url"    // Dùng làm nguồn tải (URL đầy đủ)
        const val KEY_FILE_PATH = "key_file_path"    // Key để trả về kết quả đường dẫn tệp
    }

    override suspend fun doWork(): Result {
        // Lấy dữ liệu được truyền vào worker
        val id = inputData.getString(KEY_VIDEO_ID) ?: return Result.failure()
        val videoUrl = inputData.getString(KEY_VIDEO_URL) ?: return Result.failure()

        // Sử dụng applicationContext để tránh memory leak
        val dbHelper = DBHelper(applicationContext)

        return try {
            // Tạo một thư mục "videos" trong bộ nhớ trong của ứng dụng nếu chưa có
            val outputDir = File(applicationContext.filesDir, "videos")
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }

            // Tạo tên tệp an toàn từ URL bằng cách sử dụng hashCode để tránh ký tự không hợp lệ.
            val fileName = "${id}_${videoUrl.hashCode()}.mp4"
            val outputFile = File(outputDir, fileName)

            // Logic tải tệp
            val url = URL(videoUrl)
            val connection = url.openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val outputStream = FileOutputStream(outputFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output) // Sao chép dữ liệu từ internet vào tệp
                }
            }

            // SỬA LỖI QUAN TRỌNG TẠI ĐÂY
            // Sau khi tải thành công, cập nhật CSDL với đường dẫn tệp cục bộ.
            // Sử dụng `id` (ID ngắn) để tìm đúng video cần cập nhật.
            dbHelper.updateVideoLocalPath(id, outputFile.absolutePath)
            Log.d("VideoDownloadWorker", "Tải thành công cho ID '$id': ${outputFile.absolutePath}")

            // Trả về đường dẫn của tệp như một phần của kết quả
            val outputData = workDataOf(KEY_FILE_PATH to outputFile.absolutePath)
            Result.success(outputData)

        } catch (e: Exception) {
            Log.e("VideoDownloadWorker", "Tải thất bại cho ID '$id'", e)
            Result.failure()
        }
    }
}
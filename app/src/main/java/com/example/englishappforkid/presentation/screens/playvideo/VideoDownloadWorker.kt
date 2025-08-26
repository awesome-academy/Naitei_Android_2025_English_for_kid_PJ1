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
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    companion object {
        const val KEY_VIDEO_ID = "key_video_id"
        const val KEY_VIDEO_URL = "key_video_url"
        const val KEY_FILE_PATH = "key_file_path"
    }

    override suspend fun doWork(): Result {
        val id = inputData.getString(KEY_VIDEO_ID) ?: return Result.failure()
        val videoUrl = inputData.getString(KEY_VIDEO_URL) ?: return Result.failure()

        val dbHelper = DBHelper(applicationContext)

        return try {
            val outputDir = File(applicationContext.filesDir, "videos")
            if (!outputDir.exists()) {
                outputDir.mkdirs()
            }

            val fileName = "${id}_${videoUrl.hashCode()}.mp4"
            val outputFile = File(outputDir, fileName)

            val url = URL(videoUrl)
            val connection = url.openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val outputStream = FileOutputStream(outputFile)

            inputStream.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }

            dbHelper.updateVideoLocalPath(id, outputFile.absolutePath)
            Log.d("VideoDownloadWorker", "Tải thành công cho ID '$id': ${outputFile.absolutePath}")

            val outputData = workDataOf(KEY_FILE_PATH to outputFile.absolutePath)
            Result.success(outputData)
        } catch (e: Exception) {
            Log.e("VideoDownloadWorker", "Tải thất bại cho ID '$id'", e)
            Result.failure()
        }
    }
}

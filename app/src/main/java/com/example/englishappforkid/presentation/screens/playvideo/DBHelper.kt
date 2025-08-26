package com.example.englishappforkid.presentation.playvideo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.englishappforkid.data.model.VideoItem

class DBHelper(
    context: Context,
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createVideoTable =
            """
            CREATE TABLE $TABLE_NAME (
                $ID_COLUMN TEXT PRIMARY KEY,
                $TITLE_COLUMN TEXT,
                $DESCRIPTION_COLUMN TEXT,
                $VIDEOID_COLUMN TEXT UNIQUE,
                $THUMBNAILS_COLUMN TEXT,
                $LOCAL_PATH_COLUMN TEXT
            )
            """.trimIndent()
        db?.execSQL(createVideoTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int,
    ) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     * Thêm một video mới vào cơ sở dữ liệu.
     */
    fun addVideo(video: VideoItem) {
        val db = this.writableDatabase
        val contentValues =
            ContentValues().apply {
                put(ID_COLUMN, video.id)
                put(TITLE_COLUMN, video.title)
                put(DESCRIPTION_COLUMN, video.description)
                put(VIDEOID_COLUMN, video.videoId) // videoId ở đây là URL
                put(THUMBNAILS_COLUMN, video.thumbnailUrl)
                put(LOCAL_PATH_COLUMN, video.localPath)
            }
        // Sử dụng insertWithOnConflict để tránh lỗi nếu cố thêm video đã tồn tại (dựa trên PRIMARY KEY)
        db.insertWithOnConflict(TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
        Log.d("DB_ADD", "Đã thêm hoặc thay thế video với ID: '${video.id}'")
    }

    /**
     * Lấy thông tin video dựa trên ID (khóa chính).
     */
    fun getVideoById(id: String): VideoItem? {
        val db = this.readableDatabase
        var videoItem: VideoItem? = null
        // Sửa: Tìm kiếm bằng ID (khóa chính) thay vì URL
        val cursor =
            db.query(
                TABLE_NAME,
                null,
                "$ID_COLUMN = ?",
                arrayOf(id),
                null,
                null,
                null,
            )

        cursor?.use {
            if (it.moveToFirst()) {
                videoItem = cursorToVideoItem(it)
            }
        }
        return videoItem
    }

    /**
     * Kiểm tra xem một video đã tồn tại trong CSDL dựa trên ID hay chưa.
     */
    fun isVideoExistsById(id: String): Boolean {
        val db = this.readableDatabase
        // Sửa: Truy vấn bằng ID
        val query = "SELECT COUNT(*) FROM $TABLE_NAME WHERE $ID_COLUMN = ?"
        val cursor = db.rawQuery(query, arrayOf(id))
        var exists = false
        cursor.use {
            if (it.moveToFirst()) {
                exists = it.getInt(0) > 0
            }
        }
        return exists
    }

    /**
     * Lấy tất cả các video đã lưu.
     */
    fun getAllVideos(): List<VideoItem> {
        val videoList = mutableListOf<VideoItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $TITLE_COLUMN ASC", null)

        cursor?.use {
            if (it.moveToFirst()) {
                do {
                    videoList.add(cursorToVideoItem(it))
                } while (it.moveToNext())
            }
        }
        return videoList
    }

    /**
     * Cập nhật đường dẫn file cục bộ cho một video dựa trên ID của nó.
     * Phương thức này rất quan trọng sau khi tải video thành công.
     */
    fun updateVideoLocalPath(id: String, localPath: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(LOCAL_PATH_COLUMN, localPath)
        }
        // Sửa: Cập nhật bằng ID
        val rowsAffected = db.update(TABLE_NAME, values, "$ID_COLUMN = ?", arrayOf(id))
        Log.d("DB_UPDATE", "Đã cập nhật $rowsAffected dòng cho ID: $id với đường dẫn: $localPath")
    }

    /**
     * Lấy đường dẫn file cục bộ của video dựa trên ID.
     */
    fun getLocalPathById(id: String): String? {
        val db = this.readableDatabase
        var localPath: String? = null
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(LOCAL_PATH_COLUMN),
            "$ID_COLUMN = ?", // Sửa: Truy vấn bằng ID
            arrayOf(id),
            null, null, null
        )
        cursor.use {
            if (it.moveToFirst()) {
                val localPathColIndex = it.getColumnIndex(LOCAL_PATH_COLUMN)
                if (localPathColIndex != -1) {
                    localPath = it.getString(localPathColIndex)
                }
            }
        }
        return if (localPath.isNullOrBlank()) null else localPath
    }

    /**
     * Xóa một video khỏi CSDL dựa trên ID.
     */
    fun removeVideoById(id: String) {
        val db = this.writableDatabase
        // Sửa: Xóa bằng ID (khóa chính)
        val rowsDeleted = db.delete(TABLE_NAME, "$ID_COLUMN = ?", arrayOf(id))
        Log.d("DB_DELETE", "Đã xóa $rowsDeleted dòng cho ID: $id")
    }

    /**
     * Hàm tiện ích để chuyển đổi một dòng từ Cursor thành đối tượng VideoItem.
     */
    private fun cursorToVideoItem(cursor: android.database.Cursor): VideoItem {
        val idCol = cursor.getColumnIndex(ID_COLUMN)
        val titleCol = cursor.getColumnIndex(TITLE_COLUMN)
        val descCol = cursor.getColumnIndex(DESCRIPTION_COLUMN)
        val videoIdCol = cursor.getColumnIndex(VIDEOID_COLUMN)
        val thumbCol = cursor.getColumnIndex(THUMBNAILS_COLUMN)
        val localPathCol = cursor.getColumnIndex(LOCAL_PATH_COLUMN)

        return VideoItem(
            id = cursor.getString(idCol),
            title = cursor.getString(titleCol),
            description = cursor.getString(descCol),
            videoId = cursor.getString(videoIdCol), // Đây là URL
            thumbnailUrl = cursor.getString(thumbCol),
            localPath = cursor.getString(localPathCol)
        )
    }

    companion object {
        // Tăng phiên bản DB để kích hoạt onUpgrade, tạo lại bảng với cấu trúc đúng
        private const val DB_VERSION = 3
        private const val DB_NAME = "english_videos.db"
        private const val TABLE_NAME = "videos"
        const val ID_COLUMN = "id"
        const val TITLE_COLUMN = "title"
        const val DESCRIPTION_COLUMN = "description"
        const val VIDEOID_COLUMN = "video_url" // Đổi tên cho rõ nghĩa
        const val THUMBNAILS_COLUMN = "thumbnail_url"
        const val LOCAL_PATH_COLUMN = "local_path"
    }
}
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
    fun getVideoById(videoId: String): VideoItem? {
        val db = this.readableDatabase
        var videoItem: VideoItem? = null
        val cursor =
            db.query(
                TABLE_NAME,
                null,
                "$ID_COLUMN = ?",
                arrayOf(videoId),
                null,
                null,
                null,
            )

        cursor?.use {
            if (it.moveToFirst()) {
                val idCol = it.getColumnIndex(ID_COLUMN)
                val titleCol = it.getColumnIndex(TITLE_COLUMN)
                val descCol = it.getColumnIndex(DESCRIPTION_COLUMN)
                val videoIdColIndex = it.getColumnIndex(VIDEOID_COLUMN)
                val thumbCol = it.getColumnIndex(THUMBNAILS_COLUMN)

                videoItem =
                    VideoItem(
                        id = it.getString(idCol),
                        title = it.getString(titleCol),
                        description = it.getString(descCol),
                        videoId = it.getString(videoIdColIndex),
                        thumbnailUrl = it.getString(thumbCol),
                    )
            }
        }
        return videoItem
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createVideoTable =
            """
            CREATE TABLE $TABLE_NAME (
                $ID_COLUMN TEXT PRIMARY KEY,
                $TITLE_COLUMN TEXT,
                $DESCRIPTION_COLUMN TEXT,
                $VIDEOID_COLUMN TEXT UNIQUE,
                $THUMBNAILS_COLUMN TEXT
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

    fun addVideo(video: VideoItem) {
        val db = this.writableDatabase
        val contentValues =
            ContentValues().apply {
                put(ID_COLUMN, video.id)
                put(TITLE_COLUMN, video.title)
                put(DESCRIPTION_COLUMN, video.description)
                put(VIDEOID_COLUMN, video.videoId)
                put(THUMBNAILS_COLUMN, video.thumbnailUrl)
            }
        db.insert(TABLE_NAME, null, contentValues)
        Log.d("DB_ADD", "Đã thêm video với ID: '${video.id}'")
    }

    fun isVideoExists(videoId: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_NAME WHERE $VIDEOID_COLUMN = ?"
        val cursor = db.rawQuery(query, arrayOf(videoId))
        var exists = false
        if (cursor.moveToFirst()) {
            exists = cursor.getInt(0) > 0
        }
        cursor.close()
        Log.d("DB_CHECK", "Đang kiểm tra VIDEO_ID: '$videoId', Kết quả: $exists")
        return exists
    }

    fun getAllVideos(): List<VideoItem> {
        val videoList = mutableListOf<VideoItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        cursor?.use {
            if (it.moveToFirst()) {
                val idCol = it.getColumnIndex(ID_COLUMN)
                val titleCol = it.getColumnIndex(TITLE_COLUMN)
                val descCol = it.getColumnIndex(DESCRIPTION_COLUMN)
                val videoIdCol = it.getColumnIndex(VIDEOID_COLUMN)
                val thumbCol = it.getColumnIndex(THUMBNAILS_COLUMN)

                do {
                    val video =
                        VideoItem(
                            id = it.getString(idCol),
                            title = it.getString(titleCol),
                            description = it.getString(descCol),
                            videoId = it.getString(videoIdCol),
                            thumbnailUrl = it.getString(thumbCol),
                        )
                    videoList.add(video)
                } while (it.moveToNext())
            }
        }
        return videoList
    }

    fun updateVideo(video: VideoItem): Int {
        val db = this.writableDatabase
        val contentValues =
            ContentValues().apply {
                put(TITLE_COLUMN, video.title)
                put(DESCRIPTION_COLUMN, video.description)
                put(VIDEOID_COLUMN, video.videoId)
                put(THUMBNAILS_COLUMN, video.thumbnailUrl)
            }
        return db.update(TABLE_NAME, contentValues, "$ID_COLUMN = ?", arrayOf(video.id))
    }

    fun removeVideo(videoId: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$ID_COLUMN = ?", arrayOf(videoId))
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "my_db"
        private const val TABLE_NAME = "video"
        const val ID_COLUMN = "id"
        const val TITLE_COLUMN = "title"
        const val DESCRIPTION_COLUMN = "description"
        const val VIDEOID_COLUMN = "video_id"
        const val THUMBNAILS_COLUMN = "thumbnail_url"
    }
}

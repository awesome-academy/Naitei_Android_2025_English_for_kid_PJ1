package com.example.englishappforkid.data.repository

import com.example.englishappforkid.data.DataSource
import com.example.englishappforkid.data.model.VideoItem

class VideoRepository {
    fun getVideos(): List<VideoItem> = DataSource.shortStories
}

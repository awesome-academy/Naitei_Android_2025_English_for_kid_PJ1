package com.example.englishappforkid.data.repository

import com.example.englishappforkid.data.DataSource
import com.example.englishappforkid.data.model.Topic

class TopicRepository {
    fun getTopics(): List<Topic> = DataSource.topics
}

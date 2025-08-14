package com.example.englishappforkid.data

import com.example.englishappforkid.data.model.Topic

/**
 * Lớp này tạo và trả về một danh sách các chủ đề học (Topic) có sẵn.
 * Đây là dữ liệu mẫu để sử dụng trong giai đoạn phát triển.
 */
object DataSource {

    val topics = listOf(
        Topic(1, "Animals"),
        Topic(2, "Colors"),
        Topic(3, "Numbers"),
        Topic(4, "Fruits"),
        Topic(5, "Family"),
        Topic(6, "Jobs"),
        Topic(7, "Sports"),
        Topic(8, "Kitchen")
    )
}

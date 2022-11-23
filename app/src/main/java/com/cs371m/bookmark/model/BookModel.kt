package com.cs371m.bookmark.model

import com.google.firebase.Timestamp

data class BookCommentModel(
    var content: String = "",
    var user: String = "",
    var timestamp: Timestamp = Timestamp.now(),
)

data class BookModel(
    var author:String = "",
    var averageRate:Int = 0,
    var totalRate:Int = 0,
    var totalRateCount:Int = 0,
    var likes: Int = 0,
    var title: String = "",
    var comment: List<BookCommentModel> = ArrayList<BookCommentModel>(),
)
package com.cs371m.bookmark.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class BookCommentModel(
    var content: String = "",
    var user: String = "",
    var timestamp: Timestamp = Timestamp.now()
)

data class BookModel(
    var author:List<String> = emptyList<String>(),
    var averageRate:Double = 0.0,
    var ISBN:String = "",
    var totalRate:Double = 0.0,
    var totalRateCount:Int = 0,
    var likes: Int = 0,
    var title: String = "",
    var comment: List<BookCommentModel> = emptyList<BookCommentModel>(),
)
package com.cs371m.bookmark.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class BookCommentModel(
    var content: String = "",
    var user: String = "",
    @ServerTimestamp val timeStamp: Timestamp? = null,
)

data class BookModel(
    var author:String = "",
    var averageRate:Float = 0.0F,
    var totalRate:Int = 0,
    var totalRateCount:Int = 0,
    var likes: Int = 0,
    var title: String = "",
    var comment: List<BookCommentModel> = ArrayList<BookCommentModel>(),
)
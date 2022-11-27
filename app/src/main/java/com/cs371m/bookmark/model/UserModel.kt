package com.cs371m.bookmark.model

import com.google.firebase.Timestamp

data class UserCommentModel(
    var ISBN:String = "",
    var content:String = "",
    var timestamp: Timestamp = Timestamp.now()
)

data class RateModel(
    var ISBN:String = "",
    var value: Double = 0.0,
)

data class UserModel (
    var displayName:String = "haha",
    var likes:List<String> = ArrayList<String>(),
    var comments: List<UserCommentModel> = ArrayList<UserCommentModel>(),
    var rate: List<RateModel> = ArrayList<RateModel>(),
)
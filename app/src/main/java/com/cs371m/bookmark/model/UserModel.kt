package com.cs371m.bookmark.model

import com.google.firebase.Timestamp

data class UserCommentModel(
    var ISBN:String = "",
    var content:String = "",
    var timestamp: Timestamp
)

//data class RateModel(
//    var ISBN:String = "",
//    var value:Int = 0,
//)

data class UserModel (
    var displayName:String = "",
    var likes:List<String>,
    var comments: List<UserCommentModel>,
    var rate: Map<String,Int>
)
package com.cs371m.bookmark.api

import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("numFound")
    val numFound: Int,
    @SerializedName("docs")
    val docs: List<Doc>,
)

data class Doc(
    @SerializedName("title")
    val title: String,
    @SerializedName("author_name")
    val author_name: List<String>,
    @SerializedName("isbn")
    val isbn: List<String>,
)
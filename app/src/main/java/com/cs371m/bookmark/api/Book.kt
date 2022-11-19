package com.cs371m.bookmark.api

import com.google.gson.annotations.SerializedName

data class Book (
    @SerializedName("bib_key")
    val bib_key: String,
    @SerializedName("info_url")
    val info_url: String,
    @SerializedName("preview_url")
    val preview_url: String,
    @SerializedName("thumbnail_url")
    val thumbnail_url: String,
    @SerializedName("details")
    val details: Details,

//    @SerializedName("title")
//    val title: String,
//    @SerializedName("authors")
//    val authors: List<Author>,
//    @SerializedName("number_of_pages")
//    val number_of_pages: Int,
//    @SerializedName("pagination")
//    val pagination: String,
//    @SerializedName("by_statement")
//    val by_statement: String,
//    identifiers is omitted.
//    classifications is omitted.
//    @SerializedName("publishers")
//    val publishers: List<Publisher>,
//    @SerializedName("publish_places")
//    val publish_places: List<PublishPlace>,
//    @SerializedName("publish_date")
//    val publish_date: String,
//    @SerializedName("subjects")
//    val subjects: List<Subject>,
//    @SerializedName("subject_places")
//    val subject_places: List<Subject>,
//    @SerializedName("subject_people")
//    val subject_people: List<Subject>,
//    @SerializedName("subject_times")
//    val subject_times: List<Subject>,
//    notes is omitted.
//    ebooks is omitted.

)

data class Details(
    @SerializedName("number_of_pages")
    val number_of_pages: Int,
    @SerializedName("table_of_contents")
    val table_of_contents: List<TableOfContent>,
    @SerializedName("title")
    val title: String,
    @SerializedName("subjects")
    val subjects: List<String>,
    @SerializedName("by_statement")
    val by_statement: String,
    @SerializedName("physical_dimensions")
    val physical_dimensions: String,
    @SerializedName("publishers")
    val publishers: List<String>,
    @SerializedName("description")
    val description: String,
    @SerializedName("physical_format")
    val physical_format: String,
    @SerializedName("authors")
    val authors: List<Author>,
    @SerializedName("publish_places")
    val publish_places: List<String>,
    @SerializedName("pagination")
    val pagination: String,
    @SerializedName("publish_date")
    val publish_date: String,
)

data class TableOfContent(
    @SerializedName("level")
    val level: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("pagenum")
    val pagenum: String,
)

data class Author(
    @SerializedName("key")
    val key: String,
    @SerializedName("name")
    val name: String,
)

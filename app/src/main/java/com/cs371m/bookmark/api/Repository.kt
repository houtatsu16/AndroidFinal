package com.cs371m.bookmark.api

class Repository(private val api: OpenLibraryApi) {
    // XXX Write me.
    suspend fun getBook(ISBN: String): Map<String, Book> {
        //SSS
        return api.getBook("ISBN:$ISBN")
        //EEE // XXX Write me
    }
}
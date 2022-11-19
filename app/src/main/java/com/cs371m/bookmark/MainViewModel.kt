package com.cs371m.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs371m.bookmark.api.Repository
import com.cs371m.bookmark.api.OpenLibraryApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl

class MainViewModel : ViewModel(){

    private var isbn = "9780980200449"

    private var searchTitle = "abc"

    private var api = OpenLibraryApi.create()
    private var repo = Repository(api)

    init {
        // XXX one-liner to kick off the app
        netRefresh()
    }

// From https://openlibrary.org/dev/docs/api/covers
//    https://covers.openlibrary.org/b/$key/$value-$size.jpg
//
    // haha
    //haha
//    Where:
//
//    key can be any one of ISBN, OCLC, LCCN, OLID and ID (case-insensitive)
//    value is the value of the chosen key
//    size can be one of S, M and L for small, medium and large respectively.
    fun coverImageUrl(ISBN: String, size: String): String {
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("covers.openlibrary.org")
            .addPathSegment("b")
            .addPathSegment("ISBN")
            .addPathSegment("$ISBN-$size.jpg")
            .build()
        return url.toString()
    }

    fun netRefresh() {
        // XXX Write me.  This is where the network request is initiated.
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO) {
            // Update LiveData from IO dispatcher, use postValue
            Log.d("netRefresh", "haha")
            val books = repo.getBook(isbn)
            books.forEach{
                Log.d("netRefresh", it.key.toString())
                Log.d("netRefresh", it.value.toString())
            }

            Log.d("netRefresh", coverImageUrl(isbn, "S"))
            val i = 3
            Log.d("netRefresh", repo.searchBookByTitle(searchTitle).toString())
            val a =1 
        }
    }

}
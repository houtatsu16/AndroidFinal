package com.cs371m.bookmark

import android.util.Log
import androidx.lifecycle.*
import com.cs371m.bookmark.api.Book
import com.cs371m.bookmark.api.Repository
import com.cs371m.bookmark.api.OpenLibraryApi
import com.cs371m.bookmark.model.BookModel
import com.cs371m.bookmark.model.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl

class MainViewModel : ViewModel(){

    private var isbn = "9780980200447"

    private var searchTitle = "abc"

    private var api = OpenLibraryApi.create()
    private var repo = Repository(api)

    private var postContent = MutableLiveData<List<Book>>()
    private var favoriteContent = MutableLiveData<List<BookModel>>().apply { value = mutableListOf() }


    private val dbHelp = DBHelper()

    private val currentBook= MutableLiveData(BookModel())

    private val currentUser= MutableLiveData(UserModel())

    private val topBooks= MutableLiveData<List<BookModel>>()

    private val randomBooks= MutableLiveData<List<BookModel>>()

    private val randomBookIndex= MutableLiveData(0)

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
//            Log.d("netRefresh", "haha")
            val books = repo.getBook(isbn)
            books.forEach{
                Log.d("netRefresh", it.key.toString())
                Log.d("netRefresh", it.value.toString())
            }

            Log.d("netRefresh", coverImageUrl(isbn, "M"))
            Log.d("netRefresh", repo.searchBookByTitle(searchTitle).toString())

            dbHelp.fetchBook("dzx8yqfsIR2aRw1PBBuZ",currentBook)
            dbHelp.fetchTopBooks(topBooks,5,"averageRate")
            dbHelp.fetchTopBooks(topBooks,5,"likes")

            dbHelp.fetchUser("uItYS3uQ3gPvDYSdxncb",currentUser)
        }
    }



    fun observeTopBooks(): MutableLiveData<List<BookModel>> {
        return topBooks
    }

    fun observeRandomBooks(): MutableLiveData<List<BookModel>> {
        return randomBooks
    }

    fun addFav(post: BookModel){
        val lst = favoriteContent.value?.toMutableList()
        lst?.let {
            it.add(post)
            favoriteContent.value = it
        }

    }

    fun isFav(post: BookModel): Boolean {
        return favoriteContent.value?.contains(post) ?: false
    }

    fun removeFav(post: BookModel) {
        val lst = favoriteContent.value?.toMutableList()
        lst?.let {
            it.remove(post)
            favoriteContent.value = it
        }
    }

    fun observeFavorite(): LiveData<List<BookModel>> {
        return favoriteContent
    }

}
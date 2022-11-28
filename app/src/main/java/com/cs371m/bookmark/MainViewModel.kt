package com.cs371m.bookmark

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cs371m.bookmark.api.Book
import com.cs371m.bookmark.api.Repository
import com.cs371m.bookmark.api.OpenLibraryApi
import com.cs371m.bookmark.api.SearchResult
import com.cs371m.bookmark.model.BookModel
import com.cs371m.bookmark.model.UserModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl

class MainViewModel : ViewModel() {

    private var isbn = "9780980200447"

    private var searchTitle = "abc"

    private var api = OpenLibraryApi.create()
    private var repo = Repository(api)

    private var searchBookResult = MutableLiveData<SearchResult>()

    private var bookDetails = MutableLiveData<Map<String, Book>>()
    private var favoriteContent =
        MutableLiveData<List<BookModel>>().apply { value = mutableListOf() }


    private val dbHelp = DBHelper()

    private val currentBook = MutableLiveData(BookModel())

    private val currentUser = MutableLiveData(UserModel())

    private val topBooks = MutableLiveData<List<BookModel>>()

    val topBooksReady = MutableLiveData(false)

    private val randomBooks = MutableLiveData<List<BookModel>>()

    private val randomBookIndex = MutableLiveData(0)

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

    fun updateTopBooks() {
        topBooksReady.postValue(false)
        dbHelp.fetchTopBooks(topBooks, 10, "averageRate")
        topBooksReady.postValue(true)
    }

    fun netRefresh() {
        // XXX Write me.  This is where the network request is initiated.
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO
        ) {
            // Update LiveData from IO dispatcher, use postValue
//            Log.d("netRefresh", "haha")
//            val books = repo.getBook(isbn)
//            books.forEach{
//                Log.d("netRefresh", it.key.toString())
//                Log.d("netRefresh", it.value.toString())
//            }
//
//            Log.d("netRefresh", coverImageUrl(isbn, "M"))
//            Log.d("netRefresh", repo.searchBookByTitle(searchTitle).toString())

//            dbHelp.fetchBook("dzx8yqfsIR2aRw1PBBuZ",currentBook)
//            dbHelp.fetchTopBooks(topBooks,5,"averageRate")
//            dbHelp.fetchTopBooks(topBooks,5,"likes")
//            dbHelp.checkUser("haha")
//            dbHelp.checkBook("isbn1235", "jun","a good book.")
//            dbHelp.fetchUser("uItYS3uQ3gPvDYSdxncb",currentUser)
//            dbHelp.updateUserDisPlayName("haha","new_name")
//            dbHelp.addUserComment("haha", "isbn1235","this is a comment", Timestamp.now())
//            dbHelp.likeBook("haha", "isbn1235")
//            dbHelp.unlikeBook("haha", "isbn1235")
//            dbHelp.updateRate("haha", "isbn1235",5.0,5.0,1,5.0)


//            dbHelp.fetchTopBooks(randomBooks, 5, "averageRate")

            updateTopBooks()

        }
    }


    fun observeTopBooks(): MutableLiveData<List<BookModel>> {
        Log.d("MainVM", "topBooks: ${topBooks.value}")
        return topBooks
    }

    fun observeRandomBooks(): MutableLiveData<List<BookModel>> {
        Log.d("MainVM", "randomBooks: ${randomBooks.value}")
        return randomBooks
    }

    fun addFav(post: BookModel) {
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

    fun getCurrentBook(isbn: String): MutableLiveData<BookModel> {
        dbHelp.fetchBook(isbn, currentBook)
        return currentBook
    }


    fun getCurrentUser(user: String): MutableLiveData<UserModel> {
        dbHelp.fetchUser(user, currentUser)
        return currentUser
    }

    fun refreshCurrentBook() {
        dbHelp.fetchBook(currentBook.value!!.ISBN, currentBook)
    }

    fun refreshCurrentUser() {
        dbHelp.fetchUser("haha", currentUser)
    }

    fun observeCurrentBook(): MutableLiveData<BookModel> {
        Log.d("MainVM", "currentBook: ${currentBook.value}")
        return currentBook
    }

    fun observeCurrentUser(): MutableLiveData<UserModel> {
        Log.d("MainVM", "currentUser: ${currentUser.value}")
        return currentUser
    }

    fun getDetails(isbn: String): MutableLiveData<Map<String, Book>> {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO
        ) {
            bookDetails.postValue(repo.getBook(isbn))
        }
        return bookDetails
    }

    // Searching
    fun searchBook(title: String) {
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO
        ) {
            searchBookResult.postValue(repo.searchBookByTitle(title))
            Log.d("searchBookResult", "value: ${searchBookResult.value}")
        }
    }

    fun observeSearchBook(): MutableLiveData<SearchResult> {
        return searchBookResult
    }


    fun checkUser(userId: String) {
        dbHelp.checkUser(userId)
    }

    fun checkBook(ISBN: String, author: String, title: String) {
        dbHelp.checkBook(ISBN, author, title)
    }

    fun addUserComment(content: String) {
//        dbHelp.addUserComment(currentUser.value!!.displayName, currentBook.value!!.ISBN,content,
//            Timestamp.now())

        dbHelp.addUserComment(
            "haha", currentBook.value!!.ISBN, content,
            Timestamp.now()
        )
    }

    fun updateUserDisplayName(displayName: String) {
        dbHelp.updateUserDisPlayName("haha", displayName)
    }

    fun getTopBookList(): MutableLiveData<List<BookModel>> {
        return topBooks
    }
}

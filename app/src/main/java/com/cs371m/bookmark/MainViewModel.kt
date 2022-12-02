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
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.HttpUrl

class MainViewModel : ViewModel() {
    private var api = OpenLibraryApi.create()
    private var repo = Repository(api)

    private var searchBookResult = MutableLiveData<SearchResult>()
    private var bookDetails = MutableLiveData<Map<String, Book>>()
    private val dbHelp = DBHelper()
    private val bookForTitle = MutableLiveData<BookModel>()
    private val currentBook = MutableLiveData<BookModel>()
    private val currentUser = MutableLiveData<UserModel>()
    private val topBooks = MutableLiveData<List<BookModel>>()
    val topBooksReady = MutableLiveData(false)

    private val ratingBook = MutableLiveData<BookModel>()
    private var userId = FirebaseAuth.getInstance().currentUser?.uid?:"haha"

    private var uid = MutableLiveData("Uninitialized")
    private var dname = MutableLiveData("")


    init {
        // XXX one-liner to kick off the app
        checkUser(userId)
        netRefresh()

    }



// From https://openlibrary.org/dev/docs/api/covers
//    https://covers.openlibrary.org/b/$key/$value-$size.jpg
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
        dbHelp.fetchTopBooks(topBooks, 100, "averageRate")
        topBooksReady.postValue(true)
    }

    fun netRefresh() {
        // XXX Write me.  This is where the network request is initiated.
        viewModelScope.launch(
            context = viewModelScope.coroutineContext
                    + Dispatchers.IO
        ) {
            updateTopBooks()
            refreshCurrentUser()
        }
    }

    fun observeTopBooks(): MutableLiveData<List<BookModel>> {
        return topBooks
    }

    fun observeRatingBook(): MutableLiveData<BookModel> {
        return ratingBook
    }

    fun setRatingBook(book: BookModel){
        ratingBook.postValue(book)
    }

    fun getCurrentBook(isbn: String): MutableLiveData<BookModel> {
        dbHelp.fetchBook(isbn, currentBook)
        return currentBook
    }

    fun getCurrentUser(user: String): MutableLiveData<UserModel> {
        dbHelp.fetchUser(user, currentUser)
        return currentUser
    }

    fun getCurrentLikes(user: String): List<String> {
        dbHelp.fetchUser(user, currentUser)
        return currentUser?.value?.likes?: mutableListOf()
    }


    fun getBookForTitle(isbn: String): String {
        dbHelp.fetchBook(isbn, bookForTitle)
        return bookForTitle.value?.title ?: "0"
    }

    fun refreshCurrentBook() {
        dbHelp.fetchBook(currentBook.value!!.ISBN, currentBook)
    }

    fun refreshCurrentUser() {
        dbHelp.fetchUser(userId, currentUser)
    }

    fun observeCurrentBook(): MutableLiveData<BookModel> {
        Log.d("observeCurrentBook", "currentBook: ${currentBook.value}")
        return currentBook
    }

    fun observeCurrentUser(): MutableLiveData<UserModel> {
        Log.d("observeCurrentUser", "currentUser: ${currentUser.value}")
        return currentUser
    }

    fun currentUser(): UserModel{
        return currentUser.value!!
    }

    fun currentBook(): BookModel{
        return currentBook.value!!
    }

    fun ratingBook(): BookModel{
        return ratingBook.value!!
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

    fun getBookUnsafe(isbn: String): Task<DocumentSnapshot> {
        return dbHelp.getBookUnsafe(isbn)
    }

    fun getDisplayNameUnsafe(userId: String): Task<DocumentSnapshot> {
        return dbHelp.getUserDisPlayNameUnsafe(userId)
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

    fun checkBook(ISBN: String, author: List<String>, title: String) {
        dbHelp.checkBook(ISBN, author, title,currentBook)
    }

    fun addUserComment(content: String) {
        dbHelp.addUserComment(
            userId, currentUser.value!!.displayName, currentBook.value!!.ISBN, content,
            Timestamp.now()
        )
    }

    fun updateUserDisplayName(displayName: String) {
        dbHelp.updateUserDisPlayName(userId, displayName)
    }

    fun getTopBookList(): MutableLiveData<List<BookModel>> {
        return topBooks
    }

    fun updateLike(ISBN: String, like:Boolean){
        if(like){
            dbHelp.likeBook(userId,ISBN)
        }else{
            dbHelp.unlikeBook(userId,ISBN)
        }
    }

    fun updateRate(book: BookModel, rate: Double){
        var tr = book.totalRate
        var cnt = book.totalRateCount
        dbHelp.updateRate(userId,book.ISBN,rate,tr+rate,cnt+1, (tr+rate) /(cnt+1))
    }

    fun formatAuthorList(list: List<String>):String{
        when (list.size) {
            0 -> {
                return "by N/A"
            }
            1 -> {
                return "by " + list[0]
            }
            2 -> {
                return "by " + list[0] +", " +  list[1]
            }
            else -> {
                return "by " + list[0] +", " +  list[1] + "..."
            }
        }
    }

    private fun userLogout() {
        uid.postValue("")
        dname.postValue("")

    }

    fun updateUser() {
        // XXX Write me. Update user data in view model
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            uid.postValue(user.uid)
            dname.postValue(user.displayName)
        }
    }

    fun observeUid() : LiveData<String> {
        return uid

    }
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        userLogout()
    }

}

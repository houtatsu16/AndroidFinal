package com.cs371m.bookmark

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cs371m.bookmark.model.BookModel
import com.cs371m.bookmark.model.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class DBHelper {
    private val allUsersCollection = "allUsers"
    private val allBooksCollection = "allBooks"

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    fun fetchBook(isbn:String, model:MutableLiveData<BookModel>){
        db.collection(allBooksCollection).document(isbn).get().addOnSuccessListener {
                document ->
            if (document != null) {
                Log.d("book1", "fetchBook data: ${document.toObject(BookModel::class.java)!!}")
                val book = document.toObject(BookModel::class.java)!!
                model.postValue(book)
            }
        }.addOnFailureListener {
            Log.d("fetchBook", "fetchBook fetch FAILED ", it)
        }
    }

    fun fetchTopBooks(models:MutableLiveData<List<BookModel>>, limit:Long, orderBy:String){
        db.collection(allBooksCollection).orderBy(orderBy).limit(limit).get().addOnSuccessListener {
                result ->
            Log.d("fetchTopBooks", "getHotBooks fetch ${result!!.documents.size}")
                models.postValue(result.documents.mapNotNull {
                    it.toObject(BookModel::class.java)
                })
        }.addOnFailureListener {
            Log.d("fetchBook", "fetchBook fetch FAILED ", it)
        }
    }

    fun fetchUser(userId:String, model:MutableLiveData<UserModel>) {
        db.collection(allUsersCollection).document(userId).get().addOnSuccessListener {
                document ->
            if (document != null) {
                Log.d("book1", "fetchBook data: ${document.toObject(UserModel::class.java)!!}")
                val book = document.toObject(UserModel::class.java)!!
                model.postValue(book)
            }
        }.addOnFailureListener {
            Log.d("fetchUser", "fetchBook fetch FAILED ", it)
        }
    }
}
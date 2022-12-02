package com.cs371m.bookmark

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cs371m.bookmark.model.*
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await


class DBHelper {
    private val allUsersCollection = "allUsers"
    private val allBooksCollection = "allBooks"

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()


    fun fetchBook(isbn:String, model:MutableLiveData<BookModel>){
        db.collection(allBooksCollection).document(isbn).get().addOnSuccessListener {
                document ->
            if (document.exists()) {
                Log.d("fetchBook", "fetchBook data: $document")
                val book = document.toObject(BookModel::class.java)!!
                model.postValue(book)
            }
        }.addOnFailureListener {
            Log.d("fetchBook", "fetchBook fetch FAILED ", it)
        }
    }

    fun getBookUnsafe(isbn: String): Task<DocumentSnapshot> {
        return db.collection(allBooksCollection).document(isbn).get()
    }

    fun getUserDisPlayNameUnsafe(userId: String): Task<DocumentSnapshot> {
        return db.collection(allUsersCollection).document(userId).get()
    }

    fun fetchTopBooks(models:MutableLiveData<List<BookModel>>, limit:Long, orderBy:String){
        db.collection(allBooksCollection).orderBy(orderBy, Query.Direction.DESCENDING).limit(limit).get().addOnSuccessListener {
                result ->
            Log.d("fetchTopBooks", "getHotBooks fetch ${result!!.documents.size}")
                models.postValue(result.documents.mapNotNull {
                    it.toObject(BookModel::class.java)
                })
        }.addOnFailureListener {
            Log.d("fetchTopBooks", "fetchBook fetch FAILED ", it)
        }
    }

    fun checkUser(userId:String){
        var doc = db.collection(allUsersCollection).document(userId)
        doc.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                db.collection(allUsersCollection).document(userId)
                    .set(UserModel(displayName = userId))
            }
        }
    }

    fun checkBook(ISBN:String, author:List<String>, title: String, model:MutableLiveData<BookModel>){
        var doc = db.collection(allBooksCollection).document(ISBN)
        doc.get().addOnSuccessListener { document ->
            if (!document.exists()) {
                val newBook = BookModel(author=author, ISBN = ISBN, title = title)
                db.collection(allBooksCollection).document(ISBN)
                    .set(newBook).addOnSuccessListener {
                        Log.d("checkBook", ISBN + " done")
                    }
                model.postValue(newBook)
            }
        }
    }


    fun fetchUser(userId:String, model:MutableLiveData<UserModel>) {
        db.collection(allUsersCollection).document(userId).get().addOnSuccessListener {
                document ->
            if (document != null) {
                Log.d("fetchUser", "fetchUser data: ${document.toObject(UserModel::class.java)!!}")
                val book = document.toObject(UserModel::class.java)!!
                model.postValue(book)
            }
        }.addOnFailureListener {
            Log.d("fetchUser", "fetchUser fetch FAILED ", it)
        }
    }

    fun updateUserDisPlayName(userId: String, displayName: String){
        var doc = db.collection(allUsersCollection).document(userId)
        Log.d("updateUserDisPlayName", displayName)
        doc.update("displayName", displayName)
    }

    fun addUserComment(userId: String, displayName:String, ISBN: String, content: String, timestamp: Timestamp){
        val bookComment = BookCommentModel(content,displayName,timestamp)
        db.collection(allBooksCollection).document(ISBN).update("comment", FieldValue.arrayUnion(bookComment))

        val userComment = UserCommentModel(ISBN, content, timestamp)
        db.collection(allUsersCollection).document(userId).update("comments", FieldValue.arrayUnion(userComment))
    }

    fun likeBook(userId: String,ISBN: String){
        db.collection(allBooksCollection).document(ISBN).update("likes", FieldValue.increment(1))
        db.collection(allUsersCollection).document(userId).update("likes", FieldValue.arrayUnion(ISBN))
    }

    fun unlikeBook(userId: String,ISBN: String){
        db.collection(allBooksCollection).document(ISBN).update("likes", FieldValue.increment(-1))
        db.collection(allUsersCollection).document(userId).update("likes", FieldValue.arrayRemove(ISBN))
    }

    fun updateRate(
        userId: String,
        ISBN: String, rate: Double, totalRate: Double, totalRateCount: Int, averageRate: Double
    ){
        db.collection(allBooksCollection).document(ISBN).update(
            mapOf(
                "totalRate" to totalRate,
                "totalRateCount" to totalRateCount,
                "averageRate" to averageRate
            )
        )
        db.collection(allUsersCollection).document(userId).update("rate", FieldValue.arrayUnion(
            RateModel(ISBN, rate)
        ))
    }


}
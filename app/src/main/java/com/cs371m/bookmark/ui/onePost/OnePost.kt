package com.cs371m.bookmark.ui.onePost

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.R
import com.cs371m.bookmark.databinding.OnePostBinding
import com.cs371m.bookmark.glide.Glide
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class OnePost : AppCompatActivity() {
    companion object {
        const val postTitle = "title"
        const val postAuthor = "author"
        const val postISBN = "isbn"
    }
    private val viewModel: MainViewModel by viewModels()
    private var liked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.show()
        val onePostBinding = OnePostBinding.inflate(layoutInflater)
        setContentView(onePostBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)


        val bundle: Bundle? = intent.extras

        val onePostISBN = bundle!!.getString(postISBN, "")
        val onePostTitle = bundle!!.getString(postTitle, "")
        val onePostAuthor = bundle!!.getStringArrayList(postAuthor)

        viewModel.checkBook(onePostISBN,onePostAuthor!!,onePostTitle)
        viewModel.getCurrentBook(onePostISBN)

        val adapter = CommentAdapter(viewModel)
        val rv = onePostBinding.recyclerView
        rv.adapter = adapter
        val manager = LinearLayoutManager(rv.context)
        rv.layoutManager = manager
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL )
        rv.addItemDecoration(dividerItemDecoration)


        viewModel.observeCurrentBook().observe(this) {
            if (it.title != "" && it.ISBN != "") {
                onePostBinding.onePostTitle.text = it.title
                onePostBinding.onePostAuthor.text = viewModel.formatAuthorList(it.author)
                onePostBinding.onePostRatingBar.rating = it.averageRate.toFloat()
                onePostBinding.onePostFavNum.text = it.likes.toString()
                onePostBinding.onePostAverageRating.text = ((it.averageRate * 100.0).roundToInt() / 100.0).toString()
                /*
                val urlString = "https://covers.openlibrary.org/b/ISBN/" + onePostISBN + "-L.jpg"
                Glide.glideFetch(urlString, onePostBinding.onePostSelfImage, 180)
                val book = viewModel.getDetails(onePostISBN).value
                */
                var url = viewModel.coverImageUrl(it.ISBN, "M")
                Glide.glideFetch(url, onePostBinding.onePostSelfImage, 120)
                val book = viewModel.getDetails(it.ISBN).value
                if (book != null) {
                    val description = book.values.toMutableList()[0].details.description
                    if (description != null) {
                        onePostBinding.onePostSelfText.text = description
                    } else {
                        onePostBinding.onePostSelfText.text = "N/A"
                    }
                } else {
                    onePostBinding.onePostSelfText.text = "N/A"
                }
                adapter.submitList(it.comment)
                adapter.notifyDataSetChanged()
            }

        }

        viewModel.observeCurrentUser().observe(this){
            Log.d("onePostOnBindViewHolder", "model: $it")
            val user = it
            liked = user.likes.contains(onePostISBN)

            val likeButton = onePostBinding.onePostLikeButton

            if(liked){
                val img: Drawable = likeButton.context.resources.getDrawable(R.drawable.ic_favorite_black_24dp)
                likeButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }else{
                val img: Drawable = likeButton.context.resources.getDrawable(R.drawable.ic_favorite_border_black_24dp)
                likeButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }

            val rateMap = user.rate.associate { Pair(it.ISBN,it.value) }

            Log.d("rateMap", rateMap.toString())
            if(rateMap.containsKey(onePostISBN)){
                onePostBinding.onePostRatingBarUser.rating = rateMap.get(onePostISBN)!!.toFloat()
            }else{
                onePostBinding.onePostRatingBarUser.rating = 0F
            }
        }


        onePostBinding.onePostCreate.setOnClickListener {
            val stringInput = onePostBinding.actionComment.text.toString()
            if (stringInput.isEmpty()) {
                Toast.makeText(this,
                    "Comment cannot be null!",
                    Toast.LENGTH_LONG).show()
            } else {
                viewModel.addUserComment(stringInput)
                onePostBinding.actionComment.text.clear()
                viewModel.refreshCurrentUser()
                viewModel.refreshCurrentBook()
            }
        }

        onePostBinding.onePostLikeButton.setOnClickListener {
            liked = !liked
            val likeButton = onePostBinding.onePostLikeButton

            if(liked){
                val img: Drawable = likeButton.context.resources.getDrawable(R.drawable.ic_favorite_black_24dp)
                likeButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }else{
                val img: Drawable = likeButton.context.resources.getDrawable(R.drawable.ic_favorite_border_black_24dp)
                likeButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null)
            }

            val user = viewModel.currentUser()
            val book = viewModel.currentBook()
            val previousLike = user.likes.contains(book.ISBN)
            if(previousLike != liked){
                viewModel.updateLike(book.ISBN, liked)
            }

            viewModel.refreshCurrentUser()
            viewModel.refreshCurrentBook()
        }

        onePostBinding.onePostRateButton.setOnClickListener {
            val book = viewModel.currentBook()
            if(onePostBinding.onePostRatingBarUser.rating != 0F){
                viewModel.updateRate(book, onePostBinding.onePostRatingBarUser.rating.toDouble())
            }

            viewModel.refreshCurrentUser()
            viewModel.refreshCurrentBook()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }
}

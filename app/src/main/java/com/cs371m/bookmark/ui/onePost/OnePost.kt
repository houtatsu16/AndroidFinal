package com.cs371m.bookmark.ui.onePost

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.databinding.OnePostBinding
import com.cs371m.bookmark.glide.Glide
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

class OnePost : AppCompatActivity() {
    companion object {
        const val postTitle = "title"
        const val postSelfText = "selfText"
        const val postImageURL = "imageURL"
        const val postAuthor = "author"
        const val postFavNum = "favNum"
        const val postStars = "stars"
        const val postISBN = "isbn"
    }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        val onePostBinding = OnePostBinding.inflate(layoutInflater)
        setContentView(onePostBinding.root)

        // supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras

        val onePostISBN = bundle!!.getString(postISBN, "0")
        // val onePostTitle = bundle!!.getString(postTitle, "a")
        Log.d("onePost", "isbn: ${onePostISBN}")
        val item = viewModel.getCurrentBook(onePostISBN)
        Log.d("onePost", "item: ${item.value}")

        val adapter = CommentAdapter(viewModel)
        val rv = onePostBinding.recyclerView
        rv.adapter = adapter
        val manager = LinearLayoutManager(rv.context)
        rv.layoutManager = manager
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL )
        rv.addItemDecoration(dividerItemDecoration)

        viewModel.observeCurrentBook().observe(this) {
            onePostBinding.onePostTitle.text = it.title
            onePostBinding.onePostAuthor.text = it.author
            onePostBinding.onePostRatingBar.rating = it.averageRate.toFloat()
            onePostBinding.onePostFavNum.text = it.likes.toString()
            Glide.glideFetch("https://covers.openlibrary.org/b/ISBN/9780980200447-M.jpg", onePostBinding.onePostSelfImage)
            // onePostBinding.onePostSelfText.text = viewModel.getDetails("9780980200447").value!!.get("detail").toString()
            adapter.submitList(it.comment)
            adapter.notifyDataSetChanged()
        }




        // onePostBinding.onePostSelfText.text = bundle!!.getString(postSelfText)
        // onePostBinding.onePostAuthor.text = bundle.getString(postAuthor)

        // val onePostImageURL = bundle!!.getString(postImageURL)

        // Gilde
        /* if (onePostImageURL != null && onePostThumbURL != null) {
            Glide.glideFetch(onePostImageURL, onePostThumbURL, onePostBinding.onePostSelfImage)
        } */

        // Write comments
        // onePostBinding.actionComment.requestFocus()

        onePostBinding.onePostCreate.setOnClickListener {
            val stringInput = onePostBinding.actionComment.text.toString()
            if (stringInput.isEmpty()) {
                Toast.makeText(this,
                    "Comment cannot be null!",
                    Toast.LENGTH_LONG).show()
            } else {

            }
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
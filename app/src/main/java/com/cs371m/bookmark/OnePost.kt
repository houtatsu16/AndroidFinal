package com.cs371m.bookmark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.cs371m.bookmark.databinding.OnePostBinding

class OnePost : AppCompatActivity() {
    companion object {
        const val postTitle = "title"
        const val postSelfText = "selfText"
        const val postImageURL = "imageURL"
        const val postAuthor = "author"
        const val postFavNum = "favNum"
        const val postStars = "stars"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onePostBinding = OnePostBinding.inflate(layoutInflater)
        setContentView(onePostBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras
        val onePostTitle = bundle!!.getString(postTitle, "a")
        onePostBinding.onePostTitle.text = onePostTitle

        onePostBinding.onePostSelfText.text = bundle!!.getString(postSelfText)
        onePostBinding.onePostAuthor.text = bundle.getString(postAuthor)

        val onePostImageURL = bundle!!.getString(postImageURL)

        // Gilde
        /* if (onePostImageURL != null && onePostThumbURL != null) {
            Glide.glideFetch(onePostImageURL, onePostThumbURL, onePostBinding.onePostSelfImage)
        } */

        // Write comments
        onePostBinding.actionComment.requestFocus()

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
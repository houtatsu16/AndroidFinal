package com.cs371m.bookmark.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.databinding.ActivityMainBinding
import com.cs371m.bookmark.databinding.ActivitySearchPostBinding
import com.cs371m.bookmark.databinding.OnePostBinding
import com.cs371m.bookmark.ui.onePost.CommentAdapter
import com.cs371m.bookmark.ui.onePost.OnePost

class searchPost : AppCompatActivity() {
    companion object {
        const val queryString = "query"
    }
    private val viewModel : MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.show()
        val activitySearchPostBinding = ActivitySearchPostBinding.inflate(layoutInflater)
        setContentView(activitySearchPostBinding.root)

        val bundle: Bundle? = intent.extras

        val queryStr = bundle!!.getString(queryString, "0")
        Log.d("searchResult", "queryString: $queryStr")

        viewModel.searchBook(queryStr)

        val adapter = searchResultAdapter(viewModel)
        val rv = activitySearchPostBinding.collectionContainer
        rv.adapter = adapter
        val manager = LinearLayoutManager(rv.context)
        rv.layoutManager = manager
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL )
        rv.addItemDecoration(dividerItemDecoration)

        viewModel.observeSearchBook().observe(this) {
            Log.d("searchPost", "nums: ${it.numFound}")
            Log.d("searchPost", "docs: ${it.docs}")
            adapter.submitList(it.docs)
            adapter.notifyDataSetChanged()
        }


    }
}
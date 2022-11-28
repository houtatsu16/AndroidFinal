package com.cs371m.bookmark.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs371m.bookmark.MainActivity2
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.R
import com.cs371m.bookmark.databinding.ActionBarBinding
import com.cs371m.bookmark.databinding.ActionBarSearchBinding
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
    private var actionBarSearchBinding: ActionBarSearchBinding? = null
    private val handler = Handler()
    private val TIME_OUT: Long = 3000




    private fun initActionBar(actionBar: ActionBar, queryStr: String) {
        // Disable the default and enable the custom
        actionBar.setDisplayShowTitleEnabled(false)
        actionBar.setDisplayShowCustomEnabled(true)
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        actionBarSearchBinding = ActionBarSearchBinding.inflate(layoutInflater)
        // Apply the custom view
        actionBar.customView = actionBarSearchBinding?.root
        actionBarSearchBinding?.searchActionBarTitle?.text = "Search result:  \"" + queryStr + "\""

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activitySearchPostBinding = ActivitySearchPostBinding.inflate(layoutInflater)
        setContentView(activitySearchPostBinding.root)

        val bundle: Bundle? = intent.extras

        val queryStr = bundle!!.getString(queryString, "0")
        Log.d("searchResult", "queryString: $queryStr")

        supportActionBar?.let{
            initActionBar(it, queryStr)
        }

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
            if (it.numFound == 0) {
                Toast.makeText(this,
                    "Not any result fit for this query!",
                    Toast.LENGTH_LONG).show()
                // finish()
                run()
            }
            adapter.submitList(it.docs)
            adapter.notifyDataSetChanged()
        }


    }

    fun run() {
        handler.postDelayed({
                            finish()
        }, TIME_OUT)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var returnVal = true

        if (item.itemId == android.R.id.home) {
            finish()
        } else {
            returnVal = super.onOptionsItemSelected(item)
        }
        return returnVal
    }
}
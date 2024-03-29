package com.cs371m.bookmark.ui.search

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs371m.bookmark.DBHelper
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.ui.onePost.OnePost
import com.cs371m.bookmark.R
import com.cs371m.bookmark.api.Doc
import com.cs371m.bookmark.api.SearchResult
import com.cs371m.bookmark.databinding.CommentPostBinding
import com.cs371m.bookmark.databinding.HotPostBinding
import com.cs371m.bookmark.databinding.OnePostBinding
import com.cs371m.bookmark.databinding.SearchPostBinding
import com.cs371m.bookmark.glide.Glide
import com.cs371m.bookmark.model.BookCommentModel
import com.cs371m.bookmark.model.BookModel
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

// https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
// Slick adapter that provides submitList, so you don't worry about how to update
// the list, you just submit a new one when you want to change the list and the
// Diff class computes the smallest set of changes that need to happen.
// NB: Both the old and new lists must both be in memory at the same time.
// So you can copy the old list, change it into a new list, then submit the new list.
//
// You can call adapterPosition to get the index of the selected item
class searchResultAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Doc, searchResultAdapter.VH>(CommentDiff()) {
    inner class VH(val searchPostBinding: SearchPostBinding) : RecyclerView.ViewHolder(searchPostBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val searchPostBinding = SearchPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(searchPostBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = currentList[holder.adapterPosition]
        if (item.isbn != null) {
            val searchPostBinding = holder.searchPostBinding
            searchPostBinding.searchPostTitle.text = item.title
            searchPostBinding.searchPostAuthor.text = viewModel.formatAuthorList(item.author_name)
            searchPostBinding.searchPostTitle.setOnClickListener {
                // dbHelp.checkBook(item.isbn[0], item.author_name[0], item.title)
                val intent = Intent(holder.itemView.context, OnePost::class.java)
                intent.apply {
                    putExtra(OnePost.postISBN, item.isbn[0])
                    putExtra(OnePost.postTitle, item.title)
                    putStringArrayListExtra(OnePost.postAuthor, ArrayList(item.author_name))
                }

                holder.itemView.context.startActivity(intent)
            }
            searchPostBinding.searchPostIsbn.text = "ISBN: " + item.isbn[0]
            val urlStr = "https://covers.openlibrary.org/b/ISBN/" + item.isbn[0] + "-L.jpg"
            Glide.glideFetch(urlStr, searchPostBinding.searchPostThumbnail, 90)
        }
    }

    class CommentDiff : DiffUtil.ItemCallback<Doc>() {
        override fun areItemsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return oldItem.title == newItem.title
        }
        override fun areContentsTheSame(oldItem: Doc, newItem: Doc): Boolean {
            return (oldItem.author_name == newItem.author_name) && (oldItem.isbn == newItem.isbn)
        }
    }
}


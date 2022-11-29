package com.cs371m.bookmark.ui.onePost

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.ui.onePost.OnePost
import com.cs371m.bookmark.R
import com.cs371m.bookmark.databinding.CommentPostBinding
import com.cs371m.bookmark.databinding.HotPostBinding
import com.cs371m.bookmark.databinding.OnePostBinding
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
class CommentAdapter(private val viewModel: MainViewModel)
    : ListAdapter<BookCommentModel, CommentAdapter.VH>(CommentDiff()) {
    inner class VH(val commentPostBinding: CommentPostBinding) : RecyclerView.ViewHolder(commentPostBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val commentPostBinding = CommentPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(commentPostBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = currentList[holder.adapterPosition]
        val commentPostBinding = holder.commentPostBinding

        commentPostBinding.commentContent.text = item.content
        commentPostBinding.commentTime.text = convertTimestamp(item.timestamp)
        commentPostBinding.commentUsername.text = item.user
    }

    fun convertTimestamp(time: com.google.firebase.Timestamp): String {
        val milliseconds = time.seconds * 1000 + time.nanoseconds / 1000000
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:M:SS")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate).toString()
        Log.d("dateTime", "time: $date")
        return date
    }

    class CommentDiff : DiffUtil.ItemCallback<BookCommentModel>() {
        override fun areItemsTheSame(oldItem: BookCommentModel, newItem: BookCommentModel): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }
        override fun areContentsTheSame(oldItem: BookCommentModel, newItem: BookCommentModel): Boolean {
            return (oldItem.content == newItem.content) && (oldItem.user == newItem.user)
        }
    }
}


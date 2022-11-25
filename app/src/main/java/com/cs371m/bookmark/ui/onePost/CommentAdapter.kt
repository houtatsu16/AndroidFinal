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
    companion object {
        const val hotTitle = "title"
        const val hotAuthor = "author"
        const val hotImageURL = "imageURL"
        const val hotStars = "stars"
        const val hotLikes = "likes"
        const val hotComment = "comment"
        const val isbn = "isbn"
    }

    inner class VH(val commentPostBinding: CommentPostBinding) : RecyclerView.ViewHolder(commentPostBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.d("commentAdapter", "doing")

        val commentPostBinding = CommentPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(commentPostBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = currentList[holder.adapterPosition]
        Log.d("commentAdapter", "item: ${item}")
        val commentPostBinding = holder.commentPostBinding

        commentPostBinding.commentContent.text = item.content
        commentPostBinding.commentTime.text = item.timestamp.toString()
        commentPostBinding.commentUsername.text = item.user

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


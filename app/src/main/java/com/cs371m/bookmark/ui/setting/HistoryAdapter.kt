package com.cs371m.bookmark.ui.setting

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
import com.cs371m.bookmark.databinding.CommentHistoryBinding
import com.cs371m.bookmark.databinding.CommentPostBinding
import com.cs371m.bookmark.databinding.HotPostBinding
import com.cs371m.bookmark.databinding.OnePostBinding
import com.cs371m.bookmark.glide.Glide
import com.cs371m.bookmark.model.BookCommentModel
import com.cs371m.bookmark.model.BookModel
import com.cs371m.bookmark.model.UserCommentModel
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
class HistoryAdapter(private val viewModel: MainViewModel)
    : ListAdapter<UserCommentModel, HistoryAdapter.VH>(CommentDiff()) {
    companion object {
        const val hotTitle = "title"
        const val hotAuthor = "author"
        const val hotImageURL = "imageURL"
        const val hotStars = "stars"
        const val hotLikes = "likes"
        const val hotComment = "comment"
        const val isbn = "isbn"
    }

    inner class VH(val historyBinding: CommentHistoryBinding) : RecyclerView.ViewHolder(historyBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.d("historyAdapter", "doing")

        val historyBinding = CommentHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(historyBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = currentList[holder.adapterPosition]
        Log.d("historyAdapter", "item: ${item}")
        val historyBinding = holder.historyBinding

        historyBinding.historyIsbn.text = item.ISBN
        historyBinding.historyContent.text = item.content
        historyBinding.historyTime.text = convertTimestamp(item.timestamp)
        historyBinding.historyTitle.text = viewModel.getBookForTitle(item.ISBN).value!!.title
    }

    fun convertTimestamp(time: com.google.firebase.Timestamp): String {
        val milliseconds = time.seconds * 1000 + time.nanoseconds / 1000000
        val sdf = SimpleDateFormat("MM/dd/yyyy HH:M:SS")
        val netDate = Date(milliseconds)
        val date = sdf.format(netDate).toString()
        Log.d("dateTime", "time: $date")
        return date
    }

    class CommentDiff : DiffUtil.ItemCallback<UserCommentModel>() {
        override fun areItemsTheSame(oldItem: UserCommentModel, newItem: UserCommentModel): Boolean {
            return oldItem.timestamp == newItem.timestamp
        }
        override fun areContentsTheSame(oldItem: UserCommentModel, newItem: UserCommentModel): Boolean {
            return (oldItem.content == newItem.content) && (oldItem.ISBN == newItem.ISBN)
        }
    }
}


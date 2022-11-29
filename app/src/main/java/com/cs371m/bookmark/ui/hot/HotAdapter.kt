package com.cs371m.bookmark.ui.hot

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
import com.cs371m.bookmark.databinding.HotPostBinding
import com.cs371m.bookmark.glide.Glide
import com.cs371m.bookmark.model.BookModel
import com.cs371m.bookmark.ui.onePost.OnePost.Companion.postAuthor
import com.cs371m.bookmark.ui.onePost.OnePost.Companion.postISBN
import com.cs371m.bookmark.ui.onePost.OnePost.Companion.postTitle
import kotlin.math.roundToInt

// https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
// Slick adapter that provides submitList, so you don't worry about how to update
// the list, you just submit a new one when you want to change the list and the
// Diff class computes the smallest set of changes that need to happen.
// NB: Both the old and new lists must both be in memory at the same time.
// So you can copy the old list, change it into a new list, then submit the new list.
//
// You can call adapterPosition to get the index of the selected item
class HotAdapter(private val viewModel: MainViewModel)
    : ListAdapter<BookModel, HotAdapter.VH>(BookDiff()) {

    inner class VH(val hotPostBinding: HotPostBinding) : RecyclerView.ViewHolder(hotPostBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val hotPostBinding = HotPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(hotPostBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = currentList[holder.adapterPosition]
        val hotPostBinding = holder.hotPostBinding
        hotPostBinding.hotPostTitle.text = item.title
        hotPostBinding.hotPostAuthor.text = viewModel.formatAuthorList(item.author)
        hotPostBinding.hotPostRatingBar.rating = item.averageRate.toFloat()
        hotPostBinding.hotPostFavNum.text = item.likes.toString()
        hotPostBinding.hotPostAverageRating.text = ((item.averageRate * 100.0).roundToInt() / 100.0).toString()

        var url = viewModel.coverImageUrl(item.ISBN, "M")

        Glide.glideFetch(url, hotPostBinding.hotPostThumbnail,90)
        hotPostBinding.hotPostRowFav.setImageResource(R.drawable.ic_favorite_black_24dp)

        hotPostBinding.hotPostTitle.setOnClickListener {
            val intent = Intent(holder.itemView.context, OnePost::class.java)
            intent.apply {
                putExtra(postISBN, item.ISBN)
                putExtra(postTitle, item.title)
                putStringArrayListExtra(postAuthor, ArrayList(item.author))
            }

            holder.itemView.context.startActivity(intent)
        }
    }

    class BookDiff : DiffUtil.ItemCallback<BookModel>() {
        override fun areItemsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
            return oldItem.title == newItem.title
        }
        override fun areContentsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
            return oldItem.averageRate == newItem.averageRate && oldItem.likes == newItem.likes
        }
    }
}


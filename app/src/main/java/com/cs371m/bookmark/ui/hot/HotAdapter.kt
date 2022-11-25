package com.cs371m.bookmark.ui.hot

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs371m.bookmark.MainActivity2
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.OnePost
import com.cs371m.bookmark.R
import com.cs371m.bookmark.api.Book
import com.cs371m.bookmark.databinding.FragmentHotBinding
import com.cs371m.bookmark.databinding.HotPostBinding
import com.cs371m.bookmark.glide.Glide
import com.cs371m.bookmark.model.BookModel

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
    companion object {
        const val hotTitle = "title"
        const val hotAuthor = "author"
        const val hotImageURL = "imageURL"
        const val hotStars = "stars"
        const val hotLikes = "likes"
        const val isbn = "isbn"
    }

    inner class VH(val hotPostBinding: HotPostBinding) : RecyclerView.ViewHolder(hotPostBinding.root) {
        init {
            hotPostBinding.hotPostRowFav.setOnClickListener {
                val position = adapterPosition
                if (viewModel.isFav(getItem(position))) {
                    viewModel.removeFav(getItem(position))
                } else {
                    viewModel.addFav(getItem(position))
                }
                notifyItemChanged(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.d("hotAdapter", "doing")

        val hotPostBinding = HotPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(hotPostBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Log.d("hotAdapter", "done")
        val item = currentList[holder.adapterPosition]
        Log.d("hotAdapter", "item: ${item}")
        val hotPostBinding = holder.hotPostBinding

        hotPostBinding.hotPostTitle.text = item.title
        hotPostBinding.hotPostAuthor.text = item.author
        hotPostBinding.hotPostRatingBar.rating = item.averageRate.toFloat()
        hotPostBinding.hotPostFavNum.text = item.likes.toString()

        Glide.glideFetch("https://covers.openlibrary.org/b/ISBN/9780980200447-M.jpg", hotPostBinding.hotPostThumbnail)

        /* if(viewModel.isFav(item)) {
            rowPostBinding.rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            rowPostBinding.rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        } */

        if(viewModel.isFav(item)) {
            hotPostBinding.hotPostRowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            hotPostBinding.hotPostRowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }

        Log.d("onBind", "onbindview.......")

        hotPostBinding.hotPostTitle.setOnClickListener {
            val intent = Intent(holder.itemView.context, OnePost::class.java)
            intent.apply {
                // putExtra(isbn, item.)
            }

            holder.itemView.context.startActivity(intent)
        }
    }

    class BookDiff : DiffUtil.ItemCallback<BookModel>() {
        override fun areItemsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
            return oldItem.title == newItem.title
        }
        override fun areContentsTheSame(oldItem: BookModel, newItem: BookModel): Boolean {
            return oldItem.author == newItem.author
        }
    }
}


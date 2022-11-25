package com.cs371m.bookmark.ui.collection

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.ui.onePost.OnePost
import com.cs371m.bookmark.databinding.CollectionPostBinding
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
class CollectionAdapter(private val viewModel: MainViewModel)
    : ListAdapter<BookModel, CollectionAdapter.VH>(BookDiff()) {
    companion object {
        const val hotTitle = "title"
        const val hotAuthor = "author"
        const val hotImageURL = "imageURL"
        const val hotStars = "stars"
        const val hotLikes = "likes"
        const val isbn = "isbn"
    }

    inner class VH(val collectionPostBinding: CollectionPostBinding) : RecyclerView.ViewHolder(collectionPostBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        Log.d("collectionAdapter", "doing")

        val collectionPostBinding = CollectionPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(collectionPostBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        Log.d("collectionAdapter", "done")
        val item = currentList[holder.adapterPosition]
        Log.d("collectionAdapter", "item: ${item}")
        val collectionPostBinding = holder.collectionPostBinding

        collectionPostBinding.favTitle.text = item.title

        if (item.title != null) {
            Glide.glideFetch("https://covers.openlibrary.org/b/ISBN/9780980200447-M.jpg", collectionPostBinding.favImg)
        }

        /* if(viewModel.isFav(item)) {
            rowPostBinding.rowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            rowPostBinding.rowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        } */


        Log.d("onBind", "onbindview.......")

        collectionPostBinding.favTitle.setOnClickListener {
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


package com.cs371m.bookmark.ui.collection

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.ui.onePost.OnePost
import com.cs371m.bookmark.databinding.CollectionPostBinding
import com.cs371m.bookmark.glide.Glide
import com.cs371m.bookmark.model.BookModel
import com.cs371m.bookmark.ui.hot.HotAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter
// Slick adapter that provides submitList, so you don't worry about how to update
// the list, you just submit a new one when you want to change the list and the
// Diff class computes the smallest set of changes that need to happen.
// NB: Both the old and new lists must both be in memory at the same time.
// So you can copy the old list, change it into a new list, then submit the new list.
//
// You can call adapterPosition to get the index of the selected item
class CollectionAdapter(private val viewModel: MainViewModel)
    : ListAdapter<String, CollectionAdapter.VH>(BookDiff()) {
    inner class VH(val collectionPostBinding: CollectionPostBinding) : RecyclerView.ViewHolder(collectionPostBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val collectionPostBinding = CollectionPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(collectionPostBinding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = currentList[holder.adapterPosition]
        val collectionPostBinding = holder.collectionPostBinding

        var url = viewModel.coverImageUrl(item, "L")

        Glide.glideFetch(url, collectionPostBinding.favImg, 190)
        viewModel.getBookUnsafe(item).addOnSuccessListener { it ->
            if (it.exists()) {
                val book = it.toObject(BookModel::class.java)!!
                collectionPostBinding.favTitle.text = book.title

                collectionPostBinding.favTitle.setOnClickListener {
                    val intent = Intent(holder.itemView.context, OnePost::class.java)
                    intent.apply {
                        putExtra(OnePost.postISBN, book.ISBN)
                        putExtra(OnePost.postTitle, book.title)
                        putStringArrayListExtra(OnePost.postAuthor, ArrayList(book.author))
                    }

                    holder.itemView.context.startActivity(intent)
                }
            }
        }

        collectionPostBinding.favButton.setOnClickListener {
            viewModel.updateLike(item, false)
            viewModel.refreshCurrentUser()
        }
    }

    class BookDiff : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}


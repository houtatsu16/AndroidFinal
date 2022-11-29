package com.cs371m.bookmark.ui.rate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.R
import com.cs371m.bookmark.databinding.FragmentRateBinding
import com.cs371m.bookmark.glide.Glide
import com.cs371m.bookmark.ui.onePost.OnePost
import java.util.*

class RateFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRateBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.show()
        _binding = FragmentRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    private var liked = false
    private var rand = Random()

    private fun nextRatingBook(){
        val bookList = viewModel.getTopBookList().value
        val bookListSize = bookList!!.size
        var randomNum = rand.nextInt(bookListSize)
        viewModel.setRatingBook(bookList!![randomNum])
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nextRatingBook()

        viewModel.observeRatingBook().observe(viewLifecycleOwner) {
            binding.rateTitle.text = it.title
            binding.rateAuthor.text= viewModel.formatAuthorList(it.author)
            var url = viewModel.coverImageUrl(it.ISBN, "M")
            Glide.glideFetchbyHeight(url, binding.rateSelfImage, 250)
            var currentISBN = it.ISBN
            val user = viewModel.currentUser()
            liked = user.likes.contains(currentISBN)
            if(liked){
                binding.rateRowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
            }else{
                binding.rateRowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }

            val rateMap = user.rate.associate { Pair(it.ISBN,it.value) }
            if(rateMap.containsKey(currentISBN)){
                binding.rateRatingBar.rating = rateMap.get(currentISBN)!!.toFloat()
            }else{
                binding.rateRatingBar.rating = 0F
            }
        }

        binding.skipButton.setOnClickListener {
            nextRatingBook()
        }

        binding.rateRowFav.setOnClickListener {
            liked = !liked
            if(liked){
                binding.rateRowFav.setImageResource(R.drawable.ic_favorite_black_24dp)
            }else{
                binding.rateRowFav.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
        }


        binding.confirmButton.setOnClickListener {
            val user = viewModel.currentUser()
            val book = viewModel.ratingBook()
            val previousLike = user.likes.contains(book.ISBN)
            if(previousLike != liked){
                viewModel.updateLike(book.ISBN, liked)
            }

            if(binding.rateRatingBar.rating!=0F){
                viewModel.updateRate(book, binding.rateRatingBar.rating.toDouble())
            }

            nextRatingBook()
            viewModel.refreshCurrentUser()
        }

        binding.rateTitle.setOnClickListener {
            val book = viewModel.ratingBook()
            val intent = Intent(requireActivity(), OnePost::class.java)
            intent.apply {
                putExtra(OnePost.postISBN, book.ISBN)
                putExtra(OnePost.postTitle, book.title)
                putStringArrayListExtra(OnePost.postAuthor, ArrayList(book.author))
            }

            requireActivity().startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
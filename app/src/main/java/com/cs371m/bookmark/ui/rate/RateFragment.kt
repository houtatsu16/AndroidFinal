package com.cs371m.bookmark.ui.rate

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cs371m.bookmark.DBHelper
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.databinding.FragmentRateBinding
import com.cs371m.bookmark.glide.Glide
import com.cs371m.bookmark.ui.hot.HotAdapter

class RateFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentRateBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")


        val item = viewModel.getCurrentBook("0446568813")
        val user = viewModel.getCurrentUser("haha")

        binding.rateTitle.text = item.value!!.title
        binding.rateAuthor.text = item.value!!.author

        binding.rateRatingBar.rating = item.value!!.averageRate.toFloat()
        var url = viewModel.coverImageUrl(item.value!!.ISBN, "M")
        Glide.glideFetch(url, binding.rateSelfImage, 250)

        viewModel.observeCurrentBook().observe(viewLifecycleOwner) {
            Log.d("rate", "title: ${it.title}")
            binding.rateTitle.text = it.title
            binding.rateAuthor.text = it.author
            // binding.rateRatingBar.rating = it.averageRate.toFloat()
            var url = viewModel.coverImageUrl(it.ISBN, "M")
            Glide.glideFetch(url, binding.rateSelfImage, 250)

        }

        binding.leftButton.setOnClickListener {
            val isbn = "8441516480"
            val item = viewModel.getCurrentBook(isbn)

            binding.rateTitle.text = item.value!!.title
            binding.rateAuthor.text = item.value!!.author
            // binding.rateRatingBar.rating = item.value!!.averageRate.toFloat()
            var url = viewModel.coverImageUrl(item.value!!.ISBN, "M")
            Glide.glideFetch(url, binding.rateSelfImage, 250)
        }

        binding.rightButton.setOnClickListener {
            val isbn = "0446568813"
            val item = viewModel.getCurrentBook(isbn)
            binding.rateTitle.text = item.value!!.title
            binding.rateAuthor.text = item.value!!.author
            // binding.rateRatingBar.rating = item.value!!.averageRate.toFloat()
            var url = viewModel.coverImageUrl(item.value!!.ISBN, "M")
            Glide.glideFetch(url, binding.rateSelfImage, 250)
        }

        binding.submitButton.setOnClickListener {

        }




        /* // Swipe
        val adapter = RateAdapter(viewModel)
        val rv = binding.recyclerView
        rv.adapter = adapter
        val manager = LinearLayoutManager(rv.context)
        rv.layoutManager = manager
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL )
        rv.addItemDecoration(dividerItemDecoration)

        val itemTouchHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                /* val fromPos = viewHolder.adapterPosition
                val toPos = target.adapterPosition
                adapter.notifyItemMoved(fromPos, toPos)
                return true */
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                adapter.notifyItemMoved(viewHolder.adapterPosition, direction)
            }
        })

        itemTouchHelper.attachToRecyclerView(rv)


        viewModel.observeRandomBooks().observe(viewLifecycleOwner) {
            Log.d("rateFragment", "did!, ${it}")
            binding.RateSwipeRefreshLayout.apply {
                isRefreshing = false
                setOnRefreshListener {
                    viewModel.netRefresh()
                }
            }
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
        */



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.cs371m.bookmark.ui.hot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.api.Book
import com.cs371m.bookmark.databinding.FragmentHotBinding

class HotFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentHotBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.show()
        _binding = FragmentHotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = HotAdapter(viewModel)
        val rv = binding.recyclerView
        rv.adapter = adapter
        val manager =LinearLayoutManager(rv.context)
        rv.layoutManager = manager
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL )
        rv.addItemDecoration(dividerItemDecoration)

        viewModel.observeTopBooks().observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }

        binding.HotSwipeRefreshLayout.apply {
            setOnRefreshListener {
                viewModel.updateTopBooks()
            }
        }

        viewModel.topBooksReady.observe(viewLifecycleOwner){
            binding.HotSwipeRefreshLayout.isRefreshing = !it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
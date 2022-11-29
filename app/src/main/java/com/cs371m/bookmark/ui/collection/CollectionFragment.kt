package com.cs371m.bookmark.ui.collection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.databinding.FragmentCollectionBinding
import okhttp3.internal.notifyAll

class CollectionFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentCollectionBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.show()

        _binding = FragmentCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = CollectionAdapter(viewModel)
        val rv = binding.collectionContainer
        // rv.setHasFixedSize(true)

        rv.adapter = adapter
        val manager = GridLayoutManager(rv.context, 2)
        // val manager = LinearLayoutManager(rv.context)
        rv.layoutManager = manager
        // binding.swipeRefreshLayout.isEnabled = false
        viewModel.refreshCurrentUser()
        viewModel.observeCurrentUser().observe(viewLifecycleOwner) {
            adapter.submitList(it.likes)
            adapter.notifyDataSetChanged()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
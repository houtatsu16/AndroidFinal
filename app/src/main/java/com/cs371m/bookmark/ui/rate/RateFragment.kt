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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.databinding.FragmentHotBinding
import com.cs371m.bookmark.databinding.FragmentRateBinding
import com.cs371m.bookmark.ui.hot.HotAdapter

class RateFragment : Fragment() {
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
        Log.d(javaClass.simpleName, "onViewCreated")

        val adapter = RateAdapter(viewModel)
        val rv = binding.recyclerView
        rv.adapter = adapter
        val manager = LinearLayoutManager(rv.context)
        rv.layoutManager = manager
        val dividerItemDecoration = DividerItemDecoration(
            rv.context, LinearLayoutManager.VERTICAL )
        rv.addItemDecoration(dividerItemDecoration)




        viewModel.observeRandomBooks().observe(viewLifecycleOwner) {
            Log.d("rateFragment", "did!, ${it}")
            binding.swipeRefreshLayout.apply {
                isRefreshing = false
                setOnRefreshListener {
                    viewModel.netRefresh()
                }
            }
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
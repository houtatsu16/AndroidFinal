package com.cs371m.bookmark.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.databinding.FragmentSettingsBinding
import com.cs371m.bookmark.ui.onePost.CommentAdapter

class SettingsFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "onViewCreated")
        viewModel.getCurrentUser("haha")
        // viewModel.getBookForTitle("9074336639")

        viewModel.observeCurrentUser().observe(viewLifecycleOwner) {
            binding.Username.text = it.displayName
        }

        binding.SettingHistory.setOnClickListener {
            val adapter = HistoryAdapter(viewModel)
            val rv = binding.recyclerView
            rv.adapter = adapter
            val manager = LinearLayoutManager(rv.context)
            rv.layoutManager = manager
            val dividerItemDecoration = DividerItemDecoration(
                rv.context, LinearLayoutManager.VERTICAL )
            rv.addItemDecoration(dividerItemDecoration)

            viewModel.observeCurrentUser().observe(viewLifecycleOwner) {
                binding.historyTitle.visibility = View.VISIBLE
                binding.historyClose.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.submitList(it.comments)
                adapter.notifyDataSetChanged()
            }
        }

        binding.historyClose.setOnClickListener {
            binding.historyTitle.visibility = View.INVISIBLE
            binding.historyClose.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.INVISIBLE
        }

        binding.SettingChangeName.setOnClickListener {
            binding.settingTypeName.visibility = View.VISIBLE
            binding.settingOk.visibility = View.VISIBLE
            binding.settingCancel.visibility = View.VISIBLE
        }

        binding.Username.setOnClickListener {
            binding.settingTypeName.visibility = View.VISIBLE
            binding.settingOk.visibility = View.VISIBLE
            binding.settingCancel.visibility = View.VISIBLE
        }

        binding.settingCancel.setOnClickListener {
            binding.settingTypeName.visibility = View.INVISIBLE
            binding.settingOk.visibility = View.INVISIBLE
            binding.settingCancel.visibility = View.INVISIBLE
        }

        binding.settingOk.setOnClickListener {
            val stringInput = binding.settingTypeName.text.toString()
            if (stringInput.isEmpty()) {
                Toast.makeText(requireActivity(),
                    "Name cannot be null!",
                    Toast.LENGTH_LONG).show()
            } else {
                viewModel.updateUserDisplayName(stringInput)
                binding.settingTypeName.text.clear()
                viewModel.refreshCurrentUser()
                binding.settingTypeName.visibility = View.INVISIBLE
                binding.settingOk.visibility = View.INVISIBLE
                binding.settingCancel.visibility = View.INVISIBLE

                // TODO: hide keyboard
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
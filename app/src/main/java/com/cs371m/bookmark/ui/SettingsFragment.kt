package com.cs371m.bookmark.ui

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
import com.cs371m.bookmark.MainActivity2
import com.cs371m.bookmark.MainViewModel
import com.cs371m.bookmark.databinding.FragmentSettingsBinding

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
        viewModel.observeCurrentUser().observe(viewLifecycleOwner) {
            binding.Username.text = it.displayName
        }

        binding.SettingHistory.setOnClickListener {

        }

        binding.SettingChangeName.setOnClickListener {

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
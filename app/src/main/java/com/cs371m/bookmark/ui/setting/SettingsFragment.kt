package com.cs371m.bookmark.ui.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
            binding.settingTypeName.visibility = View.VISIBLE
            binding.settingOk.visibility = View.VISIBLE

        }

        binding.settingOk.setOnClickListener {
            val stringInput = binding.settingTypeName.text.toString()
            if (stringInput.isEmpty()) {
                Toast.makeText(requireActivity(),
                    "Comment cannot be null!",
                    Toast.LENGTH_LONG).show()
            } else {
                viewModel.updateUserDisplayName(stringInput)
                binding.settingTypeName.text.clear()
                viewModel.refreshCurrentUser()
            }
            binding.settingTypeName.visibility = View.INVISIBLE
            binding.settingOk.visibility = View.INVISIBLE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
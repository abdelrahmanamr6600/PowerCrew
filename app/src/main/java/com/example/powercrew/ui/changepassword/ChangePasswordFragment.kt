package com.example.powercrew.ui.changepassword

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentChangePasswordBinding
import com.example.powercrew.databinding.FragmentChangePhoneBinding
import com.example.powercrew.ui.profile.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment




class ChangePasswordFragment :BottomSheetDialogFragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }

    private var currentUserPassword : String? =null

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        observeUserData()
        setListeners()
        return binding.root
    }

    private fun observeUserData(){

        viewModel.userLiveData.observe(viewLifecycleOwner){ result ->
            result.data?.let { userData ->
                currentUserPassword = userData.password
            }
        }
    }


    private fun setListeners(){
        binding.changePasswordBtn.setOnClickListener {
            viewModel.updatePassword(binding.newPasswordEt.text.toString(),binding.oldPasswordEt.text.toString())
        }
    }

    companion object {
    val TAG = "ChangePassword"

    }
}
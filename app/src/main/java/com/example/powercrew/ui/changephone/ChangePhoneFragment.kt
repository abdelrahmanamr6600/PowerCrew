package com.example.powercrew.ui.changephone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentChangePhoneBinding
import com.example.powercrew.ui.profile.ProfileViewModel
import com.example.powercrew.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.subodh.customtoast.CustomToast


class ChangePhoneFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentChangePhoneBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }

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
    ): View? {
       _binding = FragmentChangePhoneBinding.inflate(layoutInflater)
        setListeners()
        observeChangePhoneState()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val rootView = requireView()
            val insets = ViewCompat.getRootWindowInsets(rootView)
            val isKeyboardVisible = insets?.isVisible(WindowInsetsCompat.Type.ime()) == true

            if (isKeyboardVisible) {
                dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }
        }

    }

    private fun setListeners(){
        binding.changePhoneBtn.setOnClickListener {
            viewModel.updatePhone(binding.newPhoneEt.text.toString().trim())
        }
    }

    private fun observeChangePhoneState(){
        viewModel.updatePhoneStatus.observe(viewLifecycleOwner){result->
            when(result){
                is Resource.Error -> phoneChangedFailed(result.message.toString())
                is Resource.Loading -> showProgressBar()
                is Resource.Success -> successfullyPhoneChanged()
            }
        }
    }
    private fun successfullyPhoneChanged(){
        CustomToast.makeText(requireContext(),
            getString(R.string.phone_was_changed_successfully),Toast.LENGTH_LONG,CustomToast.SUCCESS).show()
        hideProgressBar()
        dismiss()
    }
    private fun phoneChangedFailed(error:String){
        CustomToast.makeText(requireContext(),
            error,Toast.LENGTH_LONG,CustomToast.ERROR).show()
        hideProgressBar()
    }

    private fun showProgressBar(){
        binding.progressBar.visibility = View.VISIBLE
        binding.changePhoneBtn.visibility = View.INVISIBLE
    }
    private fun hideProgressBar(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.changePhoneBtn.visibility = View.VISIBLE
    }

    companion object {
   var TAG = "ChangePassword"

    }
}
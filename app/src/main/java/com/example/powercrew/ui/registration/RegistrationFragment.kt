package com.example.powercrew.ui.registration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentRegistrationBinding
import com.example.powercrew.domain.models.User
import com.example.powercrew.utils.Resource
import com.subodh.customtoast.CustomToast


class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RegistrationViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        onRegistrationBtnClick()
        observeValidationResult()
        observeRegistrationResult()

        return binding.root
    }

    private fun observeRegistrationResult() {
        viewModel.registrationResult.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    showProgressBar()
                }

                is Resource.Success -> {
                    CustomToast.makeText(context, requireContext().getString(R.string.registration_succefully), Toast.LENGTH_SHORT,CustomToast.SUCCESS).show();
                    navigateToNextActivity()
                }

                is Resource.Error -> {
                    showDataValidationErrors(resource.message)
                    hideProgressBar()

                }
            }
        }
    }


    private fun observeValidationResult(){
        viewModel.validationResulResult.observe(viewLifecycleOwner){ resource->
            when(resource){
                is Resource.Loading -> {showProgressBar()}
                is Resource.Success -> {signup(resource.data!!)}
                is Resource.Error -> {
                    showDataValidationErrors(resource.message)
                    hideProgressBar()
                }
            }


        }
    }
    private fun showDataValidationErrors(message: String?){
        when(message){
            requireContext().getString(R.string.error_email) -> {
                binding.emailEt.requestFocus()
                binding.emailEt.error = requireContext().getString(R.string.error_email)
            }
            "Phone number does not exist in the database." ->{
                binding.phoneEt.requestFocus()
                binding.phoneEt.error =  requireContext().getString(R.string.phone_dosnt_exist)
            }
            requireContext().getString(R.string.error_empty_name_en) ->{
                binding.fullNameEt.requestFocus()
                binding.fullNameEt.error =  requireContext().getString(R.string.error_empty_name_en)
            }
            requireContext().getString(R.string.error_short_password_en) ->{
                binding.passwordEt.requestFocus()
                binding.passwordEt.error =  requireContext().getString(R.string.error_short_password_en)
            }
            requireContext().getString(R.string.please_enter_your_password)->{
                binding.passwordEt.requestFocus()
                binding.passwordEt.error =  requireContext().getString(R.string.please_enter_your_password)
            }
        }
    }

    private fun hideProgressBar(){
        binding.loadingProgress.visibility = View.INVISIBLE
        binding.registerBtn.visibility = View.VISIBLE
    }
    private fun showProgressBar() {
        binding.loadingProgress.visibility = View.VISIBLE
        binding.registerBtn.visibility = View.INVISIBLE
    }
    private fun navigateToNextActivity(){
        findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
    }


    private fun onRegistrationBtnClick() {
        binding.registerBtn.setOnClickListener {

            val user = User(
                "",
                binding.fullNameEt.text.toString(),
                binding.emailEt.text.toString(),
                binding.phoneEt.text.toString(),
                binding.passwordEt.text.toString(),
                null

            )
          viewModel.validationUserData(user)
        }
    }

    private fun signup(user:User){
        viewModel.registerUser(user)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
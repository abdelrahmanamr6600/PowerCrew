package com.example.powercrew.ui.login
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentLoginBinding
import com.example.powercrew.utils.Resource
import com.subodh.customtoast.CustomToast



class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels { ViewModelProvider.AndroidViewModelFactory(requireActivity().application) }
    private lateinit var userEmail:String
    private lateinit var userPassword:String
    override fun onStart() {
        super.onStart()
        checkLoginState()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        onLoginClickListener()
        observeValidationState()
        goToRegistrationFragment()
        observeLoginState()

        return binding.root
    }
     private fun hideProgressLoading(){
         binding.loadingProgress.visibility = View.INVISIBLE
         binding.loginBtn.visibility =View.VISIBLE

     }
    private fun showProgressBar(){
        binding.loadingProgress.visibility=View.VISIBLE
        binding.loginBtn.visibility =View.INVISIBLE
    }

    private fun checkLoginState(){
        viewModel.isLoggedIn.observe(viewLifecycleOwner){state ->
            if (state)
                checkCityState()
        }
    }


    private fun observeLoginState(){
        viewModel.loginResult.observe(viewLifecycleOwner){resources ->
            when(resources){
                is Resource.Error -> showLoginErrors(resources.message!!)
                is Resource.Loading -> {showProgressBar()}
                is Resource.Success -> checkCityState()
            }

        }
    }

    private fun observeValidationState(){
        viewModel.validationResult.observe(viewLifecycleOwner){resources->
            when(resources){
                is Resource.Error -> showDataValidationErrors(resources.message)
                is Resource.Loading -> {showProgressBar()}
                is Resource.Success -> login(userEmail, userPassword)
            }
        }
        hideProgressLoading()
    }
    private fun showLoginErrors(message:String){
        CustomToast.makeText(context, message, Toast.LENGTH_SHORT,CustomToast.ERROR).show()
        hideProgressLoading()

    }


    private fun showDataValidationErrors(message: String?){
        when(message){
            requireContext().getString(R.string.error_email) -> {
                binding.emailEt.requestFocus()
                binding.emailEt.error = requireContext().getString(R.string.error_email)
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
        hideProgressLoading()

    }
    private fun onLoginClickListener(){
        binding.loginBtn.setOnClickListener {
            userEmail = binding.emailEt.text!!.trim().toString()
            userPassword = binding.passwordEt.text!!.trim().toString()
            viewModel.validationLoginData(userEmail,userPassword)

        }
    }
    private fun login(email: String,password: String){
        viewModel.login(email,password)
    }

    private fun goToRegistrationFragment(){
        binding.registerBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun goToHomeScreen(){
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.loginFragment, true)
            .build()
        findNavController().navigate(R.id.mainFragment, null, navOptions)
    }

    private fun goToChooseCitFragment(){
        val navOptions = NavOptions.Builder()
            .setPopUpTo(R.id.loginFragment, true)
            .build()
        findNavController().navigate(R.id.chooseCityFragment, null, navOptions)
    }

    private fun checkCityState() {
   viewModel.cityState.observe(viewLifecycleOwner){state->
       if (state){
           goToHomeScreen()
       }else{
           goToChooseCitFragment()
       }
   }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
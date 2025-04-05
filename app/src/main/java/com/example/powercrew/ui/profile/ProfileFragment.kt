package com.example.powercrew.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentProfileBinding
import com.example.powercrew.databinding.FragmentRegistrationBinding
import com.example.powercrew.domain.models.User
import com.example.powercrew.ui.changepassword.ChangePasswordFragment
import com.example.powercrew.ui.changephone.ChangePhoneFragment
import com.example.powercrew.ui.cities.ChooseCityFragment
import com.example.powercrew.ui.cities.ChoseCityViewModel
import com.example.powercrew.ui.engineers.EngineersFragment
import com.example.powercrew.utils.Resource
import com.subodh.customtoast.CustomToast
import kotlin.math.log


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var originalName: String
    private lateinit var originalEmail: String
    private lateinit var userPassword:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        )[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        observeOnUserInfo()
        setListeners()
        observeUpdateNameStatue()
        observeEmailUpdateStatue()

        return binding.root
    }

    private fun observeOnUserInfo(){
        profileViewModel.userLiveData.observe(viewLifecycleOwner){user->
            when(user){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> user.data?.let { failUserInfo(it) }
            }
        }
    }

    private fun failUserInfo(user:User){
        originalName = user.fullName
        originalEmail = user.email
        userPassword = user.password
        binding.firstName.setText(user.fullName)
        binding.emailEt.setText(user.email)

    }

    @OptIn(UnstableApi::class)
    private fun setListeners() {
        binding.emailEt.setOnClickListener {
            binding.emailEt.isEnabled = true
            binding.emailEt.isFocusable = true
            binding.emailEt.isFocusableInTouchMode = true
            binding.emailEt.requestFocus()
            Log.d("state", binding.emailEt.isEnabled.toString())
        }
        binding.firstName.setOnClickListener {
            binding.firstName.isEnabled = true
            binding.firstName.isFocusable = true
            binding.firstName.isFocusableInTouchMode = true
            binding.firstName.requestFocus()
        }
        binding.btnEdit.setOnClickListener {
            if (binding.firstName.requestFocus()) {
                updateName(binding.firstName.text.toString())
            } else if (binding.emailEt.requestFocus()) {
                updateEmail(binding.emailEt.text.toString())
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please Change Mail Or Name First",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.tvChangeCity.setOnClickListener {

            val changeCityFragment = ChooseCityFragment().apply {
                arguments = Bundle().apply {
                    putInt("ChangeCity", 1)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, changeCityFragment)
                .addToBackStack(null)
                .commit()

        }

        binding.btnChangePhone.setOnClickListener {
            ChangePhoneFragment()
                .show(childFragmentManager, ChangePhoneFragment.TAG)
        }
        binding.tvChangePassword.setOnClickListener {
            ChangePasswordFragment().show(childFragmentManager,ChangePasswordFragment.TAG)
        }
    }

   private fun updateName(newName:String){
  profileViewModel.updateFullName(newName,originalName)

   }
    private fun updateEmail(newEmail:String){
        profileViewModel.updateEmail(newEmail,originalEmail,userPassword)
    }

    private fun observeUpdateNameStatue(){
        profileViewModel.updateFullNameStatus.observe(viewLifecycleOwner){result->
            when(result){
                is Resource.Error -> {Toast.makeText(requireContext(),"new NAME == oLDnAME",Toast.LENGTH_SHORT).show()}
                is Resource.Loading -> {}
                is Resource.Success -> {}
            }
        }
    }


    private fun observeEmailUpdateStatue(){
        profileViewModel.updateEmailStatus.observe(viewLifecycleOwner){result->
            when(result){
                is Resource.Error -> {
                    CustomToast.makeText(requireContext(), result.message, Toast.LENGTH_SHORT,
                        CustomToast.ERROR).show()
                }
                is Resource.Loading -> {}
                is Resource.Success -> { CustomToast.makeText(requireContext(), "Please Check Your Mail Inbox And Confirm Your New Mail", Toast.LENGTH_SHORT,
                    CustomToast.SUCCESS).show()
                }
            }
        }
    }

}
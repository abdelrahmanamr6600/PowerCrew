package com.example.powercrew.ui.spalsh

import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController

import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentSplashBinding
import com.example.powercrew.ui.login.LoginViewModel
import com.example.powercrew.utils.LocaleHelper
import com.example.powercrew.utils.PlayerManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class SplashFragment : Fragment() {
    private lateinit var playerManager: PlayerManager
    private val videoUri = "asset:///splash_animation.mp4"
    private val viewModel: LoginViewModel by viewModels {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
    }

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(LocaleHelper.setAppLocale(context))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        requestCallAndNotificationPermissions(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playVideo()
        setAnimation()
        lifecycleScope.launch {
            delay(5000)
            checkLoginState()
        }
    }

    private fun setAnimation() {
        binding.tvAppDesc.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.description))
        binding.tvAppName.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.app_name))
    }

    private fun playVideo() {
        playerManager = PlayerManager(requireContext())
        playerManager.initializePlayer(binding.viewer, videoUri)
    }

    private fun checkLoginState() {
        viewModel.isLoggedIn.observe(viewLifecycleOwner) { state ->
            if (state) checkCityState() else navigateTo(R.id.loginFragment)
        }
    }

    private fun checkCityState() {
        viewModel.cityState.observe(viewLifecycleOwner) { state ->
            navigateTo(if (state) R.id.mainFragment else R.id.chooseCityFragment)
        }
    }

    private fun navigateTo(destination: Int) {
        val navOptions = NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
        findNavController().navigate(destination, null, navOptions)
    }

    override fun onPause() {
        super.onPause()
        playerManager.pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        playerManager.resumePlayer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        playerManager.releasePlayer()
    }
    private val REQUEST_PERMISSIONS_CODE = 100

    fun requestCallAndNotificationPermissions(activity: Activity) {
        val permissions = mutableListOf<String>()
        permissions.add(Manifest.permission.CALL_PHONE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        ActivityCompat.requestPermissions(
            activity,
            permissions.toTypedArray(),
            REQUEST_PERMISSIONS_CODE
        )
    }




}

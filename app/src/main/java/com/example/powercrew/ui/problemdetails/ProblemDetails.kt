package com.example.powercrew.ui.problemdetails

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.powercrew.databinding.FragmentProblemDetailsBinding
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.utils.getParcelableCompat


class ProblemDetails : Fragment() {
    private var _binding: FragmentProblemDetailsBinding? = null
    private val binding get() = _binding!!
    private var problem: Problem? = null
    private var engineer: Engineer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            problem = it.getParcelableCompat("problem")
            engineer = it.getParcelableCompat("engineer")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentProblemDetailsBinding.inflate(layoutInflater)
        setData()

        return binding.root
    }


    private fun setData() {
        problem?.apply {
            binding.dateTv.text = createdAt
            binding.titleTv.text = title
            binding.addressTv.text = address
            binding.problemDescriptionTv.setText(description)
            binding.statueTv.text = state
        }

        engineer?.apply {
            binding.nameTv.text = fullName
            binding.stateTv.text = if (state) "Online" else "Offline"
        }

        binding.callIconIv.setOnClickListener { checkPermissionAndCall() }
    }

    private fun makeCall() {
        val phoneNumber = engineer?.phone
        if (!phoneNumber.isNullOrEmpty()) {
            val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
            startActivity(callIntent)
        } else {
            Toast.makeText(requireContext(), "Phone number is not available", Toast.LENGTH_SHORT).show()
        }
    }
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                makeCall()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    private fun checkPermissionAndCall() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            makeCall()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    }




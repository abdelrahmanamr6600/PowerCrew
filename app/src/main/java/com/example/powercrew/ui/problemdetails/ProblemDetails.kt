package com.example.powercrew.ui.problemdetails

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.powercrew.R
import com.example.powercrew.databinding.FragmentProblemDetailsBinding
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.utils.getParcelableCompat
import com.google.auth.oauth2.GoogleCredentials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class ProblemDetails : Fragment() {
    private var _binding: FragmentProblemDetailsBinding? = null
    private val binding get() = _binding!!
    private var problem: Problem? = null
    private var engineer: Engineer? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                makeCall()
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

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
        _binding = FragmentProblemDetailsBinding.inflate(inflater, container, false)
        setData()
        setupSwipedBtn()
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

    private fun checkPermissionAndCall() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            makeCall()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }
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

    private fun setupSwipedBtn() {
        problem?.let { currentProblem ->
            if (currentProblem.state == "InProgress") {
                binding.turnOnSwipeabBtn.uncheckedText = getString(R.string.the_power_has_been_disconnected)
                binding.turnOnSwipeabBtn.onSwipedListener = {
                    sendNotificationToUser(
                        engineer?.token.orEmpty(),
                        currentProblem.title,
                        "تم فصل التيار الكهربائي")

                    loadingState()

                }
            } else if (currentProblem.state == "Completed")
                {
                binding.turnOnSwipeabBtn.uncheckedText = getString(R.string.the_power_has_been_connected)
                binding.turnOnSwipeabBtn.onSwipedListener = {
                    sendNotificationToUser(
                        engineer?.token.orEmpty(),
                        currentProblem.title,
                        "تم إعادة التيار الكهربائي"
                    )
                    loadingState()
                }
            }
        }
    }

    private fun sendNotificationToUser(userToken: String, problemTitle: String, body: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = getFirebaseAccessToken()
            if (accessToken == null) {
                Log.e("FCM", "Failed to get access token")
                return@launch
            }

            val json = JSONObject().apply {
                put("message", JSONObject().apply {
                    put("token", userToken)
                    put("data", JSONObject().apply {
                        put("title", problemTitle)
                        put("body", body)
                    })
                })
            }

            val url = "https://fcm.googleapis.com/v1/projects/powercrew-d5bf7/messages:send"
            val requestBody = json.toString().toRequestBody("application/json".toMediaTypeOrNull())

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $accessToken")
                .addHeader("Content-Type", "application/json")
                .post(requestBody)
                .build()

            OkHttpClient().newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("FCM", "Failed to send notification: ${e.message}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        Log.d("FCM", "Notification sent successfully")
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.turnOnSwipeabBtn.visibility = View.INVISIBLE
                    } else {
                        Log.e("FCM", "Failed: ${response.code} -> ${response.body?.string()}")
                    }
                }
            })
        }
    }

    private suspend fun getFirebaseAccessToken(): String? {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream = requireContext().assets.open("service-account.json")
                val credentials = GoogleCredentials
                    .fromStream(inputStream)
                    .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))
                credentials.refreshIfExpired()
                credentials.accessToken.tokenValue
            } catch (e: Exception) {
                Log.e("FCM", "Token error: ${e.localizedMessage}")
                null
            }
        }
    }
    private fun loadingState(){
        binding.progressBar.visibility = View.VISIBLE
        binding.turnOnSwipeabBtn.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

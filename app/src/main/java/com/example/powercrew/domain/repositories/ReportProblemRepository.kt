package com.example.powercrew.domain.repositories

import DataStoreManager
import android.content.Context
import android.util.Log
import com.example.powercrew.R
import com.example.powercrew.domain.models.Cities
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.domain.models.User
import com.example.powercrew.utils.FirestoreCollections
import com.example.powercrew.utils.Resource
import com.google.auth.oauth2.GoogleCredentials
import okhttp3.OkHttpClient

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

import java.io.InputStream

class ReportProblemRepository(var context:Context) {
    private  val dataStore =  DataStoreManager(context)
    private var fireBaseFireStore = FirebaseFirestore.getInstance()

    suspend fun reportProblem(problem:Problem,token:String):Resource<Unit>{
        try {
            val documentRef =  fireBaseFireStore.collection(FirestoreCollections.PROBLEMS).document()
            documentRef.set(problem).await()

            val problemId = documentRef.id
            sendNotificationToUser(context , token,problem.title,problemId)
            return Resource.Success(Unit)
        }catch (e:FirebaseFirestoreException){
            return Resource.Error(e.message.toString())
        }
    }


    suspend fun getUserData(): Resource<User> {
        return try {
            val user = dataStore.userData.firstOrNull()
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("User data not found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to get user data: ${e.localizedMessage}")
        }
    }

    suspend fun getFirebaseAccessToken(context: Context): String? {
        return withContext(Dispatchers.IO) {
            try {
                context.assets.open("service-account.json").use { stream ->
                    val credentials = GoogleCredentials
                        .fromStream(stream)
                        .createScoped(listOf("https://www.googleapis.com/auth/firebase.messaging"))

                    val token = credentials.refreshAccessToken().tokenValue
                    Log.d("FIREBASE_TOKEN", "Access Token = $token")
                    token
                }
            } catch (e: Exception) {
                Log.e("FIREBASE_TOKEN", "Error: ${e.message}")
                e.printStackTrace()
                null
            }
        }
    }


    private suspend fun sendNotificationToUser(context: Context, userToken: String,problemTitle:String,problemId: String) {
        val accessToken = getFirebaseAccessToken(context) ?: run {
            Log.e("FCM", "Failed to get access token")
            return
        }


         val json = JSONObject().apply {
             put("message", JSONObject().apply {
                 put("token", userToken)
                 put("data", JSONObject().apply {
                     put("problemId", problemId)
                     put("title", context.getString(R.string.new_problem))
                     put("body", problemTitle)
                 })
             })
         }

        val url = "https://fcm.googleapis.com/v1/projects/powercrew-d5bf7/messages:send"
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), json.toString())

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
                } else {
                    Log.e("FCM", "Failed: ${response.code} -> ${response.body.string()}")
                }
            }
        })
    }


}
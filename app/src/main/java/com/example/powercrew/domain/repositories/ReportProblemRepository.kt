package com.example.powercrew.domain.repositories

import DataStoreManager
import android.content.Context
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.domain.models.User
import com.example.powercrew.utils.FirestoreCollections
import com.example.powercrew.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.tasks.await

class ReportProblemRepository(context:Context) {
    private  val dataStore =  DataStoreManager(context)
    private var fireBaseFireStore = FirebaseFirestore.getInstance()

    suspend fun reportProblem(problem:Problem):Resource<Unit>{
        try {
            fireBaseFireStore.collection(FirestoreCollections.PROBLEMS).document().set(problem).await()
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

}
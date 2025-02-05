package com.example.powercrew.domain.repositories

import DataStoreManager
import android.content.Context
import com.example.powercrew.domain.models.User
import com.example.powercrew.utils.FirestoreCollections
import com.example.powercrew.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await

class LoginRepository(private val firebaseAuth: FirebaseAuth, private val fireStore: FirebaseFirestore ,private val context: Context) {
   private  val dataStore =  DataStoreManager(context)

    suspend fun login(email:String , password:String):Resource<User>{
         return try {
           val result =  firebaseAuth.signInWithEmailAndPassword(email,password).await()
            val userQuery = fireStore.collection(FirestoreCollections.USER).document(result.user!!.uid).get().await()
            val user = userQuery.toObject(User::class.java)
              saveUserDataToDataStore(user!!)
             editLoginState(true)
            Resource.Success(user)
         } catch (e: FirebaseAuthException) {
             when(e.errorCode){
                 "ERROR_INVALID_EMAIL" -> Resource.Error("Invalid email format.")
                 "ERROR_WRONG_PASSWORD" -> Resource.Error("Incorrect password.")
                 "ERROR_USER_NOT_FOUND" -> Resource.Error("User not found. Please register first.")
                 "ERROR_USER_DISABLED" -> Resource.Error("This account has been disabled.")
                 "ERROR_TOO_MANY_REQUESTS" -> Resource.Error("Too many failed login attempts. Try again later.")
                 else -> {Resource.Error("Authentication error: ${e.message}")}
             }
        }
        catch (e:FirebaseFirestoreException){
            Resource.Error(e.message!!)
        }
}

    private suspend fun saveUserDataToDataStore(user: User){
        dataStore.saveUserData(user)
    }

    private suspend fun editLoginState(state: Boolean){
        dataStore.editLoginState(state)

    }
   suspend fun getLoginState():Boolean{
        return dataStore.loginState.first()
    }

    }
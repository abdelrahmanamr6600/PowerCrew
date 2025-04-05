package com.example.powercrew.domain.repositories

import DataStoreManager
import android.content.Context
import android.util.Log
import com.example.powercrew.domain.models.User
import com.example.powercrew.utils.FirebaseAuthInstance
import com.example.powercrew.utils.FirestoreCollections
import com.example.powercrew.utils.FirestoreFieldNames
import com.example.powercrew.utils.FirestoreInstance
import com.example.powercrew.utils.Resource
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileRepository(context:Context) {
    private var dataStoreManager = DataStoreManager(context)
    private var fireStoreInstance = FirestoreInstance.db.collection(FirestoreCollections.USER)
    private var firebaseAuthInstance = FirebaseAuthInstance.auth
    init {
        setupIdTokenChangedListener()
    }


    private fun setupIdTokenChangedListener() {
        firebaseAuthInstance.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            user?.let {
                val newEmail = user.email
                if (newEmail != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        updateEmailInFireStore(newEmail)
                    }
                }
            }
        }
    }
    suspend fun getUserData(): Resource<User> {
        return try {
            val user = dataStoreManager.userData.firstOrNull()
            if (user != null) {
                Resource.Success(user)
            } else {
                Resource.Error("User data not found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to get user data: ${e.localizedMessage}")
        }
    }


     suspend fun updateFullNameInFirestore(newName: String): Resource<Unit> {
        return try {
            firebaseAuthInstance.uid?.let { userId ->
                fireStoreInstance.document(userId)
                    .update("fullName", newName)
                    .await()
                dataStoreManager.updateFullName(newName)
            }
            Log.d("Firestore", "User name updated in Firestore")
            Resource.Success(Unit)

        } catch (e: FirebaseFirestoreException) {
            Log.e("Firestore", "Error updating name", e)
            Resource.Error(e.message ?: "Unknown error")
        }
    }

    private suspend fun authenticateUser(password: String): Resource<Unit> {
        val user =
            firebaseAuthInstance.currentUser ?: return Resource.Error("المستخدم غير مسجل دخول")

        return try {

            val credential = EmailAuthProvider.getCredential(user.email!!, password)

            user.reauthenticate(credential).await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "فشل إعادة المصادقة")
        }
    }
    suspend fun updateEmailWithReAuth(newEmail: String, password: String): Resource<Unit> {
        val authResult = authenticateUser(password)
        if (authResult is Resource.Error) {
            return authResult
        }

        return try {
            val user = firebaseAuthInstance.currentUser
                ?: return Resource.Error("المستخدم غير مسجل دخول")
            user.sendEmailVerification().await()
            user.updateEmail(newEmail).await()


            user.sendEmailVerification().await()


            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "فشل تحديث البريد الإلكتروني")
        }
    }



    private suspend fun updateEmailInFireStore(newEmail: String): Resource<Unit> {
        return try {
            firebaseAuthInstance.uid?.let { userId ->
                fireStoreInstance.document(userId)
                    .update(FirestoreFieldNames.EMAIL, newEmail)
                    .await()
                dataStoreManager.updateEmail(newEmail)
            }
            Log.d("Firestore", "User Email updated in Firestore")
            Resource.Success(Unit)
        } catch (e: FirebaseFirestoreException) {
            Log.e("Firestore", "Error updating Email", e)
            Resource.Error(e.message ?: "Unknown error")
        }
    }
    suspend fun updatePhone(newPhone:String):Resource<Unit>{
        return try {
            firebaseAuthInstance.uid?.let { userId ->
                fireStoreInstance.document(userId)
                    .update(FirestoreFieldNames.PHONE_NUMBER, newPhone)
                    .await()
                dataStoreManager.updatePhone(newPhone)
            }
            Log.d("Firestore", "User Phone updated in Firestore")
            Resource.Success(Unit)

        } catch (e: FirebaseFirestoreException) {
            Log.e("Firestore", "Error updating Phone", e)
            Resource.Error(e.message ?: "Unknown error")
        }
    }

//    suspend fun updatePasswordInFireAuth(oldPassword: String, newPassword: String): Resource<Unit> {
//        return try {
//            val user = firebaseAuthInstance.currentUser ?: return Resource.Error("User not logged in")
//
//            // إعادة المصادقة قبل تحديث كلمة المرور
//            val email = user.email ?: return Resource.Error("User email not found")
//            val credential = EmailAuthProvider.getCredential(email, oldPassword)
//
//            user.reauthenticate(credential).await()  // إعادة المصادقة
//            user.updatePassword(newPassword).await() // تحديث كلمة المرور
//
//            Log.d("FirebaseAuth", "Password updated successfully!")
//            Resource.Success(Unit)
//        } catch (e: FirebaseAuthException) {
//            Log.e("FirebaseAuth", "Error updating password", e)
//            Resource.Error(e.message ?: "Unknown Error")
//        } catch (e: Exception) {
//            Log.e("FirebaseAuth", "Unexpected error", e)
//            Resource.Error("Unexpected error occurred")
//        }
//    }

    suspend fun updatePasswordInFireAuth(newPassword: String,currentUserPassword:String): Resource<Unit> {
        return try {
            val user = firebaseAuthInstance.currentUser
            if (user != null) {
                // إعادة التوثيق باستخدام البريد وكلمة المرور القديمة
                val credential = EmailAuthProvider.getCredential(user.email!!, currentUserPassword)
                user.reauthenticate(credential).await() // مهم جدًا!

                // بعد إعادة التوثيق، قم بتحديث كلمة المرور
                user.updatePassword(newPassword).await()



                Resource.Success(Unit)
            } else {
                Resource.Error("User is not logged in")
            }
        } catch (e: FirebaseAuthException) {
            Resource.Error(e.message ?: "Unknown Error")
        }
    }


}
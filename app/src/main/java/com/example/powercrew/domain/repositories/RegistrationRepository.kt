package com.example.powercrew.domain.repositories

import com.example.powercrew.domain.models.User
import com.example.powercrew.utils.FirestoreCollections
import com.example.powercrew.utils.FirestoreFieldNames
import com.example.powercrew.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
class RegistrationRepository(private val firebaseAuth: FirebaseAuth,private val fireStore:FirebaseFirestore) {

    suspend fun registerAndStoreUser(user: User): Resource<Unit> {
        return try {
            val phoneCheckQuery = fireStore.collection(FirestoreCollections.PHONE)
                .whereEqualTo(FirestoreFieldNames.PHONE_NUMBER, user.phone)
                .get()
                .await()

            if (phoneCheckQuery.documents.isEmpty()) {
                return Resource.Error("Phone number does not exist in the database.")
            }


            val result = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()


            val firebaseUser = result.user!!


            val userDocument = fireStore.collection(FirestoreCollections.USER).document(firebaseUser.uid)
              user.userId = firebaseUser.uid

            userDocument.set(user).await()

            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Registration and storage failed: ${e.localizedMessage}")  // في حالة الفشل
        }
    }
}
    

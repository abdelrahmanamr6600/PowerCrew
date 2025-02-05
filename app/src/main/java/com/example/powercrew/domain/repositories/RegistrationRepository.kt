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
            // تحقق إذا كان رقم الهاتف موجودًا في المجموعة
            val phoneCheckQuery = fireStore.collection(FirestoreCollections.PHONE)
                .whereEqualTo(FirestoreFieldNames.PHONE_NUMBER, user.phone)
                .get()
                .await()

            // إذا لم يتم العثور على الرقم
            if (phoneCheckQuery.documents.isEmpty()) {
                return Resource.Error("Phone number does not exist in the database.")
            }

            // تسجيل المستخدم الجديد باستخدام Firebase Auth
            val result = firebaseAuth.createUserWithEmailAndPassword(user.email, user.password).await()

            // الحصول على المستخدم الجديد من Firebase Auth
            val firebaseUser = result.user!!

            // إنشاء بيانات المستخدم الجديدة التي ستُخزن في Firestore
            val userDocument = fireStore.collection(FirestoreCollections.USER).document(firebaseUser.uid)
              user.userId = firebaseUser.uid
            // حفظ البيانات في Firestore
            userDocument.set(user).await()

            Resource.Success(Unit)  // النجاح
        } catch (e: Exception) {
            Resource.Error("Registration and storage failed: ${e.localizedMessage}")  // في حالة الفشل
        }
    }
}
    

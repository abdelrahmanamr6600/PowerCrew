package com.example.powercrew.domain.repositories

import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.Problem
import com.example.powercrew.utils.FirestoreCollections
import com.example.powercrew.utils.FirestoreFieldNames
import com.example.powercrew.utils.FirestoreInstance
import com.example.powercrew.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class ProblemsRepository {
    private val firebaseInstance = FirestoreInstance.db


     fun fetchProblems(statue:String):Flow<Resource<List<Problem>>> = flow {
         val problemsCollection = firebaseInstance.collection(FirestoreCollections.PROBLEMS)
             .whereEqualTo(FirestoreFieldNames.STATE,statue)
             .whereEqualTo(FirestoreFieldNames.CREWID, FirebaseAuth.getInstance().currentUser!!.uid)
        emitAll(callbackFlow {
            val listener = problemsCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error("Failed to load Problems: ${error.message}"))
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val problems = snapshot.toObjects(Problem::class.java)
                    trySend(Resource.Success(problems))
                }
            }
            awaitClose { listener.remove() }
        })
    }

    fun getEngineer(engineerId: String): Flow<Resource<Engineer>> = flow {
        emit(Resource.Loading())

        val engineerDocument = FirestoreInstance.db
            .collection(FirestoreCollections.ENGINEERS)
            .document(engineerId)

        emitAll(callbackFlow {
            val listener = engineerDocument.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error("Failed to load engineer: ${error.message}"))
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val engineer = snapshot.toObject(Engineer::class.java)
                    trySend(Resource.Success(engineer!!))
                } else {
                    trySend(Resource.Error("Engineer not found"))
                }
            }

            awaitClose { listener.remove() }
        })
    }

}
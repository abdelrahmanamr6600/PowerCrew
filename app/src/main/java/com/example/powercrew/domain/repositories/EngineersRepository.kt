package com.example.powercrew.domain.repositories

import DataStoreManager
import android.content.Context
import com.example.powercrew.domain.models.Engineer
import com.example.powercrew.domain.models.User
import com.example.powercrew.utils.FirestoreCollections
import com.example.powercrew.utils.FirestoreFieldNames
import com.example.powercrew.utils.FirestoreInstance
import com.example.powercrew.utils.Resource
import com.google.firebase.firestore.FieldPath
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class EngineersRepository(context: Context) {
    private val dataStore = DataStoreManager(context)

    fun getEngineersList(): Flow<Resource<List<Engineer>>> = flow {
        emit(Resource.Loading())

        val cityItem = dataStore.cityItemData.firstOrNull()
        val cityId = cityItem?.id ?: return@flow emit(Resource.Error("City ID is null"))

        val engineersCollection = FirestoreInstance.db.collection(FirestoreCollections.ENGINEERS)
            .whereEqualTo(FieldPath.of("cityItem", "id"), cityId)

        emitAll(callbackFlow {
            val listener = engineersCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error("Failed to load engineers: ${error.message}"))
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val engineers = snapshot.toObjects(Engineer::class.java)
                    trySend(Resource.Success(engineers))
                }
            }
            awaitClose { listener.remove() }
        })
    }


    fun getOnlineEngineersList(): Flow<Resource<List<Engineer>>> = flow {
        emit(Resource.Loading())

        val cityItem = dataStore.cityItemData.firstOrNull()
        val cityId = cityItem?.id ?: return@flow emit(Resource.Error("City ID is null"))

        val engineersCollection = FirestoreInstance.db.collection(FirestoreCollections.ENGINEERS)
            .whereEqualTo(FieldPath.of(FirestoreFieldNames.CITYITEM, FirestoreFieldNames.ID), cityId)
            .whereEqualTo(FirestoreFieldNames.STATE,true)

        emitAll(callbackFlow {
            val listener = engineersCollection.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error("Failed to load engineers: ${error.message}"))
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val engineers = snapshot.toObjects(Engineer::class.java)
                    trySend(Resource.Success(engineers))
                }
            }
            awaitClose { listener.remove() }
        })
    }







}

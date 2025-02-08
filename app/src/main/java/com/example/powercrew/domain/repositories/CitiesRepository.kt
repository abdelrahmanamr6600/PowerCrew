package com.example.powercrew.domain.repositories

import DataStoreManager
import android.content.Context
import android.util.Log

import com.example.powercrew.domain.models.Cities
import com.example.powercrew.domain.models.CityItem
import com.example.powercrew.utils.FirestoreCollections
import com.example.powercrew.utils.FirestoreFieldNames
import com.example.powercrew.utils.Resource
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CitiesRepository(context: Context) {
    private  val dataStore =  DataStoreManager(context)
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var fireBaseFireStore = FirebaseFirestore.getInstance()

    private fun convertCitiesJsonToModel(jsonString: String): Cities {
        return Gson().fromJson(jsonString, object : TypeToken<Cities>() {}.type)
    }
    suspend fun readJsonCitiesFromAssets(context: Context): Cities? {
        return withContext(Dispatchers.IO) {
            try {
                val jsonString = context.assets.open("cities.json")
                    .bufferedReader().use { it.readText() }
                convertCitiesJsonToModel(jsonString)
            } catch (e: Exception) {
                Log.e("CitiesRepository", "Error reading JSON file", e)
                null
            }
        }
    }

     suspend fun setUserCityToFirestore(city:CityItem):Resource<Unit>{
       return  try {
             fireBaseFireStore.collection(FirestoreCollections.USER)
                .document(firebaseAuth.uid!!)
                .update(FirestoreFieldNames.CITY,city).await()
                 dataStore.updateCity(city)
                  Resource.Success(Unit)
        }catch (e:FirebaseFirestoreException){
            Resource.Error(e.message.toString())
        }
    }
}
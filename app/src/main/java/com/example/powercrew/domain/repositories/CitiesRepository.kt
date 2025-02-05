package com.example.powercrew.domain.repositories

import android.content.Context
import android.util.Log

import com.example.powercrew.domain.models.Cities
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CitiesRepository() {


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
}
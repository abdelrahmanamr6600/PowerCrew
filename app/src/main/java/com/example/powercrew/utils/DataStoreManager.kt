import android.app.appsearch.AppSearchSchema.StringPropertyConfig
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.powercrew.R
import com.example.powercrew.domain.models.CityItem

import com.example.powercrew.domain.models.User
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Locale

// إنشاء امتداد DataStore للـ Context
val Context.dataStore by preferencesDataStore("db")

class DataStoreManager(private val context: Context) {
    private val dataStoreMutex = Mutex()



    companion object {

        val USER_JSON_KEY = stringPreferencesKey("user")
        val loginState_KEY = booleanPreferencesKey("loginState")
        val FULLNAME_KEY = stringPreferencesKey("fullName")
        private val LANGUAGE_KEY = stringPreferencesKey("language")


    }


    suspend fun saveUserData(user: User) {
        val gson = Gson()
        val userJson = gson.toJson(user)

        dataStoreMutex.withLock {
            context.dataStore.edit { preferences ->
                preferences[USER_JSON_KEY] = userJson
            }
        }
    }
    suspend fun updateFullName(newName: String) {
       val user =  userData.firstOrNull()
            user?.fullName = newName

        context.dataStore.edit { preferences ->
            val userJson = Gson().toJson(user)
            preferences[USER_JSON_KEY] = userJson
        }
    }

    suspend fun updatePhone(newPhone: String) {
        val user =  userData.firstOrNull()
        user?.phone = newPhone

        context.dataStore.edit { preferences ->
            val userJson = Gson().toJson(user)
            preferences[USER_JSON_KEY] = userJson
        }
    }

    suspend fun updatePassword(newPassword: String) {
        val user =  userData.firstOrNull()
        user?.password= newPassword

        context.dataStore.edit { preferences ->
            val userJson = Gson().toJson(user)
            preferences[USER_JSON_KEY] = userJson
        }
    }

    suspend fun updateEmail(newEmail: String) {
        val user =  userData.firstOrNull()
        user?.email = newEmail

        context.dataStore.edit { preferences ->
            val userJson = Gson().toJson(user)
            preferences[USER_JSON_KEY] = userJson
        }
    }
    suspend fun saveLanguage(languageCode: String) {
        context.dataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
    }




    suspend fun updateCity(cityItem: CityItem) {
        val gson = Gson()
        val preferences = context.dataStore.data.first()
        val userJson = preferences[USER_JSON_KEY]
        if (userJson != null) {
            val user = gson.fromJson(userJson, User::class.java)
            val updatedUser = user.copy(cityItem = cityItem)
            val updatedUserJson = gson.toJson(updatedUser)
            context.dataStore.edit { it[USER_JSON_KEY] = updatedUserJson }
        }
    }

    suspend fun editLoginState(state: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[loginState_KEY] = state

        }
    }

    val loginState: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[loginState_KEY] ?: false
        }
    val userData: Flow<User?> = context.dataStore.data
        .map { preferences ->
            val gson = Gson()
            val userJson = preferences[USER_JSON_KEY] ?: return@map null
            gson.fromJson(userJson, User::class.java)
        }

    val cityItemData: Flow<CityItem?> = userData.map { user ->
        user?.cityItem
    }
    val languageFlow: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[LANGUAGE_KEY] ?: Locale.getDefault().language
    }


}
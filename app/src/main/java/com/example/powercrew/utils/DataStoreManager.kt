import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.powercrew.R

import com.example.powercrew.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// إنشاء امتداد DataStore للـ Context
val Context.dataStore by preferencesDataStore("db")

class DataStoreManager(private val context: Context) {


    companion object {
        val fullName_KEY = stringPreferencesKey("full_name")
        val email_KEY = stringPreferencesKey("email")
        val phone_KEY = stringPreferencesKey("phone")
        val userId_KEY = stringPreferencesKey("uid")
        val token_KEY = stringPreferencesKey("token")
        val loginState_KEY = booleanPreferencesKey("loginState")
    }

    // دالة لحفظ البيانات
    suspend fun saveUserData(user: User) {
        context.dataStore.edit { preferences ->
            preferences[fullName_KEY] = user.email
            preferences[email_KEY] = user.email
            preferences[phone_KEY] = user.phone
            preferences[userId_KEY] = user.userId
        }
    }
    suspend fun editLoginState(state:Boolean){
        context.dataStore.edit { preferences ->
            preferences[loginState_KEY] = state

        }
    }


    val loginState: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[loginState_KEY] ?: false
        }



    val userData: Flow<User> = context.dataStore.data
        .map { preferences ->
            User(
                preferences[fullName_KEY] ?: context.getString(R.string.not_found),
                preferences[email_KEY] ?: context.getString(R.string.not_found),
                preferences[phone_KEY] ?: context.getString(R.string.not_found)
            )
        }
}
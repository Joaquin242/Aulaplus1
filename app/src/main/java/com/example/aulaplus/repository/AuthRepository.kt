package com.example.aulaplus.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.authDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "auth_preferences"
)

class AuthRepository(private val context: Context) {

    // users_json: [{"name":"","email":"","password":""}, ...]
    private val USERS_JSON = stringPreferencesKey("users_json")
    private val CURRENT_EMAIL = stringPreferencesKey("current_email")

    data class User(val name: String, val email: String, val password: String)

    fun getCurrentEmail(): Flow<String?> =
        context.authDataStore.data.map { it[CURRENT_EMAIL] }

    fun getUsers(): Flow<List<User>> =
        context.authDataStore.data.map { prefs ->
            val json = prefs[USERS_JSON] ?: "[]"
            val arr = JSONArray(json)
            buildList {
                for (i in 0 until arr.length()) {
                    val o = arr.getJSONObject(i)
                    add(User(o.getString("name"), o.getString("email"), o.getString("password")))
                }
            }
        }

    suspend fun saveUsers(list: List<User>) {
        val arr = JSONArray()
        list.forEach { u ->
            arr.put(JSONObject().apply {
                put("name", u.name)
                put("email", u.email)
                put("password", u.password)
            })
        }
        context.authDataStore.edit { it[USERS_JSON] = arr.toString() }
    }

    suspend fun setCurrentEmail(email: String?) {
        context.authDataStore.edit {
            if (email == null) it.remove(CURRENT_EMAIL) else it[CURRENT_EMAIL] = email
        }
    }
}

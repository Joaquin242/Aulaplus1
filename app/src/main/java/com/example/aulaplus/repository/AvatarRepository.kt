package com.example.aulaplus.repository

import android.content.Context
import android.net.Uri
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.avatarDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "avatar_preferences"
)

class AvatarRepository(private val context: Context) {

    companion object {
        private val AVATAR_URI_KEY = stringPreferencesKey("avatar_uri_key")
    }

    fun getAvatarUri(): Flow<Uri?> =
        context.avatarDataStore.data.map { it[AVATAR_URI_KEY]?.let(Uri::parse) }

    suspend fun saveAvatarUri(uri: Uri?) {
        context.avatarDataStore.edit { prefs ->
            if (uri == null) prefs.remove(AVATAR_URI_KEY)
            else prefs[AVATAR_URI_KEY] = uri.toString()
        }
    }
}

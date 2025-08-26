package com.example.englishappforkid.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Tạo một extension property để dễ dàng truy cập DataStore trong toàn bộ ứng dụng
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class DataManager(
    context: Context,
) {
    private val dataStore = context.dataStore

    private object PreferencesKeys {
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_PASSWORD = stringPreferencesKey("user_password")
    }

    // Hàm để LƯU email và mật khẩu
    suspend fun saveCredentials(
        email: String,
        pass: String,
    ) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_EMAIL] = email
            preferences[PreferencesKeys.USER_PASSWORD] = pass
        }
    }

    // Hàm để XÓA email và mật khẩu (khi người dùng không tick "Remember me")
    suspend fun clearCredentials() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_EMAIL)
            preferences.remove(PreferencesKeys.USER_PASSWORD)
        }
    }

    // Luồng để đọc email.
    val userEmailFlow: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_EMAIL]
        }

    // Luồng để đọc mật khẩu.
    val userPasswordFlow: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_PASSWORD]
        }
}

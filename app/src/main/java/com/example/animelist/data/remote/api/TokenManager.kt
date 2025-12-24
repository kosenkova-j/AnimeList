// data/remote/TokenManager.kt
package com.example.animelist.data.remote

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

// Создаём extension property для DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_tokens")


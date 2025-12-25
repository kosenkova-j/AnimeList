//package com.example.animelist.data.remote
//
//import android.content.Context
//import androidx.datastore.preferences.core.edit
//import androidx.datastore.preferences.core.stringPreferencesKey
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.map
//import javax.inject.Inject
//import javax.inject.Singleton
//
//@Singleton
//class TokenManager @Inject constructor(
//    private val context: Context
//) {
//    companion object {
//        // Публичный токен приложения (получается один раз)
//        // В документации: "create a new application" чтобы получить
//        const val DEFAULT_APP_TOKEN = "7t4po9p3sj79pjan"
//    }
//
//    fun getAppToken(): String = DEFAULT_APP_TOKEN
//}
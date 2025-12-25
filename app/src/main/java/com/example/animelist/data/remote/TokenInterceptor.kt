//package com.example.animelist.data.remote
//
//import okhttp3.Interceptor
//import okhttp3.Response
//import javax.inject.Inject
//
//// data/remote/TokenInterceptor.kt (только публичный токен)
//class TokenInterceptor @Inject constructor(
//    private val tokenManager: TokenManager
//) : Interceptor {
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request().newBuilder()
//            .addHeader("X-Application", tokenManager.getAppToken())
//            .addHeader("Accept", "image/avif,image/webp,application/json")
//            .addHeader("Lang", "ru") // Русский язык
//            .build()
//
//        return chain.proceed(request)
//    }
//}
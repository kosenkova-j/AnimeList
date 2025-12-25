package com.example.animelist.presentation.di

import com.example.animelist.data.remote.api.YummyAnimeApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // ????? ?????????? YummyAnime
    private const val APP_TOKEN = "7t4po9p3sj79pjan"

    // ??????? ?????????? URL ? ????????????!
    private const val BASE_URL = "https://api.yani.tv/"

    // ? NetworkModule.kt
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            // ??????? ???????, ????? ?? ???????? ??????? ??????
            if (message.length > 4000) {
                var i = 0
                while (i < message.length) {
                    val end = minOf(i + 4000, message.length)
                    println("OkHttp: ${message.substring(i, end)}")
                    i = end
                }
            } else {
                println("OkHttp: $message")
            }
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY  // ? BODY ?????? HEADERS
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)  // ???????? ??? ??????? ???????????
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            // ??????????? ? ???????
            .addInterceptor { chain ->
                val original = chain.request()

                val request = original.newBuilder()
                    .addHeader("X-Application", APP_TOKEN)  // ? ????????!
                    .addHeader("Accept", "application/json")
                    .addHeader("Lang", "ru")
                    .method(original.method, original.body)
                    .build()

                println(">>> Request: ${request.method} ${request.url}")
                println(">>> Headers: ${request.headers}")

                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideYummyAnimeApi(retrofit: Retrofit): YummyAnimeApi {
        return retrofit.create(YummyAnimeApi::class.java)
    }
}
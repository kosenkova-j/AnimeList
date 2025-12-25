package com.example.animelist.presentation.di

//import com.example.animelist.BuildConfig
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

// di/NetworkModule.kt (упрощённый)
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // 1. OkHttpClient
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept", "image/avif,image/webp")
                    .addHeader("Lang", "ru")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    // 2. Moshi (ЕСЛИ ИСПОЛЬЗУЕТЕ MOSHI)
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())  // Для Kotlin классов
            .build()
    }

    // 3. Retrofit с Moshi
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        moshi: Moshi  // ← ПОЛУЧАЕМ MOSHI
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.yani.tv/")  // ← URL ПРАВИЛЬНЫЙ
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))  // ← ПЕРЕДАЁМ MOSHI
            .build()
    }

    // 4. API интерфейс
    @Provides
    @Singleton
    fun provideYummyAnimeApi(retrofit: Retrofit): YummyAnimeApi {
        return retrofit.create(YummyAnimeApi::class.java)
    }
}
package com.example.tiktok_clone.di

import android.content.Context
import android.content.SharedPreferences
import com.example.tiktok_clone.BuildConfig
import com.example.tiktok_clone.core.config.ApiConfig
import com.example.tiktok_clone.features.post.data.repository.PostApiService
import com.example.tiktok_clone.features.post.data.repository.UploadRepository
import com.example.tiktok_clone.features.post.viewmodel.PostViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {
    // SharedPreferences example
    single<SharedPreferences> {
        androidContext().getSharedPreferences("tiktok_prefs", Context.MODE_PRIVATE)
    }

    single {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(ApiConfig.getBaseUrl())  // Use centralized config
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<PostApiService> {
        get<Retrofit>().create(PostApiService::class.java)
    }

    single {
        UploadRepository(androidContext(), get())
    }

    single {
        PostViewModel(get(), get())
    }
    
    // Add other app-level singletons here:
    // single { NetworkUtils() }
    // single { DatabaseHelper(get()) }
    // single { ApiClient() }
}


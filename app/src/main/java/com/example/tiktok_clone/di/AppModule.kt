package com.example.tiktok_clone.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    // SharedPreferences example
    single<SharedPreferences> {
        androidContext().getSharedPreferences("tiktok_prefs", Context.MODE_PRIVATE)
    }
    
    // Add other app-level singletons here:
    // single { NetworkUtils() }
    // single { DatabaseHelper(get()) }
    // single { ApiClient() }
}

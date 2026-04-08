package com.example.tiktok_clone

import android.app.Application
import com.example.tiktok_clone.di.appModule
import com.example.tiktok_clone.di.homeModule
import com.example.tiktok_clone.di.cameraModule
import com.example.tiktok_clone.di.socialModule
import com.example.tiktok_clone.di.notificationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import com.example.tiktok_clone.core.user.di.userModule

/**
 * Main Application class for TikTok Clone
 * 
 * Responsibilities:
 * - Initialize Koin dependency injection
 * - Provide application-level services
 * - Setup global configuration
 */
class TikTokApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Koin DI container
        startKoin {
            // Enable Koin logger for debugging
            androidLogger()
            
            // Provide Android context to Koin
            androidContext(this@TikTokApplication)
            
            // Load all DI modules
            modules(
                appModule,      // Core app dependencies
                homeModule,     // Home feature ViewModels
                cameraModule,   // Camera feature ViewModels  
                socialModule,     // Social feature ViewModels
                notificationModule, // Notification feature ViewModels
                userModule,
            )
        }
    }
}

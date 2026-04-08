package com.example.tiktok_clone.core.config

import com.example.tiktok_clone.BuildConfig

/**
 * Centralized API configuration
 * Change BASE_URL here to update all API endpoints
 */
object ApiConfig {
        // 🔥 LOADED FROM .ENV VIA BUILDCONFIG 🔥
        // Sync project with Gradle files if BuildConfig is not updated
        val BASE_URL = BuildConfig.API_BASE_URL

        // Environment-specific URLs
        val LOCAL_EMULATOR = BuildConfig.API_URL_EMULATOR
        val LOCAL_DEVICE = BuildConfig.API_URL_DEVICE
        val STAGING = BuildConfig.API_URL_STAGING
        val PRODUCTION = BuildConfig.API_URL_PRODUCTION

    // Auto-detect environment (optional)
    fun getBaseUrl(): String {
        return when {
            BuildConfig.DEBUG -> BASE_URL
//            BuildConfig.DEBUG -> LOCAL_EMULATOR
            else -> PRODUCTION
        }
    }

    // WebSocket base URL: http → ws, https → wss
    fun getWsBaseUrl(): String =
        getBaseUrl()
            .replace("https://", "wss://")
            .replace("http://", "ws://")
}

/**
 * Network timeouts and other configurations
 */
object NetworkConfig {
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L
}

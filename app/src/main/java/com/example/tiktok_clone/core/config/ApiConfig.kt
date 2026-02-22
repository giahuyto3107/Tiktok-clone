package com.example.tiktok_clone.core.config

import com.example.tiktok_clone.BuildConfig

/**
 * Centralized API configuration
 * Change BASE_URL here to update all API endpoints
 */
object ApiConfig {
    // 🔥 CHANGE THIS LINE TO UPDATE YOUR SERVER URL 🔥
    // Retrofit requires baseUrl to end with "/"
    // Keep this as server root; endpoints include `/api/v1/...` to avoid URL-join issues.
    const val BASE_URL = "http://192.168.110.117:8000/"
    
    // Environment-specific URLs
    const val LOCAL_EMULATOR = "http://10.0.2.2:8000/"
    const val LOCAL_DEVICE = "http://192.168.110.117:8000/"
    const val STAGING = "http://staging-api.yourapp.com/"
    const val PRODUCTION = "http://api.yourapp.com/"
    
    // Auto-detect environment (optional)
    fun getBaseUrl(): String {
        return when {
            // You can add logic here to auto-detect environment
            BuildConfig.DEBUG -> BASE_URL
            else -> PRODUCTION
        }
    }
}

/**
 * Network timeouts and other configurations
 */
object NetworkConfig {
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L
}

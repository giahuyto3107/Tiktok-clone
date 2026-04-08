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

    // Emulator: 10.0.2.2 = máy dev (localhost trên PC). KHÔNG dùng 127.0.0.1 — đó là chính điện thoại.
    const val BASE_URL = "http://10.0.2.2:8000/"
    // Máy thật: IP Wi‑Fi của PC, ví dụ "http://192.168.1.50:8000/" + chạy uvicorn --host 0.0.0.0

    // Environment-specific URLs
    const val LOCAL_EMULATOR = "http://10.0.2.2:8000/"
    /** Điện thoại thật: thay bằng IP LAN thật của PC (ipconfig), không dùng 127.0.0.1 */
    const val LOCAL_DEVICE = "http://172.20.10.12:8000/"
    const val STAGING = "http://staging-api.yourapp.com/"
    const val PRODUCTION = "http://api.yourapp.com/"

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

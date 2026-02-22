package com.example.tiktok_clone.features.home.post.ui

sealed class UploadState {
    object Idle: UploadState()
    object Loading: UploadState()
    object Success: UploadState()
    data class Error(val message: String) : UploadState()
}
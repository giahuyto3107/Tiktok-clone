package com.example.tiktok_clone.features.camera.data

import android.net.Uri

data class CameraUiState(
    val hasCameraPermissions: Boolean = false,
    val hasStoragePermissions: Boolean = false,
    val isRecording: Boolean = false,
    val selectedModeIndex: Int = 3, // 0=10m, 1=60s, 2=15s, 3=Photo
    val latestGalleryUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

package com.example.tiktok_clone.features.home.camera.viewmodel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.home.camera.data.CameraUiState
import com.example.tiktok_clone.features.home.camera.ui.components.getLastGalleryImageUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CameraViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun initializeCameraController(context: Context): LifecycleCameraController {
        return LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    fun checkPermissions(context: Context) {
        viewModelScope.launch {
            val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

            val cameraPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

            val recordAudioPermission = ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            val storagePermissionGranted = ContextCompat.checkSelfPermission(
                context, storagePermission
            ) == PackageManager.PERMISSION_GRANTED

            _uiState.value = _uiState.value.copy(
                hasCameraPermissions = cameraPermission && recordAudioPermission,
                hasStoragePermissions = storagePermissionGranted
            )

            // Load latest gallery image if storage permission is granted
            if (storagePermissionGranted) {
                _uiState.value = _uiState.value.copy(
                    latestGalleryUri = getLastGalleryImageUri(context)
                )
            }
        }
    }

    fun updatePermissions(
        cameraPermissions: Boolean,
        storagePermissions: Boolean,
        context: Context
    ) {
        _uiState.value = _uiState.value.copy(
            hasCameraPermissions = cameraPermissions,
            hasStoragePermissions = storagePermissions
        )

        // Load latest gallery image if storage permission is granted
        if (storagePermissions) {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(
                    latestGalleryUri = getLastGalleryImageUri(context)
                )
            }
        }
    }

    fun updateRecordingState(isRecording: Boolean) {
        _uiState.value = _uiState.value.copy(isRecording = isRecording)
    }

    fun updateSelectedMode(index: Int) {
        _uiState.value = _uiState.value.copy(selectedModeIndex = index)
    }

    fun updateLatestGalleryUri(uri: Uri?) {
        _uiState.value = _uiState.value.copy(latestGalleryUri = uri)
    }

    fun getStoragePermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    fun getRequiredPermissions(): Array<String> {
        val permissions = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        
        if (!_uiState.value.hasStoragePermissions) {
            permissions.add(getStoragePermission())
        }
        
        return permissions.toTypedArray()
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

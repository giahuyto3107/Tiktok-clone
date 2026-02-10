package com.example.tiktok_clone.features.home.camera.viewmodel

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.home.camera.data.CameraUiState
import com.example.tiktok_clone.features.home.camera.ui.components.getLastGalleryImageUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class CameraViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()
    
    private var activeRecording: Recording? = null

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
    
    fun startRecording(context: Context, controller: LifecycleCameraController) {
        if (_uiState.value.isRecording) return
        
        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/TikTok-Clone")
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(context.contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        val hasAudioPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        try {
            activeRecording = controller.startRecording(
                mediaStoreOutputOptions,
                AudioConfig.create(hasAudioPermission),
                ContextCompat.getMainExecutor(context)
            ) { event ->
                if (event is VideoRecordEvent.Finalize) {
                    if (!event.hasError()) {
                        Toast.makeText(context, "Video Saved", Toast.LENGTH_SHORT).show()
                        // Update latest gallery URI
                        updateLatestGalleryUri(getLastGalleryImageUri(context))
                    } else {
                        Toast.makeText(context, "Video Error", Toast.LENGTH_SHORT).show()
                        _uiState.value = _uiState.value.copy(error = "Recording failed: ${event.cause}")
                    }
                    // Stop recording state
                    updateRecordingState(false)
                }
            }
            updateRecordingState(true)
        } catch (e: Exception) {
            Log.e("CameraViewModel", "Failed to start recording", e)
            _uiState.value = _uiState.value.copy(error = "Failed to start recording: ${e.message}")
        }
    }
    
    fun stopRecording() {
        activeRecording?.stop()
        activeRecording = null
        updateRecordingState(false)
    }
    
    override fun onCleared() {
        super.onCleared()
        // Clean up any active recording
        activeRecording?.stop()
        activeRecording = null
    }
}

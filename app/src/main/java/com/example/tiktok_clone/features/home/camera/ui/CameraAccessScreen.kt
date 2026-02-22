package com.example.tiktok_clone.features.home.camera.ui

import android.Manifest
import android.os.Build
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.home.camera.ui.components.BottomTabSection
import com.example.tiktok_clone.features.home.camera.ui.components.CameraPreviewScreen
import com.example.tiktok_clone.features.home.camera.ui.components.SnapAndTimeOption
import com.example.tiktok_clone.features.home.camera.ui.components.getLastGalleryImageUri
import com.example.tiktok_clone.features.home.camera.ui.components.openSystemSettings
import com.example.tiktok_clone.features.home.camera.viewmodel.CameraViewModel
import com.example.tiktok_clone.features.home.post.data.model.PostType
import com.example.tiktok_clone.features.home.post.ui.UploadState
import com.example.tiktok_clone.features.home.post.viewmodel.PostViewModel
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CameraAccessScreen(
    onNavigationToHomeScreen: () -> Unit,
    cameraViewModel: CameraViewModel = koinViewModel(),
    postViewModel: PostViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by cameraViewModel.uiState.collectAsState()
    val uploadState by postViewModel.uploadState.collectAsState()

    val cameraController = remember {
        cameraViewModel.initializeCameraController(context)
    }

    // PICKER: Setup the Photo Picker — upload selected image
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        Log.d("CameraAccessScreen", "Photo picker result: $uri")
        uri?.let {
            val mimeType = context.contentResolver.getType(it)
            val postType = if (mimeType?.contains("video") == true) PostType.VIDEO else PostType.IMAGE
            Log.d("CameraAccessScreen", "Starting upload for URI: $uri, type: $postType, current state: $uploadState")
            
            // Only start new upload if not already uploading
            if (uploadState is UploadState.Idle) {
                postViewModel.upload(it, "", postType)
            } else {
                Log.w("CameraAccessScreen", "Upload already in progress, ignoring new request")
                Toast.makeText(context, "Please wait for current upload to complete", Toast.LENGTH_SHORT).show()
            }
        } ?: Log.d("CameraAccessScreen", "No URI selected from photo picker")
    }

    LaunchedEffect(uploadState) {
        when (uploadState) {
            is UploadState.Success -> {
                Toast.makeText(context, "Posted!", Toast.LENGTH_SHORT).show()
                // Reset state immediately but don't navigate - let user decide
                postViewModel.resetUploadState()
            }
            is UploadState.Error -> {
                Toast.makeText(context, (uploadState as UploadState.Error).message, Toast.LENGTH_LONG).show()
                postViewModel.resetUploadState()
            }
            else -> { }
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        val cameraPermissions = permissions[Manifest.permission.CAMERA] == true &&
                             permissions[Manifest.permission.RECORD_AUDIO] == true

        val storagePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions[Manifest.permission.READ_MEDIA_IMAGES] == true ||
                    permissions[Manifest.permission.READ_MEDIA_VIDEO] == true
        } else {
            permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        }

        cameraViewModel.updatePermissions(cameraPermissions, storagePermissions, context)
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                cameraViewModel.checkPermissions(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        cameraViewModel.checkPermissions(context)
        
        if (!uiState.hasCameraPermissions || !uiState.hasStoragePermissions) {
            permissionLauncher.launch(cameraViewModel.getRequiredPermissions())
        }
        
        // Reset upload state when screen is displayed to prevent stuck states
        if (uploadState !is UploadState.Idle) {
            postViewModel.resetUploadState()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
        ) {

            if (uiState.hasCameraPermissions) {
                CameraPreviewScreen(
                    controller = cameraController
                )
            } else {
                NoPermissionBackground()
            }

            CancelButton(onNavigationToHomeScreen = onNavigationToHomeScreen)

            if (!uiState.hasCameraPermissions) {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    PermissionRequestContent(
                        onOpenSettings = {
                            openSystemSettings(context)
                        }
                    )
                }
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {
                SnapAndTimeOption(
                    hasPermission = uiState.hasCameraPermissions,
                    isRecording = uiState.isRecording,
                    onModeChange = { index -> cameraViewModel.updateSelectedMode(index) },
                    recordingMode = uiState.selectedModeIndex != 3,
                    onSnapClick = {
                        cameraViewModel.updateLatestGalleryUri(getLastGalleryImageUri(context))
                        
                        if (uiState.selectedModeIndex == 3) {
                            takePhoto(context, cameraController) {
                                cameraViewModel.updateLatestGalleryUri(getLastGalleryImageUri(context))
                            }
                        } else {
                            if (uiState.isRecording) {
                                cameraViewModel.stopRecording()
                            } else {
                                cameraViewModel.startRecording(context, cameraController)
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
        }

        BottomTabSection(
            hasPermission = uiState.hasCameraPermissions,
            latestImageUri = uiState.latestGalleryUri,
            onGalleryClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageAndVideo
                    )
                )
            }
        )
    }
}

@Composable
fun NoPermissionBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xff0F6B78),
                        Color(0xff5C554C)
                    )
                )
            )
    )
}

@Composable
fun PermissionRequestContent(onOpenSettings: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = dimensionResource(R.dimen.spacing_xxxl))
            .fillMaxWidth()
    ) {
        Text(
            text = "Allow Tiktok to access to your camera and microphone",
            color = colorResource(R.color.text_on_dark),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_l)))
                .background(color = Color(0xff658c8b))
                .fillMaxWidth()
                .clickable { onOpenSettings() }
                .padding(
                    horizontal = dimensionResource(R.dimen.spacing_m),
                    vertical = dimensionResource(R.dimen.spacing_xxl),
                )
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.Cog,
                contentDescription = "Settings",
                tint = colorResource(R.color.text_on_dark),
                modifier = Modifier.size(dimensionResource(R.dimen.font_title_m))
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.spacing_m)))
            Box(modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_xxs))) {
                Text(
                    text = "Open settings",
                    color = colorResource(R.color.text_on_dark),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun CancelButton(onNavigationToHomeScreen: () -> Unit) {
    Box(
        modifier = Modifier.padding(
            top = dimensionResource(R.dimen.spacing_m),
            start = dimensionResource(R.dimen.spacing_m),
        )
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Cancel",
            tint = colorResource(R.color.text_on_dark),
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(dimensionResource(R.dimen.font_title_m))
                .clickable { onNavigationToHomeScreen() }
        )
    }
}

private fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onImageSaved: () -> Unit = {}
) {
    val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
        .format(System.currentTimeMillis())

    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/TikTok-Clone")
    }

    val outputOptions = ImageCapture.OutputFileOptions
        .Builder(context.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        .build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                Toast.makeText(context, "Photo Saved", Toast.LENGTH_SHORT).show()
                onImageSaved()
            }
            override fun onError(exc: ImageCaptureException) {
                Log.e("Camera", "Photo capture failed: ${exc.message}", exc)
            }
        }
    )
}

@Preview
@Composable
private fun PreviewCameraAccessScreen() {
    CameraAccessScreen(onNavigationToHomeScreen = {})
}
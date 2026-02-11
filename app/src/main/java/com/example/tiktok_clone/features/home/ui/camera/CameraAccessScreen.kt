package com.example.tiktok_clone.features.home.ui.camera

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Recording
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.video.AudioConfig
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.home.ui.camera.components.BottomTabSection
import com.example.tiktok_clone.features.home.ui.camera.components.SnapAndTimeOption
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CameraAccessScreen(
    onNavigationToHomeScreen: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE
            )
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    var isRecording by remember { mutableStateOf(false) }
    var activeRecording: Recording? by remember { mutableStateOf(null) }

    // State for selected mode (0=10m, 1=60s, 2=15s, 3=Photo)
    var selectedModeIndex by remember { mutableIntStateOf(3) }

    // STATE: Track latest image
    var latestGalleryUri by remember { mutableStateOf<Uri?>(null) }

    // PICKER: Setup the Photo Picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { }

    // PERMISSIONS: Determine which permission to ask for based on Android version
    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var hasCameraPermissions by remember {
        mutableStateOf(checkCameraPermissions(context))
    }
    var hasStoragePermissions by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, storagePermission
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    LaunchedEffect(hasStoragePermissions) {
        if (hasStoragePermissions) {
            latestGalleryUri = getLastGalleryImageUri(context)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        hasCameraPermissions = permissions[Manifest.permission.CAMERA] == true &&
                     permissions[Manifest.permission.RECORD_AUDIO] == true

        if (permissions[storagePermission] == true) {
            hasStoragePermissions = true
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasCameraPermissions = checkCameraPermissions(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }


    LaunchedEffect(Unit) {
        val permissionsToRequest = mutableListOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        if (!hasStoragePermissions) {
            permissionsToRequest.add(storagePermission)
        }

        if (!hasCameraPermissions || !hasStoragePermissions) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
        ) {

            if (hasCameraPermissions) {
                CameraPreviewScreen(
                    controller = cameraController
                )
            } else {
                NoPermissionBackground()
            }

            CancelButton(onNavigationToHomeScreen = onNavigationToHomeScreen)

            if (!hasCameraPermissions) {
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
                    hasPermission = hasCameraPermissions,
                    isRecording = isRecording,
                    onModeChange = { index -> selectedModeIndex = index },
                    recordingMode = selectedModeIndex != 3,
                    onSnapClick = {
                        if (selectedModeIndex == 3) {
                            takePhoto(context, cameraController) {
                                latestGalleryUri = getLastGalleryImageUri(context)
                            }
                        } else {
                            if (isRecording) {
                                activeRecording?.stop()
                                isRecording = false
                                activeRecording = null
                            } else {
                                isRecording = true
                                activeRecording = startRecording(
                                    context = context,
                                    controller = cameraController,
                                    onFinished = {
                                        isRecording = false
                                    },
                                    onVideoSaved = {
                                        latestGalleryUri = getLastGalleryImageUri(context)
                                    }
                                )
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

            }
        }

        BottomTabSection(
            hasPermission = hasCameraPermissions,
            latestImageUri = latestGalleryUri,
            onGalleryClick = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
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
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
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

    val outputOptions = androidx.camera.core.ImageCapture.OutputFileOptions
        .Builder(context.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        .build()

    controller.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : androidx.camera.core.ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(output: androidx.camera.core.ImageCapture.OutputFileResults) {
                Toast.makeText(context, "Photo Saved", Toast.LENGTH_SHORT).show()
                onImageSaved()
            }
            override fun onError(exc: androidx.camera.core.ImageCaptureException) {
                Log.e("Camera", "Photo capture failed: ${exc.message}", exc)
            }
        }
    )
}

private fun startRecording(
    context: Context,
    controller: LifecycleCameraController,
    onFinished: () -> Unit,
    onVideoSaved: () -> Unit = {}
): Recording {
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

    // Ensure Audio is enabled
    return controller.startRecording(
        mediaStoreOutputOptions,
        AudioConfig.create(hasAudioPermission),
        ContextCompat.getMainExecutor(context)
    ) { event ->
        if (event is VideoRecordEvent.Finalize) {
            if (!event.hasError()) {
                Toast.makeText(context, "Video Saved", Toast.LENGTH_SHORT).show()
                onVideoSaved()
            } else {
                Toast.makeText(context, "Video Error", Toast.LENGTH_SHORT).show()
            }
            onFinished()
        }
    }
}


@Preview
@Composable
private fun PreviewCameraAccessScreen() {
    CameraAccessScreen(onNavigationToHomeScreen = {})
}
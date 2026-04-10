package com.example.tiktok_clone.features.camera.ui.components

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.view.ViewGroup
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import androidx.compose.ui.res.dimensionResource
import androidx.core.content.ContextCompat
import coil.ImageLoader
import coil.decode.VideoFrameDecoder
import com.example.tiktok_clone.R
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Image

@Composable
fun CameraPreviewScreen(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifeCycleOwner = LocalLifecycleOwner.current

    // Bind the controller to the lifecycle
    LaunchedEffect(lifeCycleOwner) {
        controller.bindToLifecycle(lifeCycleOwner)
    }

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
                this.controller = controller
            }
        },
        modifier = modifier.fillMaxSize()
    )
}


@Composable
fun GalleryThumbnail(
    latestImageUri: Uri?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(VideoFrameDecoder.Factory())
        }
        .build()

    Box(
        modifier = modifier
            .size(35.dp)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_m)))
            .border(dimensionResource(R.dimen.border_thin), Color.White, RoundedCornerShape(dimensionResource(R.dimen.radius_m)))
            .background(Color.DarkGray)
            .clickable { onClick() }
    ) {
        if (latestImageUri != null) {
            AsyncImage(
                model = latestImageUri,
                imageLoader = imageLoader,
                contentDescription = "Latest media",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // Fallback icon if no image found
            Icon(
                imageVector = FontAwesomeIcons.Solid.Image,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.align(Alignment.Center).size(20.dp)
            )
        }
    }
}


// --- Helper Functions ---

fun checkCameraPermissions(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED
}

fun openSystemSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

fun getLastGalleryImageUri(context: Context): Uri? {
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Images.Media.DATE_ADDED
    )

    val selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
            + " OR "
            + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
            + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

    val sortOrder = "${MediaStore.Files.FileColumns.DATE_ADDED} DESC"
    val queryUri = MediaStore.Files.getContentUri("external")

    return context.contentResolver.query(
        queryUri,
        projection,
        selection,
        null,
        sortOrder
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val mediaTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)

            val id = cursor.getLong(idColumn)
            val type = cursor.getInt(mediaTypeColumn)

            // Determine if we should return an Image or Video URI
            val baseUri = if (type == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }

            ContentUris.withAppendedId(baseUri, id)
        } else null
    }
}
package com.example.tiktok_clone.features.home.ui.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.tiktok_clone.core.utils.AppColors
import com.example.tiktok_clone.core.utils.AppConstants
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import compose.icons.fontawesomeicons.solid.Times

@Composable
fun CameraAccessScreen(
    onNavigationToHomeScreen: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

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

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {

        if (hasCameraPermissions) {
            CameraPreviewScreen()
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
            SnapAndTimeOption()
            Spacer(modifier = Modifier.height(20.dp))
            BottomTabSection(
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
            .padding(horizontal = AppConstants.SPACING_XXXL.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "Allow Tiktok to access to your camera and microphone",
            color = AppColors.TEXT_ON_DARK,
            style = MaterialTheme.typography.titleMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(AppConstants.SPACING_M.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(AppConstants.RADIUS_L.dp))
                .background(color = Color(0xff658c8b))
                .fillMaxWidth()
                .clickable { onOpenSettings() }
                .padding(
                    horizontal = AppConstants.SPACING_M.dp,
                    vertical = AppConstants.SPACING_XXL.dp,
                )
        ) {
            Icon(
                imageVector = FontAwesomeIcons.Solid.Cog,
                contentDescription = "Settings",
                tint = AppColors.TEXT_ON_DARK,
                modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp)
            )
            Spacer(modifier = Modifier.width(AppConstants.SPACING_M.dp))
            Box(modifier = Modifier.padding(top = AppConstants.SPACING_XXS.dp)) {
                Text(
                    text = "Open settings",
                    color = AppColors.TEXT_ON_DARK,
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
            top = AppConstants.SPACING_M.dp,
            start = AppConstants.SPACING_M.dp,
        )
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Cancel",
            tint = AppColors.TEXT_ON_DARK,
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(AppConstants.FONT_TITLE_M.dp)
                .clickable { onNavigationToHomeScreen() }
        )
    }
}

@Composable
private fun SnapAndTimeOption(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeOptionRow(modifier = Modifier
            .fillMaxWidth(0.6f)
        )
        Spacer(modifier = Modifier.height(AppConstants.SPACING_XL.dp))
        SnapButton(modifier = Modifier
            .fillMaxWidth()
        )
    }
}

@Composable
private fun TimeOptionRow(
    modifier: Modifier = Modifier
) {
    val options = listOf("10m", "60s", "15s", "PHOTO")

    val pagerState = rememberPagerState(pageCount = { options.size }, initialPage = 3)

    Box(modifier = modifier.height(30.dp).fillMaxWidth()) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 160.dp), // Large padding to center single item
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            Text(
                text = options[page],
                color = if (pagerState.currentPage == page) Color.White else Color.White.copy(alpha = 0.5f),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun SnapButton(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier,
        contentAlignment =  Alignment.Center) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xff9C988F))
        )
    }
}

@Composable
private fun BottomTabSection(
    latestImageUri: Uri?,
    onGalleryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(modifier = Modifier
            .fillMaxWidth(0.5f)
            .padding(
                start = AppConstants.SPACING_M.dp,
                bottom = AppConstants.SPACING_M.dp
            )
        ) {
            GalleryThumbnail(
                latestImageUri = latestImageUri,
                onClick = onGalleryClick,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        PostCategorySlider(
            modifier = Modifier.fillMaxWidth(1f)
        )
    }
}

@Composable
private fun PostCategorySlider(modifier: Modifier = Modifier) {
    var selectedPostCategoryIndex by remember { mutableIntStateOf(0) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        PostCategorySliderItem(
            name = "POST",
            isSelected = selectedPostCategoryIndex == 0,
            onTap = { selectedPostCategoryIndex = 0 }
        )
        PostCategorySliderItem(
            name = "CREATE",
            isSelected = selectedPostCategoryIndex == 1,
            onTap = { selectedPostCategoryIndex = 1 }
        )
        PostCategorySliderItem(
            name = "LIVE",
            isSelected = selectedPostCategoryIndex == 2,
            onTap = { selectedPostCategoryIndex = 2 }
        )
        Spacer(modifier = Modifier.width(AppConstants.SPACING_M.dp))
    }
}

@Composable
fun PostCategorySliderItem(
    name: String,
    isSelected: Boolean,
    onTap: () -> Unit
) {
    Text(
        text = name,
        color = if (isSelected) AppColors.TEXT_ON_DARK else AppColors.TEXT_SECONDARY,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.clickable { onTap() }
    )
}

@Preview
@Composable
private fun PreviewCameraAccessScreen() {
    CameraAccessScreen(onNavigationToHomeScreen = {})
}
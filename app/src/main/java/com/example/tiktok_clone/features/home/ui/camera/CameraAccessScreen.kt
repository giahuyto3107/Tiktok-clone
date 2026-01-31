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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.example.tiktok_clone.R
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Cog
import kotlinx.coroutines.launch

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

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
        ) {

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
                SnapAndTimeOption(hasPermission = hasCameraPermissions)
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

@Composable
private fun SnapAndTimeOption(
    hasPermission: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeOptionRow(
            hasPermission = hasPermission,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
        SnapButton(
            hasPermission = hasPermission,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TimeOptionRow(
    hasPermission: Boolean,
    modifier: Modifier = Modifier
) {
    val options = listOf("10m", "60s", "15s", "PHOTO")
    val pagerState = rememberPagerState(pageCount = { options.size }, initialPage = 3)

    val scope = rememberCoroutineScope()

    val itemWidth = 100.dp
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = (screenWidth - itemWidth) / 2


    HorizontalPager(
        state = pagerState,
        userScrollEnabled = hasPermission,
        pageSize = PageSize.Fixed(itemWidth),
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        modifier = modifier
    ) { page ->
        val isSelected = pagerState.currentPage == page

        val bgColor = when {
            !isSelected -> Color.Transparent
            hasPermission -> Color.White
            else -> Color(0xff999E98)
        }

        val textColor = when {
            isSelected && hasPermission -> Color(0xff16151b)
            isSelected && !hasPermission -> Color(0xff6C716B)
            !isSelected && hasPermission -> Color(0xfff8f6f1)
            else -> Color(0xff909790)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = options[page],
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier
                    .clip(RoundedCornerShape(dimensionResource(R.dimen.radius_xxl)))
                    .background(bgColor)
                    .padding(
                        vertical = dimensionResource(R.dimen.spacing_xs),
                        horizontal = dimensionResource(R.dimen.spacing_m)
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        scope.launch {
                            pagerState.animateScrollToPage(page)
                        }
                    }
            )
        }
    }
}

@Composable
private fun SnapButton(
    hasPermission: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier,
        contentAlignment =  Alignment.Center) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(
                    if (hasPermission) Color.White
                    else Color(0xff9C988F)
                )
        )
    }
}

@Composable
private fun BottomTabSection(
    hasPermission: Boolean,
    latestImageUri: Uri?,
    onGalleryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(modifier = Modifier
            .padding(
                start = dimensionResource(R.dimen.spacing_m),
                top = dimensionResource(R.dimen.spacing_s)
            )
        ) {
            GalleryThumbnail(
                latestImageUri = latestImageUri,
                onClick = onGalleryClick,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }

        PostCategorySlider(
            hasPermission = hasPermission,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(top = dimensionResource(R.dimen.spacing_s))
        )
    }
}

@Composable
private fun PostCategorySlider(
    hasPermission: Boolean,
    modifier: Modifier = Modifier
) {
    val options = listOf("POST", "CREATE", "LIVE")
    val pagerState = rememberPagerState(pageCount = { options.size }, initialPage = 0)

    val scope = rememberCoroutineScope()

    val itemWidth = 90.dp
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val horizontalPadding = (screenWidth - itemWidth) / 2

    HorizontalPager(
        state = pagerState,
        userScrollEnabled = hasPermission,
        pageSize = PageSize.Fixed(itemWidth),
        contentPadding = PaddingValues(horizontal = horizontalPadding),
        modifier = modifier
    ) { page ->
        PostCategorySliderItem(
            name = options[page],
            isSelected = pagerState.currentPage == page,
            onTap = {
                scope.launch {
                    pagerState.animateScrollToPage(page)
                }
            }
        )
    }
}

@Composable
fun PostCategorySliderItem(
    name: String,
    isSelected: Boolean,
    onTap: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onTap() }
    ) {
        Text(
            text = name,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Preview
@Composable
private fun PreviewCameraAccessScreen() {
    CameraAccessScreen(onNavigationToHomeScreen = {})
}
package com.example.tiktok_clone.features.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.decode.ImageSource
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
    var selectedSnapTimeIndex by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .safeDrawingPadding()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xff0F6B78),
                            0.78f to Color(0xff5C554C)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                MiddleSection()
            }
            CancelButton(onNavigationToHomeScreen)
        }

        BottomSection(
            modifier = Modifier.fillMaxSize()
        )
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
            imageVector = FontAwesomeIcons.Solid.Times,
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
private fun MiddleSection() {
    Column(
        modifier = Modifier.padding(horizontal = AppConstants.SPACING_XXXL.dp)
    ) {
        Text(
            text = "Allow Tiktok to access to your camera and microphone",
            color = AppColors.TEXT_ON_DARK,
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(AppConstants.SPACING_M.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(AppConstants.RADIUS_L.dp))
                .background(color = Color(0xff658c8b))
                .padding(
                    horizontal = AppConstants.SPACING_M.dp,
                    vertical = AppConstants.SPACING_L.dp,
                )
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = FontAwesomeIcons.Solid.Cog,
                    contentDescription = "Open settings",
                    tint = AppColors.TEXT_ON_DARK,
                    modifier = Modifier.size(AppConstants.FONT_TITLE_M.dp)
                )

                Spacer(modifier = Modifier.width(AppConstants.SPACING_M.dp))

                Box(modifier = Modifier.padding(top = AppConstants.SPACING_XXS.dp)) {
                    Text(
                        text = "Open settings",
                        color = AppColors.TEXT_ON_DARK,
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
                    )
                }

            }
        }
    }
}

@Composable
private fun BottomSection(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(modifier = Modifier.weight(1f)) {
//            ImageSource(
//                file = R.,
//
//                )
        }

        PostCategorySlider(
            modifier = Modifier.weight(1f)
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
        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
        modifier = Modifier.clickable { onTap() }
    )
}

@Preview
@Composable
private fun PreviewCameraAccessScreen() {
    CameraAccessScreen(onNavigationToHomeScreen = {})
}
package com.example.tiktok_clone.features.home.ui.camera.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.R
import kotlinx.coroutines.launch

@Composable
fun SnapAndTimeOption(
    hasPermission: Boolean,
    isRecording: Boolean,
    recordingMode: Boolean,
    onModeChange: (Int) -> Unit,
    onSnapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeOptionRow(
            hasPermission = hasPermission,
            onModeChange = onModeChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_xl)))
        SnapButton(
            hasPermission = hasPermission,
            isRecording = isRecording,
            onClick = onSnapClick,
            recordingMode = recordingMode,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TimeOptionRow(
    hasPermission: Boolean,
    onModeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = listOf("10m", "60s", "15s", "PHOTO")
    val pagerState = rememberPagerState(pageCount = { options.size }, initialPage = 3)
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onModeChange(pagerState.currentPage)
    }

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
    isRecording: Boolean,
    recordingMode: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment =  Alignment.Center,
        modifier = modifier
            .clickable(enabled = hasPermission) {
                onClick()
            },
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .border(
                    width = 4.dp,
                    color = if (hasPermission) Color.White else Color(0xff9C988F),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(if (isRecording) 35.dp else 65.dp) // Shrink when recording
                .clip(if (isRecording) RoundedCornerShape(8.dp) else CircleShape)
                .background(
                    if (isRecording || recordingMode) Color.Red
                    else if (hasPermission) Color.White
                    else Color(0xff9C988F)
                )
        )
    }
}
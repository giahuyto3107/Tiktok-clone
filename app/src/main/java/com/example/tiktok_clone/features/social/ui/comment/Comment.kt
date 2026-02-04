package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

// toàn bộ commnet sheet
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentSheetContent(
    viewModel: SocialViewModel = viewModel(),
    onDismiss: () -> Unit,
) {
    val comments by viewModel.comments.collectAsState()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
//    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

//    val maxHeight = when (sheetState.currentValue) {
//        SheetValue.Expanded -> screenHeight * 0.8f
//        SheetValue.PartiallyExpanded -> screenHeight * 0.5f
//        else -> screenHeight * 0.5f
//    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        dragHandle = null, //{ BottomSheetDefaults.DragHandle() },
        modifier = Modifier


    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
//                .heightIn(max = maxHeight)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 75.dp)

            ) {
                CommentHeader(
                    commentCount = comments.size,
                    Search = "Search",
                    onClose = onDismiss,
                )
                CommentList(
                    modifier = Modifier
                        .weight(1f)
                        .nestedScroll(rememberNestedScrollInteropConnection()),

                    comments = comments
                )
            }
            CommentBottomBar(
                viewModel = viewModel,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    //.windowInsetsPadding(WindowInsets.ime)
            )
        }
    }
}











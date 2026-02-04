package com.example.tiktok_clone.features.social.ui.share

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.viewModel.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareSheetContent(
    viewlModel: SocialViewModel = viewModel(),
    onDismiss: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val handleDismiss = {
        viewlModel.onAction(SocialAction.ClearSelectedFriendShare)
        onDismiss()
    }
    val selectedFriendShare by viewlModel.selectedFriendShare.collectAsState()
    val selectedFriendShareCount = selectedFriendShare.size


    ModalBottomSheet(
        onDismissRequest = handleDismiss,
        containerColor = Color.White,
        modifier = Modifier,
        sheetState = sheetState,
        dragHandle = null //{ BottomSheetDefaults.DragHandle() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ShareHeader(
                modifier = Modifier,
                onClose = handleDismiss
            )
            ShareFriendList(
                viewModel = viewlModel,
            )
            Box( //vẽ line
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.2.dp)
                    .background(Color.Gray.copy(alpha = 0.2f))
            )
            Box(
                modifier = Modifier,
                contentAlignment = Alignment.Center
            ) {
                if (selectedFriendShareCount > 0) {
                    ShareInput(
                        viewModel = viewlModel,
                        shareFriendCount = selectedFriendShareCount
                    )
                } else {
                    ShareAnotherAppList(
                        viewModel = viewlModel,
                        onClick = handleDismiss
                    )
                }
            }
        }
    }
}










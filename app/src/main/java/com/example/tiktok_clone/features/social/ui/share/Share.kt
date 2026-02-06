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
import com.example.tiktok_clone.features.social.model.ShareItem
import com.example.tiktok_clone.features.social.model.ShareSheetMode
import com.example.tiktok_clone.features.social.model.User
import com.example.tiktok_clone.features.social.viewModel.SocialAction
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareSheetContent(
    currentUserId:String,
    viewModel: SocialViewModel = viewModel(),
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val handleDismiss = {
        viewModel.onAction(SocialAction.ClearSelectedFriendShare)
        onDismiss()
    }
    val selectedFriendShare by viewModel.selectedFriendShare.collectAsState()
    val selectedFriendShareCount = selectedFriendShare.size
    val shareSheetMode by viewModel.shareSheetMode.collectAsState()
    val shareItems by viewModel.shareItems.collectAsState()

    ModalBottomSheet(
        onDismissRequest = handleDismiss,
        containerColor = Color.White,
        modifier = Modifier,
        sheetState = sheetState,
        dragHandle = null //{ BottomSheetDefaults.DragHandle() },
    ) {

        when (shareSheetMode) {
            ShareSheetMode.Default -> {
                DefaultShareSheet(
                    currentUserId = currentUserId,
                    viewModel = viewModel,
                    handleDismiss = handleDismiss,
                    selectedFriendShare = selectedFriendShare.toList(),
                    shareItems = shareItems
                )
            }
            ShareSheetMode.Report -> {
                Option(
                    viewModel = viewModel,
                    optionType = "report",
                    onDismiss = viewModel::dismissReportSheet
                )
            }
            ShareSheetMode.NotInterested -> {
                Option(
                    viewModel = viewModel,
                    optionType = "not_interested",
                    onDismiss = viewModel::dismissReportSheet
                )
            }
            ShareSheetMode.Speed -> {
                Option(
                    viewModel = viewModel,
                    optionType = "speed",
                    onDismiss = viewModel::dismissReportSheet
                )
            }
            else -> {
                DefaultShareSheet(
                    currentUserId = currentUserId,
                    viewModel = viewModel,
                    handleDismiss = handleDismiss,
                    selectedFriendShare = selectedFriendShare.toList(),
                    shareItems = shareItems
                )
            }
        }
    }
}

@Composable
fun DefaultShareSheet(
    currentUserId:String,
    viewModel: SocialViewModel,
    handleDismiss: () -> Unit,
    selectedFriendShare: List<String>,
    shareItems: List<ShareItem>
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        ShareHeader(
            modifier = Modifier,
            onClose = handleDismiss
        )
        ShareFriendList(
            currentUserId = currentUserId,
            viewModel = viewModel
        )
        Box( //vẽ line
            modifier = Modifier
                .fillMaxWidth()
                .height(0.6.dp)
                .background(Color.LightGray.copy(alpha = 0.2f))
        )
        Box(
            modifier = Modifier,
            contentAlignment = Alignment.Center
        ) {
            if (selectedFriendShare.size > 0) {
                ShareInput(
                    viewModel = viewModel,
                    selectedFriendShare = selectedFriendShare,
                )
            }
            else{
                ShareActionList(
                    items = shareItems,
                    onActionClick = viewModel::onShareActionClicked
                )
            }
        }

    }
}





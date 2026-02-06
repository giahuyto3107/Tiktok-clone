package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    currentUser: User,
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
                    currentUser = currentUser,
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
        }
    }
}

@Composable
fun DefaultShareSheet(
    currentUser: User,
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
            currentUser = currentUser,
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
            if (selectedFriendShare.isNotEmpty()) {
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





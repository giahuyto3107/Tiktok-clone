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
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.social.data.model.User
import com.example.tiktok_clone.features.social.data.model.SocialAction
import com.example.tiktok_clone.features.social.ui.SocialUiState
import com.example.tiktok_clone.features.social.ui.components.SetKeyboardOverlayMode
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareSheetContent(
    currentPost: Post,
    currentUser: User?,
    isShared: Boolean,
    socialViewModel: SocialViewModel = koinViewModel(),
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val handleDismiss = {
        socialViewModel.onAction(SocialAction.ClearSelectedFriendShare)
        onDismiss()
    }
    val uiState by socialViewModel.uiState.collectAsState()
    val selectedFriendShare =
        (uiState as? SocialUiState.Success)?.data?.selectedFriendShare ?: emptySet()
    ModalBottomSheet(
        onDismissRequest = handleDismiss,
        containerColor = Color.White,
        modifier = Modifier,
        sheetState = sheetState,
        dragHandle = null,
    ) {
        DefaultShareSheet(
            currentUser = currentUser,
            currentPost = currentPost,
            isShared = isShared,
            handleDismiss = handleDismiss,
            selectedFriendShare = selectedFriendShare.toList(),
        )
    }
}

@Composable
fun DefaultShareSheet(
    currentPost: Post,
    currentUser: User?,
    isShared: Boolean,
    socialViewModel: SocialViewModel = koinViewModel(),
    handleDismiss: () -> Unit,
    selectedFriendShare: List<String>,

    ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ShareHeader(
            modifier = Modifier
                .padding(horizontal = 8.dp),
            onClose = handleDismiss
        )
        ShareFriendList(
            socialViewModel = socialViewModel
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
                    socialViewModel = socialViewModel,
                    selectedFriendShare = selectedFriendShare,
                    currentPost = currentPost,
                    onDismiss = handleDismiss
                )
            } else {
                ShareActionList(
                    currentUser = currentUser,
                    currentPost = currentPost,
                    isShared = isShared
                )
            }
        }

    }
}





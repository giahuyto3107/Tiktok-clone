package com.example.tiktok_clone.features.notification.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import com.example.tiktok_clone.features.notification.data.model.FollowNotificationAction
import com.example.tiktok_clone.features.notification.data.model.SocialNotificationAction
import com.example.tiktok_clone.features.notification.viewModel.FollowNotificationViewModel
import com.example.tiktok_clone.features.notification.viewModel.NotificationViewModel
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.graphics.Color

@Composable
// Man thong bao (social hoac follower)
fun Notification(
    notificationType: String,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 8.dp)
            .statusBarsPadding()
    ) {
        NotificationHeader(
            notificationType = notificationType,
            onBack = onBack
        )
        
        if (notificationType == "social") {
            val vm: NotificationViewModel = koinViewModel()
            val uiState by vm.uiState.collectAsState()

            LaunchedEffect(Unit) {
                vm.onAction(SocialNotificationAction.LoadNotifications)
            }
            DisposableEffect(Unit) {
                onDispose { vm.onAction(SocialNotificationAction.MarkAllSeen) }
            }

            when (val state = uiState) {
                is NotificationUiState.Loading -> CircularProgressIndicator()
                is NotificationUiState.Error -> ErrorBanner(message = state.message)
                is NotificationUiState.Success -> NotificationList(
                    notificationType = notificationType,
                    socialNotifications = state.items,
                    followNotifications = emptyList(),
                )
            }
        } else {
            val vm: FollowNotificationViewModel = koinViewModel()
            val uiState by vm.uiState.collectAsState()

            LaunchedEffect(Unit) {
                vm.onAction(FollowNotificationAction.SetScreenOpen(true))
                vm.onAction(FollowNotificationAction.LoadNotifications)
            }
            DisposableEffect(Unit) {
                onDispose {
                    vm.onAction(FollowNotificationAction.SetScreenOpen(false))
                    vm.onAction(FollowNotificationAction.MarkAllSeen)
                }
            }
            when (val state = uiState) {
                is NotificationUiState.Loading -> CircularProgressIndicator()
                is NotificationUiState.Error -> ErrorBanner(message = state.message)
                is NotificationUiState.Success -> NotificationList(
                    notificationType = notificationType,
                    socialNotifications = emptyList(),
                    followNotifications = state.items,
                )
            }
        }
    }
}

@Composable
// Hien thi loi dang banner
private fun ErrorBanner(message: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFEBEE))
            .padding(12.dp),
    ) {
        Text(text = message, color = Color(0xFFB00020))
    }
}


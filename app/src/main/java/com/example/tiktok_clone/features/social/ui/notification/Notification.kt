package com.example.tiktok_clone.features.social.ui.notification
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.features.social.viewModel.FollowNotificationViewModel
import com.example.tiktok_clone.features.social.viewModel.NotificationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Notification(
    notificationType: String,
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp, vertical = 8.dp)
    ) {
        NotificationHeader(
            notificationType = notificationType,
            onBack = onBack
        )

        if (notificationType == "social") {
            val vm: NotificationViewModel = koinViewModel()
            val notifications by vm.notifications.collectAsState()

            LaunchedEffect(Unit) {
                vm.loadNotifications()
            }
            DisposableEffect(Unit) {
                onDispose { vm.markAllSeen() }
            }
            NotificationList(
                notificationType = "social",
                socialNotifications = notifications,
                followNotifications = emptyList()
            )
        } else {
            val vm: FollowNotificationViewModel = koinViewModel()
            val notifications by vm.notifications.collectAsState()

            LaunchedEffect(Unit) {
                vm.setScreenOpen(true)
                vm.loadNotifications()
            }
            DisposableEffect(Unit) {
                onDispose {
                    vm.setScreenOpen(false)
                    vm.markAllSeen()
                }
            }

            NotificationList(
                notificationType = notificationType,
                socialNotifications = emptyList(),
                followNotifications = notifications
            )
        }
    }
}

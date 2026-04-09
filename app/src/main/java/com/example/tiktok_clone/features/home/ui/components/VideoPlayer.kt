package com.example.tiktok_clone.features.home.ui.components

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage

/**
 * A full-screen video player composable for the TikTok-style feed.
 *
 * @param exoPlayer   Shared ExoPlayer instance (owned by the parent screen)
 * @param mediaUrl    The video URL to play
 * @param thumbnailUrl Thumbnail shown while the video is not yet ready
 * @param isCurrentPage True if this page is the currently visible one in the pager
 */
@OptIn(UnstableApi::class)
@Composable
// Video player cho feed (exoPlayer shared)
fun VideoPlayer(
    exoPlayer: ExoPlayer,
    mediaUrl: String,
    thumbnailUrl: String?,
    isCurrentPage: Boolean,
    modifier: Modifier = Modifier
) {
    var isVideoReady by remember { mutableStateOf(false) }
    var userPaused by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    // When this page becomes the current page, load the media and start playing
    LaunchedEffect(isCurrentPage, mediaUrl, userPaused) {
        if (isCurrentPage && mediaUrl.isNotBlank()) {
            val currentItem = exoPlayer.currentMediaItem
            // Avoid reloading the same URL
            if (currentItem?.localConfiguration?.uri?.toString() != mediaUrl) {
                isVideoReady = false
                exoPlayer.setMediaItem(MediaItem.fromUri(mediaUrl))
                exoPlayer.prepare()
            }
            exoPlayer.playWhenReady = !userPaused
        } else {
            // Not current page or no media: pause
            if (exoPlayer.currentMediaItem?.localConfiguration?.uri?.toString() == mediaUrl) {
                exoPlayer.playWhenReady = false
            }
        }
    }

    // Lifecycle Observer (Respects userPaused)
    DisposableEffect(lifecycleOwner, isCurrentPage, userPaused) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.playWhenReady = false
                Lifecycle.Event.ON_RESUME -> {
                    if (isCurrentPage && !userPaused) exoPlayer.playWhenReady = true
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    // Listen for player readiness to hide thumbnail
    DisposableEffect(exoPlayer, isCurrentPage) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY && isCurrentPage) {
                    isVideoReady = true
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose { exoPlayer.removeListener(listener) }
    }

    // Pause/resume on lifecycle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.playWhenReady = false
                Lifecycle.Event.ON_RESUME -> {
                    if (isCurrentPage) exoPlayer.playWhenReady = true
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectTapGestures {
                    userPaused = !userPaused
                }
            }
    ) {
        // Video layer — only render PlayerView for the current page
        if (isCurrentPage) {
            AndroidView(
                factory = { ctx ->
                    PlayerView(ctx).apply {
                        player = exoPlayer
                        useController = false
                        resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Show an icon when paused so the user knows why it stopped
        if (userPaused && isCurrentPage) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Paused",
                modifier = Modifier.align(Alignment.Center).size(64.dp),
                tint = Color.White.copy(alpha = 0.5f)
            )
        }

        // Thumbnail overlay — shown when video is not yet ready or not the current page
        if (!isVideoReady || !isCurrentPage) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = "Video Thumbnail",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

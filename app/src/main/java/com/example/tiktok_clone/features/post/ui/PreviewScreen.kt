package com.example.tiktok_clone.features.post.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.post.data.model.PostType
import com.example.tiktok_clone.features.post.ui.components.BackButton
import com.example.tiktok_clone.features.post.ui.components.NextButton

@OptIn(UnstableApi::class)
@Composable
fun PreviewScreen(
    mediaUri: String,
    postType: String,
    onBack: () -> Unit,
    onNext: (String, String) -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(color = colorResource(R.color.black))
                .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.radius_m)))
        ) {
            if (postType == PostType.IMAGE.name) {
                AsyncImage(
                    model = mediaUri,
                    contentDescription = "Preview Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            } else if (postType == PostType.VIDEO.name) {
                val exoPlayer = remember {
                    ExoPlayer.Builder(context).build().apply {
                        setMediaItem(MediaItem.fromUri(mediaUri))
                        prepare()
                        playWhenReady = true
                        repeatMode = Player.REPEAT_MODE_ALL // Loop the video preview
                    }
                }

                // Important: Release the player when leaving the preview screen!
                DisposableEffect(Unit) {
                    onDispose { exoPlayer.release() }
                }

                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = false // Hides the ugly default seekbar
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            BackButton(
                onClick = onBack,
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                color = colorResource(R.color.white)
            )
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_m)))

        NextButton(
            onNext = { onNext(mediaUri, postType) },
            modifier = Modifier.padding(
                bottom = dimensionResource(R.dimen.spacing_l),
                start = dimensionResource(R.dimen.spacing_m),
                end = dimensionResource(R.dimen.spacing_m)
            )
        )
    }
}

@Preview
@Composable
fun PreviewPreviewScreen(
    mediaUri: String = "",
    postType: String = "",
    onBack: () -> Unit = {},
    onNext: (String, String) -> Unit = { _, _ -> },
) {
    PreviewScreen(mediaUri, postType, onBack, onNext)
}
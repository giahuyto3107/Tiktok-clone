package com.example.tiktok_clone.features.post.ui

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.example.tiktok_clone.features.post.ui.components.PostButton

@Composable
fun PrePostScreen(
    mediaUri: String,
    postType: String,
    onBack: () -> Unit,
    onPost: () -> Unit,
) {
    var titleText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white))
    ) {
        BackButton(
            onClick = onBack,
            icon = Icons.AutoMirrored.Filled.ArrowBackIos,
            color = colorResource(R.color.black)
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.spacing_m)))
        ImageScrollerSection(
            postType = postType,
            mediaUri = mediaUri,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.spacing_m))
        )
        TitleSection(
            value = titleText,
            onValueChange = { titleText = it },
            modifier = Modifier.fillMaxWidth()
        )
        DescriptionSection(
            value = descriptionText,
            onValueChange = { descriptionText = it },
            modifier = Modifier.weight(1f).fillMaxWidth()
        )
        PostButton(onPost = onPost)
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun ImageScrollerSection(
    postType: String,
    mediaUri: String,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 100.dp)
                .clip(shape = RoundedCornerShape(
                    dimensionResource(R.dimen.radius_m))
                )
//                .background(colorResource(R.color.black))
        ) {
            if (postType == PostType.IMAGE.name) {
                AsyncImage(
                    model = mediaUri,
                    contentDescription = "Preview Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth,
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
        }
    }
}

@Composable
private fun TitleSection(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                "Add a catchy title",
                color = colorResource(R.color.text_secondary),
                style = MaterialTheme.typography.labelLarge
            )
      },
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        ),
    )
}

@Composable
private fun DescriptionSection(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                "Writing a long description can help get 3x more views on average",
                style = MaterialTheme.typography.labelMedium,
                color = colorResource(R.color.text_secondary)
            )
        },
        modifier = modifier,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent
        )
    )
}

@Preview
@Composable
fun PreviewPrePostScreen(
    onPost: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    PrePostScreen(
        onBack = onBack,
        onPost = onPost,
        postType = "",
        mediaUri = ""
    )
}


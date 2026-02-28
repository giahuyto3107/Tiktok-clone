package com.example.tiktok_clone.features.post.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.R
import com.example.tiktok_clone.features.post.ui.components.BackButton

@Composable
fun PreviewScreen(
    mediaUri: String,
    postType: String,
    onBack: () -> Unit
) {
    Spacer(modifier = Modifier.height(150.dp))
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(

            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .background(color = colorResource(R.color.text_secondary))
                .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.radius_m)))
        ) {
            BackButton(onClick = onBack)
        }
        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = colorResource(R.color.red),
            ),
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Next")
        }
    }
}

@Preview
@Composable
fun PreviewPreviewScreen(
    mediaUri: String = "",
    postType: String = "",
    onBack: () -> Unit = {}
) {
    PreviewScreen(mediaUri, postType, onBack)
}
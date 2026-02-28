package com.example.tiktok_clone.features.post.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import com.example.tiktok_clone.R

@Composable
fun BackButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier.padding(
            top = dimensionResource(R.dimen.spacing_m),
            start = dimensionResource(R.dimen.spacing_m),
        )
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Cancel",
            tint = colorResource(R.color.text_on_dark),
            modifier = Modifier
                .align(Alignment.TopStart)
                .size(dimensionResource(R.dimen.font_title_m))
                .clickable { onClick() }
        )
    }
}
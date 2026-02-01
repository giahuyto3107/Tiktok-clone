package com.example.tiktok_clone.features.social.ui.shareComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel

@Composable
fun ShareAnotherAppList(
    viewModel: SocialViewModel = viewModel(),
) {
    val apps by viewModel.apps.collectAsState()
    val shareAcctions by viewModel.shareAcctions.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(apps.size) { app ->
                ShareAnotherApp(
                    icon = apps[app].icon,
                    iconSize = apps[app].iconSize,
                    appIconByLetter = apps[app].appIconByLetter,
                    fontSize = apps[app].fontSize,
                    appName = apps[app].appName,
                    tint = apps[app].tint,
                    backgroundColor = apps[app].backgroundColor,
                    onClick = apps[app].onClick,
                    modifier = apps[app].modifier,
                )

            }
        }
        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(shareAcctions.size) { shareAcction ->
                ShareAnotherApp(
                    icon = shareAcctions[shareAcction].icon,
                    iconSize = shareAcctions[shareAcction].iconSize,
                    appName = shareAcctions[shareAcction].appName,
                    tint = shareAcctions[shareAcction].tint,
                    backgroundColor = shareAcctions[shareAcction].backgroundColor,
                    onClick = shareAcctions[shareAcction].onClick,
                )
            }
        }
    }
}
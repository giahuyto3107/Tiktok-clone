package com.example.tiktok_clone.features.shop.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiktok_clone.core.utils.AppColors

@Composable
fun ShopScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🛍️ Shop",
                style = MaterialTheme.typography.headlineMedium,
                color = AppColors.TEXT_ON_DARK,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "Discover amazing products",
                style = MaterialTheme.typography.bodyLarge,
                color = AppColors.TEXT_SECONDARY
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    ShopScreen()
}

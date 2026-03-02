package com.example.tiktok_clone.features.home.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.tiktok_clone.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VideoDescriptionSection(
    userName: String,
    caption: String?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = Modifier
            .padding(bottom = 4.dp)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = userName,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        InlineShowMore(
            text = caption ?: "",
        )
    }
}

@Composable
fun InlineShowMore(text: String) {
    var isExpanded by remember { mutableStateOf(false) }
    var hasOverflow by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = text,
            fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.font_m).toSp() },
            style = MaterialTheme.typography.labelSmall,
            color = Color.White,
            maxLines = if (isExpanded) Int.MAX_VALUE else 3,
            onTextLayout = { textLayoutResult ->
                // Check if the text actually exceeds the 3-line limit
                if (textLayoutResult.hasVisualOverflow || textLayoutResult.lineCount > 3) {
                    hasOverflow = true
                }
            },
        )

        if (hasOverflow) {
            Text(
                text = if (isExpanded) "Show less" else "Show more",
                fontWeight = FontWeight.Bold,
                fontSize = with(LocalDensity.current) { dimensionResource(R.dimen.font_m).toSp() },
                color = colorResource(R.color.text_secondary),
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.spacing_xxs))
                    .clickable { isExpanded = !isExpanded } //
            )
        }
    }
}
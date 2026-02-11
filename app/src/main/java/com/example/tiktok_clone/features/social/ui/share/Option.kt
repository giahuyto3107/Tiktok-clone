package com.example.tiktok_clone.features.social.ui.share

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.social.ui.components.ReasonOptionHeader
import com.example.tiktok_clone.features.social.ui.components.ReasonOptionItem
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Option(
    viewModel: SocialViewModel = viewModel(),
    optionType: String,
    onDismiss: () -> Unit,
) {
    val reportOptions by viewModel.reportOptions.collectAsState()
    val notInterestedOptions by viewModel.notInterestedOptions.collectAsState()
    val speedOptions by viewModel.speedOptions.collectAsState()
    var isSelectecd by remember { mutableStateOf<Int?>(null) }

    val options = when(optionType) {
        "report" -> reportOptions
        "not_interested" -> notInterestedOptions
        "speed" -> speedOptions
        else -> emptyList()
    }
    val title = when(optionType) {
        "report" -> "Báo cáo"
        "not_interested" -> "Không quan tâm"
        "speed" -> "Tốc độ phát lại"
        else -> ""
    }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,

    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = Modifier,
        containerColor = Color.White,
        dragHandle = null

    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ReasonOptionHeader(
                title = title,
                modifier = Modifier,
                onClose = onDismiss
            )
            LazyColumn(
                modifier = Modifier
                    .padding(start = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(options.size) { reason ->
                    ReasonOptionItem(
                        reasonOption = options[reason].reasonOption,
                        isSelected = isSelectecd == options[reason].reasonId,
                        onClick = {
                            isSelectecd = options[reason].reasonId
                        }
                    )
                }
            }
        }
    }
}


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
import com.example.tiktok_clone.features.social.model.ReasonOption
import com.example.tiktok_clone.features.social.ui.components.ReasonOptionHeader
import com.example.tiktok_clone.features.social.ui.components.ReasonOptionItem
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReasonOption(
    viewModel: SocialViewModel = viewModel(),
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    typeOfReasonOption: String?
) {
    val speedOptions by viewModel.speedOptions.collectAsState()
    val reportOptions by viewModel.reportOptions.collectAsState()
    val notInterestedOptions by viewModel.notInterestedOptions.collectAsState()

    val reasonOptions: List<ReasonOption> = when(typeOfReasonOption) {
        "report" -> reportOptions
        "not_interested" -> notInterestedOptions
        "speed" -> speedOptions
        else -> emptyList()
    }
    var selectedReasonId by remember { mutableStateOf<Int?>(null) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        containerColor = Color.White,
        dragHandle = null

    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .then(modifier),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ReasonOptionHeader(
                typeOfReasonOption = typeOfReasonOption,
                modifier = Modifier,
                onClose = onDismiss
            )
            LazyColumn(
                modifier = Modifier
                    .padding(start = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(reasonOptions.size) { reason ->
                    ReasonOptionItem(
                        reasonOption = reasonOptions[reason].reasonOption,
                        isSelected = selectedReasonId == reasonOptions[reason].reasonId,
                        onClick = {
                            selectedReasonId = reasonOptions[reason].reasonId

                        }
                    )

                }
            }
        }
    }
}


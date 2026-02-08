package com.example.tiktok_clone.features.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiktok_clone.features.search.navigation.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onBack: () -> Unit = {},
    onNavigateToResult: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showAll by remember { mutableStateOf(false) }

    // 👉 Điều hướng sang màn kết quả
    LaunchedEffect(state.navigateToResult) {
        if (state.navigateToResult) {
            viewModel.onResultNavigated()
            onNavigateToResult(state.query)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {

        // ================= HEADER =================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onBack() }
            )

            Spacer(Modifier.width(8.dp))

            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                placeholder = {
                    Text(
                        text = "Tìm kiếm",
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp),
                singleLine = true,
                textStyle = TextStyle(fontSize = 14.sp),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = "Tìm kiếm",
                color = Color.Red,
                modifier = Modifier.clickable {
                    viewModel.onSearchSubmit(state.query)
                }
            )
        }

        Divider()

        // ================= GỢI Ý KHI GÕ =================
        if (state.suggestions.isNotEmpty()) {

            LazyColumn {
                items(state.suggestions) { text ->
                    SuggestionItem(
                        text = text,
                        onClick = {
                            viewModel.onSearchSubmit(text)
                        }
                    )
                }
            }

        } else {

            // ================= CHƯA GÕ: LỊCH SỬ + GỢI Ý =================
            LazyColumn {

                val recentList =
                    if (showAll) state.recentSearches
                    else state.recentSearches.take(5)

                // -------- LỊCH SỬ TÌM KIẾM --------
                items(recentList) { text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onSearchSubmit(text)
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = Color.Gray
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            text = text,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                viewModel.removeRecent(text)
                            }
                        )
                    }
                }

                // -------- XEM THÊM --------
                if (!showAll && state.recentSearches.size > 5) {
                    item {
                        Text(
                            text = "Xem thêm",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showAll = true }
                                .padding(12.dp),
                            color = Color.Gray
                        )
                    }
                }

                // -------- BẠN CÓ THỂ THÍCH --------
                item {
                    Text(
                        text = "Bạn có thể thích",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 14.sp
                    )
                }

                items(state.youMayLike) { text ->
                    Text(
                        text = text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onSearchSubmit(text)
                            }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}
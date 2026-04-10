package com.example.tiktok_clone.features.search.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.search.SearchViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBack: () -> Unit = {},
    onNavigateToResult: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    var showAllRecent by remember { mutableStateOf(false) }

    LaunchedEffect(state.navigateToResult) {
        if (state.navigateToResult) {
            viewModel.onResultNavigated()
            onNavigateToResult()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Quay lại")
            }
            OutlinedTextField(
                value = state.query,
                onValueChange = viewModel::onQueryChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                placeholder = { Text("Tìm kiếm", fontSize = 14.sp) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(22.dp),
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                shape = MaterialTheme.shapes.large,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color(0xFFF5F5F5),
                ),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                "Tìm kiếm",
                color = Color(0xFFE53935),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable { viewModel.onSearchSubmit(state.query) }
                    .padding(end = 4.dp, top = 8.dp, bottom = 8.dp),
            )
        }

        state.error?.let { err ->
            Text(
                err,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )
        }

        if (state.isLoading) {
            LinearProgressIndicator(Modifier.fillMaxWidth())
        }

        HorizontalDivider(color = Color(0xFFEEEEEE))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (state.query.isNotBlank()) {
                item {
                    Text(
                        "Đề xuất",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        fontSize = 13.sp,
                        color = Color.Gray,
                    )
                }
                if (state.isSuggestLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(28.dp))
                        }
                    }
                } else if (state.suggestions.isEmpty()) {
                    item {
                        Text(
                            "Không có gợi ý",
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            fontSize = 13.sp,
                            color = Color.Gray,
                        )
                    }
                } else {
                    itemsIndexed(state.suggestions, key = { i, _ -> "sg_$i" }) { _, text ->
                        SuggestionItem(
                            text = text,
                            isFromDatabase = true,
                            onClick = { viewModel.onSearchSubmit(text) },
                        )
                    }
                }
                item {
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                }
                item {
                    Text(
                        "Lịch sử",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        fontSize = 13.sp,
                        color = Color.Gray,
                    )
                }
            }

            val recent = if (showAllRecent) state.recentSearches else state.recentSearches.take(5)
            itemsIndexed(recent, key = { i, _ -> "rc_$i" }) { _, text ->
                SuggestionItem(
                    text = text,
                    onClick = { viewModel.onSearchSubmit(text) },
                    onRemove = { viewModel.removeRecent(text) },
                )
            }

            if (!showAllRecent && state.recentSearches.size > 5) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showAllRecent = true }
                            .padding(12.dp),
                    ) {
                        Text("Xem thêm", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Bạn có thể thích",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(
                        onClick = { viewModel.refreshDiscover() },
                        enabled = !state.isDiscoverLoading,
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Làm mới")
                    }
                }
            }

            if (state.isDiscoverLoading && state.discoverItems.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(Modifier.size(28.dp))
                    }
                }
            }

            itemsIndexed(state.discoverItems, key = { i, it -> "d_${it.keyword}_$i" }) { _, item ->
                DiscoverListRow(item = item) {
                    viewModel.onSearchSubmit(item.keyword)
                }
            }

            item {
                Text(
                    "Nội dung tìm kiếm thịnh hành",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp, bottom = 8.dp),
                )
            }
            item {
                Text(
                    "LIVE thịnh hành · Xu hướng theo dữ liệu search_history & video hot",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 24.dp),
                )
            }
        }
    }
}

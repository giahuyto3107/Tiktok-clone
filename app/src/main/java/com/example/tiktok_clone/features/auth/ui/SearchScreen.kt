    package com.example.tiktok_clone.features.auth.ui

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
    import com.example.tiktok_clone.features.auth.navigation.SearchViewModel


    @Composable
    fun SearchScreen(
        viewModel: SearchViewModel = viewModel(),
        onBack: () -> Unit = {}
    ) {
        val state by viewModel.uiState.collectAsState()
        var showAll by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(Color.White)
        ) {

            // 🔝 HEADER SEARCH
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            )

            {

                // ⬅ Nút quay về
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBack() }
                )

                Spacer(Modifier.width(8.dp))

                // 🔍 Ô tìm kiếm
                OutlinedTextField(
                    value = state.query,
                    onValueChange = viewModel::onQueryChange,
                    placeholder = { Text("hdpe là ngon luôn", fontSize = 12.sp ) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, null,modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "Camera",modifier = Modifier.size(18.dp)
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp)
                        .heightIn(min = 30.dp),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 14.sp),
                    shape = RoundedCornerShape(8.dp)

                )

                Spacer(Modifier.width(8.dp))

                // 🔴 Nút Tìm kiếm
                Text(
                    text = "Tìm kiếm",
                    color = Color.Red,
                    modifier = Modifier.clickable {
                        viewModel.onSearchSubmit(state.query)
                    }
                )
            }


            Divider()


            // 📌 Gợi ý hoặc lịch sử
            if (state.suggestions.isNotEmpty()) {
                LazyColumn {
                    items(state.suggestions) {
                        SuggestionItem(it) {
                            viewModel.onSearchSubmit(it)
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {

                    // 👉 CHỈ HIỆN 5 MỤC NẾU CHƯA XEM THÊM
                    val recentList =
                        if (showAll) state.recentSearches
                        else state.recentSearches.take(5)

                    items(recentList) { text ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = text,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove",
                                tint = Color.Gray,
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable {
                                        viewModel.removeRecent(text)
                                    }
                            )
                        }
                    }

                    // 👉 NÚT XEM THÊM
                    if (!showAll && state.recentSearches.size > 5) {
                        item {
                            Text(
                                text = "Xem thêm",
                                fontSize = 13.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                                    .clickable { showAll = true }
                            )
                        }
                    }

                    // 👉 BẠN CÓ THỂ THÍCH
                    item {
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Bạn có thể thích",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }

                    items(state.youMayLike) {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }

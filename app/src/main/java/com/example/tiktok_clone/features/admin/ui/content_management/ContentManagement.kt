package com.example.tiktok_clone.features.admin.ui.content_management

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.features.admin.ui.TopBarSection
import com.example.tiktok_clone.features.admin.ui.dashboard.BorderGray
import com.example.tiktok_clone.features.admin.ui.dashboard.TextBlack
import com.example.tiktok_clone.features.admin.ui.dashboard.TextGray
import com.example.tiktok_clone.features.profile.ui.TikTokRed

// Data Model cho Content
data class VideoContent(
    val id: String,
    val title: String,
    val thumbnail: String = "",
    val uploader: String,
    val views: String,
    val likes: String,
    val date: String,
    val status: String // "Public", "Private", "Removed", "Pending"
)

@Composable
fun ContentManagementScreen() {
    var selectedVideo by remember { mutableStateOf<VideoContent?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(40.dp)
    ) {
        if (selectedVideo == null) {
            ContentDashboard(onVideoClick = { selectedVideo = it })
        } else {
            // Màn hình duyệt video (Giống ảnh phải)
            ContentDetailReview(
                video = selectedVideo!!,
                onBack = { selectedVideo = null }
            )
        }
    }
}

// --- CONTENT LIST VIEW (Ảnh Trái) ---
@Composable
fun ContentDashboard(onVideoClick: (VideoContent) -> Unit) {
    TopBarSection(title = "Video Content Management")
    Spacer(modifier = Modifier.height(24.dp))

    // Filter Chips
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Color.Black)) { Text("Filter") }
        FilterChipFake("Date")
        FilterChipFake("Status")
        FilterChipFake("Hashtags")
    }

    Spacer(modifier = Modifier.height(32.dp))

    // Content Table
    ContentTableSection(onVideoClick)
}

@Composable
fun FilterChipFake(text: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFE5E7EB), RoundedCornerShape(4.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { }
    ) {
        Text(text, fontSize = 13.sp, color = TextBlack)
    }
}

@Composable
fun ContentTableSection(onVideoClick: (VideoContent) -> Unit) {
    val videos = listOf(
        VideoContent("1", "Hình ảnh 1 & Anh vui!", "", "Nhật Tiến", "1Tr", "500N", "27/11/2026", "Public"),
        VideoContent("2", "Hình ảnh 2 & Anh Buồn!", "", "Huy Tô", "2Tr", "800N", "20/11/2026", "Private"),
        VideoContent("3", "Hình ảnh 3 & Anh vui!", "", "Tiến Trần", "1N", "500", "10/10/2026", "Removed")
    )

    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White).border(1.dp, BorderGray).padding(20.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Content Table", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text("Search content", fontSize = 13.sp)
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(40.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = BorderGray,
                    focusedBorderColor = Color.Black
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Table Header
        Row(modifier = Modifier.fillMaxWidth().background(Color(0xFF4A89DC)).padding(12.dp)) {
            TableHeaderCell("Thumbnail & Title", 2f)
            TableHeaderCell("Uploader", 1f)
            TableHeaderCell("Stats (view/like)", 1.5f)
            TableHeaderCell("Date", 1f)
            TableHeaderCell("Status", 1f)
        }

        // Rows
        videos.forEach { video ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onVideoClick(video) }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Thumbnail & Title
                Row(modifier = Modifier.weight(2f), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(50.dp, 30.dp).background(Color.Gray)) // Fake Thumb
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(video.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
                TableCell(video.uploader, 1f)
                TableCell("view: ${video.views}/like:${video.likes}", 1.5f)
                TableCell(video.date, 1f)
                Box(modifier = Modifier.weight(1f)) {
                    Text(video.status, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Icon(Icons.Default.MoreVert, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
            Divider(color = BorderGray)
        }
    }
}

// --- CONTENT DETAIL REVIEW (Ảnh Phải) ---
@Composable
fun ContentDetailReview(video: VideoContent, onBack: () -> Unit) {
    Column {
        // Header
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { onBack() }) {
            Icon(Icons.Default.ArrowBack, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Back", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))
        CenterTitle("Video chờ kiểm duyệt") // Tiêu đề giữa
        Spacer(modifier = Modifier.height(40.dp))

        // Layout chia đôi: List bên trái - Player bên phải
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp) // Chiều cao cố định cho khu vực duyệt
                .border(1.dp, BorderGray)
        ) {
            // 1. CỘT TRÁI: DANH SÁCH VIDEO CHỜ
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .border(width = 1.dp, color = BorderGray)
            ) {
                val pendingVideos = (1..7).map { "Video $it: Lý do...." }
                pendingVideos.forEachIndexed { index, title ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .background(if (index == 1) Color(0xFFE0E0E0) else Color.White) // Giả lập đang chọn video 2
                            .border(0.5.dp, BorderGray)
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(title, fontSize = 13.sp)
                    }
                }
            }

            // 2. CỘT PHẢI: PLAYER & ACTIONS
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Video Player Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color(0xFFEEEEEE)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.PlayArrow, null, modifier = Modifier.size(48.dp), tint = Color.Gray)
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text("Danh sách thống kê report có biểu đồ tròn ở đây", fontSize = 12.sp, color = TextGray)

                Spacer(modifier = Modifier.weight(1f))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { /* Bỏ qua */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)), // Green
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text("Bỏ qua")
                    }

                    Button(
                        onClick = { /* Gỡ bỏ */ },
                        colors = ButtonDefaults.buttonColors(containerColor = TikTokRed), // Red
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.width(120.dp)
                    ) {
                        Text("Gỡ bỏ")
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.TableHeaderCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        color = Color.White // Header màu trắng trên nền xanh
    )
}

@Composable
fun RowScope.TableCell(text: String, weight: Float) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        fontSize = 13.sp,
        color = TextBlack
    )
}

@Composable
fun CenterTitle(title: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Text(title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
    }
}
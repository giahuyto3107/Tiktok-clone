package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.More
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiktok_clone.R.dimen
import com.example.tiktok_clone.features.social.ui.components.CommentItem
import com.example.tiktok_clone.features.social.ui.components.formatCount
import com.example.tiktok_clone.ui.theme.TextPrimaryBlue
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.SortAmountDownAlt

// header bar comment
@Composable
fun CommentHeader(
    commentCount: Long,
    search: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSort by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .then(modifier),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
            ) {
                Text(
                    text = "Tìm kiếm: ",
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(dimen.font_s).value.sp,
                    color = Color.Gray
                )
                Text(
                    text = search,
                    fontWeight = FontWeight.Bold,
                    fontSize = dimensionResource(dimen.font_s).value.sp,
                    color = TextPrimaryBlue
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = TextPrimaryBlue,
                    contentDescription = "search",
                    modifier = Modifier.size(16.dp)
                )
            }
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(25.dp),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Box(
                    modifier = Modifier
                        .clickable {
                            isSort = !isSort
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    CommentItem(
                        icon = Icons.AutoMirrored.Filled.Notes,
                        onClick = {
                            isSort = !isSort
                        },
                        text = "Sắp xếp",
                        showText = false,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(20.dp)
                    )
                    CommentItem(
                        icon = Icons.AutoMirrored.Filled.ArrowRightAlt,
                        onClick = {
                            isSort = !isSort
                        },
                        text = "Sắp xếp",
                        showText = false,
                        tint = Color.Black,
                        modifier = Modifier
                            .rotate(90f)
                            .offset(y = - 10.dp)
                            .size(22.dp)

                    )
                }
                CommentItem(
                    icon = Icons.Outlined.Close,
                    onClick = onClose,
                    text = "Đóng",
                    tint = Color.Black,
                    showText = false,
                    modifier = Modifier
                        .size(24.dp)

                )
            }

        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${formatCount(commentCount)} bình luận",
                modifier = Modifier.padding(4.dp),
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(dimen.font_s).value.sp,
                color = Color.Black
            )
        }
    }
}

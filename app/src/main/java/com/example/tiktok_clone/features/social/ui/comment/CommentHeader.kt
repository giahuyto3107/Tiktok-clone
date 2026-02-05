package com.example.tiktok_clone.features.social.ui.comment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import compose.icons.fontawesomeicons.solid.Times

// header bar comment
@Composable
fun CommentHeader(
    commentCount: Long,
    Search: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSort by remember { mutableStateOf(false) }

    val inLineContent = mapOf(
        "searchIcon" to InlineTextContent(
            Placeholder(
                width = 16.sp,
                height = 16.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign
                    .AboveBaseline //căn đáy của vật thể nằm khớp trên đường chân chữ (baseline) để không bị lún xuống phần đuôi của các ký tự có nét móc dưới.
            )
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = TextPrimaryBlue,
                contentDescription = "search"
            )
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .then(modifier),
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Tìm kiếm: ")
                    withStyle(
                        style = SpanStyle(color = TextPrimaryBlue)
                    ) {
                        append(Search)
                    }
                    appendInlineContent("searchIcon", "[icon]")
                },
                inlineContent = inLineContent,
                fontWeight = FontWeight.Bold,
                fontSize = dimensionResource(dimen.font_xl).value.sp,
                modifier = Modifier.padding(4.dp)
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Bottom),

                ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    CommentItem(
                        icon = FontAwesomeIcons.Solid.SortAmountDownAlt,
                        onClick = {
                            isSort = !isSort
                        },
                        text = "Sắp xếp",
                        showText = false,
                        tint = Color.Black,
                        modifier = Modifier
                            .size(16.dp)
                            .graphicsLayer(
                                scaleX = -1f
                            )
                    )
                    CommentItem(
                        icon = Icons.Outlined.Close,
                        onClick = onClose,
                        text = "Đóng",
                        tint = Color.Black,
                        showText = false,
                        modifier = Modifier
                            .padding(4.dp)
                            .size(24.dp)

                    )
                }
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
                fontSize = 18.sp,
                color = Color.Black
            )
        }
    }
}

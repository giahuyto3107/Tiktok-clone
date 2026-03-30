package com.example.tiktok_clone.features.social.ui.components

import android.content.Context
import android.net.Uri
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.post.data.model.PostType
import java.io.File

data class PickedCommentImage(
    val uri: Uri,
    val file: File,
)

data class ShareMessagePayload(
    val type: String,
    val imageUri: String,
    val content: String?,
)

fun resolvePickedCommentImage(
    context: Context,
    uri: Uri,
): PickedCommentImage? {
    val mimeType = context.contentResolver.getType(uri)
    if (mimeType?.startsWith("image") != true) return null

    val extension = when (mimeType) {
        "image/png" -> ".png"
        "image/gif" -> ".gif"
        "image/webp" -> ".webp"
        else -> ".jpg"
    }

    val file = uriToTempFile(context, uri, extension, "comment_img_") ?: return null
    return PickedCommentImage(uri = uri, file = file)
}

fun buildShareMessagePayload(
    post: Post,
    messageText: String,
): ShareMessagePayload {
    val shareType = when (post.type) {
        PostType.IMAGE -> "IMAGE"
        PostType.VIDEO -> "VIDEO"
    }
    val normalizedMediaUrl = post.mediaUrl.substringAfter("/uploads", post.mediaUrl)
        .let { mediaPath ->
            if (mediaPath == post.mediaUrl) post.mediaUrl else "/uploads$mediaPath"
        }
    val shareText = messageText.trim().ifBlank { post.caption.ifBlank { null } }

    return ShareMessagePayload(
        type = shareType,
        imageUri = normalizedMediaUrl,
        content = shareText,
    )
}

fun uriToTempFile(
    context: Context,
    uri: Uri,
    extension: String,
    prefix: String,
): File? {
    return try {
        val input = context.contentResolver.openInputStream(uri) ?: return null
        val file = File.createTempFile(prefix, extension, context.cacheDir)
        input.use { stream ->
            file.outputStream().use { out -> stream.copyTo(out) }
        }
        file
    } catch (_: Exception) {
        null
    }
}

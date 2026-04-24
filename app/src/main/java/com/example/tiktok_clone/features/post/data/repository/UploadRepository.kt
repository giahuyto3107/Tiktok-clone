package com.example.tiktok_clone.features.post.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.tiktok_clone.features.post.data.model.Post
import com.example.tiktok_clone.features.post.data.model.PostType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UploadRepository(
    private val context: Context,
    private val apiService: PostApiService
) {
    private companion object {
        private const val TAG = "UploadRepository"
    }

    suspend fun createPost(
        uri: Uri = Uri.EMPTY,
        caption: String = "",
        type: PostType,
        userId: String = "0"
    ): Result<Boolean> {
        Log.d(TAG, "createPost called with URI: $uri, type: $type")
        return if (type == PostType.VIDEO) {
            uploadVideo(
                uri = uri,
                caption = caption,
                userId = userId
            )
        } else {
            uploadImage(
                uri = uri,
                caption = caption,
                userId = userId
            )
        }
    }

    suspend fun uploadImage(
        uri: Uri,
        caption: String,
        userId: String
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            kotlin.runCatching {
                Log.d(TAG, "Bắt đầu upload cho URI: $uri")

                // 1. Xử lý File (I/O task)
                val filePart = createFilePartFromUri(uri, "image.jpg")
                    ?: throw IllegalArgumentException("Không thể đọc dữ liệu từ URI")

                // 2. Chuẩn bị dữ liệu Multipart
                val captionPart = caption.toRequestBody("text/plain".toMediaTypeOrNull())
                val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())

                // 3. Gọi API (Network task)
                val response = apiService.uploadImage(filePart, captionPart, userIdPart)

                // 4. Kiểm tra phản hồi
                if (response.isSuccessful || response.code() == 200 || response.code() == 201) {
                    Log.d(TAG, "Upload image thành công: ${response.code()}")
                    true
                } else {
                    val errorMsg = "Upload thất bại: ${response.code()} ${response.message()}"
                    Log.e(TAG, errorMsg)
                    throw Exception(errorMsg)
                }
            }.onFailure { e ->
                // Tự động catch mọi Exception (bao gồm cả IOException khi đọc file)
                Log.e(TAG, "Lỗi trong quá trình upload: ${e.message}", e)
            }
        }

    private suspend fun uploadVideo(
        uri: Uri,
        caption: String,
        userId: String
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            runCatching {
                val filePart = createFilePartFromUri(uri, "video.mp4")
                    ?: throw IllegalArgumentException("Could not read video from URI")
                val captionPart = caption.toRequestBody("text/plain".toMediaTypeOrNull())
                val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiService.uploadVideo(filePart, captionPart, userIdPart)
                if (response.isSuccessful) true
                else {
                    val request = response.raw().request
                    Log.e(
                        TAG,
                        "Video upload failed: code=${response.code()} method=${request.method} url=${request.url}"
                    )
                    throw Exception("Upload failed: ${response.code()}")
                }
            }
        }

    /**
     * Copies content from [uri] into a temp file and creates a [MultipartBody.Part].
     * @param defaultFileName used when URI has no useful filename
     */
    private fun createFilePartFromUri(uri: Uri, defaultFileName: String): MultipartBody.Part? {
        Log.d(TAG, "createFilePartFromUri called with URI: $uri, defaultFileName: $defaultFileName")
        var stream: InputStream?
        try {
            stream = context.contentResolver.openInputStream(uri) ?: run {
                Log.e(TAG, "Failed to open input stream for URI: $uri")
                return null
            }
            val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
            val fileName = uri.lastPathSegment?.takeIf { it.contains('.') } ?: defaultFileName
            val tempFile = File.createTempFile(
                "upload_${System.currentTimeMillis()}_${uri.hashCode()}",
                "_$fileName"
            )
            Log.d(TAG, "Created temp file: ${tempFile.absolutePath}")

            return try {
                FileOutputStream(tempFile).use { out ->
                    stream.use { it.copyTo(out) }
                }
                Log.d(TAG, "File copied successfully, size: ${tempFile.length()}")
                val body = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
                MultipartBody.Part.createFormData("file", fileName, body)
            } catch (e: Exception) {
                Log.e(TAG, "Error creating file part", e)
                tempFile.delete()
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error in createFilePartFromUri", e)
            return null
        }
    }

    suspend fun getPosts(
        page: Int = 1,
        pageSize: Int = 20
    ): List<Post> {
        return apiService.getPosts(page = page, pageSize = pageSize).posts
    }
}
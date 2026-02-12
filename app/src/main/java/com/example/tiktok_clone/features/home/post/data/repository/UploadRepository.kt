package com.example.tiktok_clone.features.home.post.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.tiktok_clone.features.home.post.data.model.PostType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class UploadRepository(
    private val context: Context,
    private val apiService: PostApiService
) {
    private companion object {
        private const val TAG = "UploadRepository"
    }

    suspend fun createPost(
        uri: Uri,
        description: String,
        type: PostType
    ): Result<Boolean> {
        return if (type == PostType.VIDEO) {
            uploadVideo(uri, description)
        } else {
            uploadImage(uri, description)
        }
    }

    private suspend fun uploadImage(uri: Uri, description: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val filePart = createFilePartFromUri(uri, "image.jpg")
                    ?: return@withContext Result.failure(IllegalArgumentException("Could not read image from URI"))
                val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
                val response = apiService.uploadImage(filePart, descPart)
                if (response.isSuccessful) Result.success(true)
                else {
                    val request = response.raw().request
                    Log.e(TAG, "Image upload failed: code=${response.code()} method=${request.method} url=${request.url}")
                    Result.failure(Exception("Upload failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private suspend fun uploadVideo(uri: Uri, description: String): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val filePart = createFilePartFromUri(uri, "video.mp4")
                    ?: return@withContext Result.failure(IllegalArgumentException("Could not read video from URI"))
                val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
                val response = apiService.uploadVideo(filePart, descPart)
                if (response.isSuccessful) Result.success(true)
                else {
                    val request = response.raw().request
                    Log.e(TAG, "Video upload failed: code=${response.code()} method=${request.method} url=${request.url}")
                    Result.failure(Exception("Upload failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Copies content from [uri] into a temp file and creates a [MultipartBody.Part].
     * @param defaultFileName used when URI has no useful filename
     */
    private fun createFilePartFromUri(uri: Uri, defaultFileName: String): MultipartBody.Part? {
        val stream = context.contentResolver.openInputStream(uri) ?: return null
        val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
        val fileName = uri.lastPathSegment?.takeIf { it.contains('.') } ?: defaultFileName
        val tempFile = File.createTempFile("upload_", "_$fileName").apply {
            deleteOnExit()
        }
        return try {
            FileOutputStream(tempFile).use { out ->
                stream.use { it.copyTo(out) }
            }
            val body = tempFile.asRequestBody(mimeType.toMediaTypeOrNull())
            MultipartBody.Part.createFormData("file", fileName, body)
        } catch (e: Exception) {
            tempFile.delete()
            null
        }
    }
}


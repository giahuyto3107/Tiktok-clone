package com.example.tiktok_clone.features.post.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
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
        description: String = "",
        type: PostType,
        userId: String = "0"
    ): Result<Boolean> {
        Log.d(TAG, "createPost called with URI: $uri, type: $type")
        return if (type == PostType.VIDEO) {
            uploadVideo(
                uri = uri,
                description = description,
                userId = userId
            )
        } else {
            uploadImage(
                uri = uri,
                description = description,
                userId = userId
            )
        }
    }

    private suspend fun uploadImage(
        uri: Uri,
        description: String,
        userId: String
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "uploadImage starting for URI: $uri")
                val filePart = createFilePartFromUri(uri, "image.jpg")
                    ?: return@withContext Result.failure(IllegalArgumentException("Could not read image from URI"))
                Log.d(TAG, "filePart created successfully")
                val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
                val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiService.uploadImage(filePart, descPart, userIdPart)
                Log.d(TAG, "uploadImage response: code=${response.code()}, successful=${response.isSuccessful}")
                if (response.isSuccessful) Result.success(true)
                else {
                    val request = response.raw().request
                    Log.e(TAG, "Image upload failed: code=${response.code()} method=${request.method} url=${request.url}")
                    Result.failure(Exception("Upload failed: ${response.code()}"))
                }
            } catch (e: Exception) {
                Log.e(TAG, "uploadImage exception", e)
                Result.failure(e)
            }
        }

    private suspend fun uploadVideo(
        uri: Uri,
        description: String,
        userId: String
    ): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val filePart = createFilePartFromUri(uri, "video.mp4")
                    ?: return@withContext Result.failure(IllegalArgumentException("Could not read video from URI"))
                val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
                val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = apiService.uploadVideo(filePart, descPart, userIdPart)
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
        Log.d(TAG, "createFilePartFromUri called with URI: $uri, defaultFileName: $defaultFileName")
        var stream: InputStream? = null
        try {
            stream = context.contentResolver.openInputStream(uri) ?: run {
                Log.e(TAG, "Failed to open input stream for URI: $uri")
                return null
            }
            val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"
            val fileName = uri.lastPathSegment?.takeIf { it.contains('.') } ?: defaultFileName
            val tempFile = File.createTempFile("upload_${System.currentTimeMillis()}_${uri.hashCode()}", "_$fileName")
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
}
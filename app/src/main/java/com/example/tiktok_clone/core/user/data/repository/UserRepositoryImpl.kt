package com.example.tiktok_clone.core.user.data.repository

import android.content.Context
import android.net.Uri
import com.example.tiktok_clone.core.config.ApiConfig
import com.example.tiktok_clone.core.user.domain.model.UserProfile
import com.example.tiktok_clone.core.user.domain.repository.UserRepository
import com.example.tiktok_clone.features.user.data.UserApiService
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class UserRepositoryImpl(
    private val firebaseAuth: FirebaseAuth,
    private val context: Context,
    private val userApiService: UserApiService
) : UserRepository {
    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

    override fun getCurrentUser(): UserProfile? {
        val user = firebaseAuth.currentUser
        return user?.let {
            UserProfile(
                id = it.uid,
                email = it.email,
                displayName = it.displayName,
                avtPhotoUrl = it.photoUrl?.toString() ?: "null"
            )
        }
    }

    override fun updateProfile(displayName: String, photoUrl: String, onComplete: (Boolean) -> Unit) {
        val user = firebaseAuth.currentUser
        if (user == null) {
            onComplete(false)
            return
        }
        
        val profileUpdates = com.google.firebase.auth.userProfileChangeRequest {
            this.displayName = displayName
            if (photoUrl.isNotBlank() && photoUrl != "null") {
                photoUri = Uri.parse(photoUrl)
            }
        }
        
        user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Reload user to force Firebase to sync the updated profile (photoUrl, displayName)
                    // back to the local cached user object
                    user.reload().addOnCompleteListener {
                        onComplete(true)
                    }
                } else {
                    onComplete(false)
                }
            }
    }

    override suspend fun uploadAvatarLocal(uri: Uri): String? {
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val contentResolver = context.contentResolver
                val inputStream = contentResolver.openInputStream(uri) ?: return@withContext null
                val tempFile = File(context.cacheDir, "temp_avatar_${System.currentTimeMillis()}.jpg")
                
                FileOutputStream(tempFile).use { output ->
                    inputStream.copyTo(output)
                }
                
                val requestFile = tempFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val filePart = MultipartBody.Part.createFormData("file", tempFile.name, requestFile)
                
                val response = userApiService.uploadAvatar(filePart)
                tempFile.delete() // Clean up
                
                if (response.status == "success") {
                    // Ensure base URL does not have trailing slash while relative path has leading slash to avoid //
                    val base = ApiConfig.getBaseUrl().removeSuffix("/")
                    base + response.avatarUrl
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}


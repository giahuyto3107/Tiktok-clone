package com.example.tiktok_clone.features.auth.ui


import android.content.Context
import com.facebook.AccessToken
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class FacebookAuthUiHelper {
    private val auth = FirebaseAuth.getInstance()

    suspend fun signInWithFacebookCredential(accessToken: AccessToken): Result<AuthResult> {
        return try {
            val credential = FacebookAuthProvider.getCredential(accessToken.token)
            val authResult = auth.signInWithCredential(credential).await()
            Result.success(authResult)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
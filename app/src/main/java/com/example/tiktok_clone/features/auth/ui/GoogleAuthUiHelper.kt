package com.example.tiktok_clone.features.auth.ui
import android.content.Context
import android.util.Log
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.tiktok_clone.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import com.example.tiktok_clone.features.auth.data.UserData

class GoogleAuthUiHelper(
    private val context: Context,
    private val firebaseAuth: FirebaseAuth = Firebase.auth
) {
    companion object {
        const val TAG = "GoogleAuthUiHelper"
    }

    suspend fun signInGoogle(): Result<UserData> = try {
        val credentialManager = CredentialManager.create(context)

        // Instantiate a Google sign-in request
        val credentialOptions = GetGoogleIdOption.Builder()
            // Your server's client ID, not your Android client ID.
            .setServerClientId(context.getString(R.string.default_web_client_id))
            // Only show accounts previously used to sign in.
            .setFilterByAuthorizedAccounts(false)
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(credentialOptions)
            .build()

        val result = credentialManager.getCredential(context, request)
        val credential = result.credential

        handleSignIn(credential)
    } catch (e: Exception) {
        Log.e(TAG, "Google sign-in failed: ", e)
        Result.failure(e)
    }

    private suspend fun handleSignIn(credential: Credential?): Result<UserData> {
        // Check if credential is of type Google ID
        return if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w(TAG, "Credential is not of type Google ID!")
            Result.failure(Exception("Invalid Google credential"))
        }
    }

    private suspend fun firebaseAuthWithGoogle(idToken: String): Result<UserData> =
        suspendCancellableCoroutine { cont ->
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result.user
                        firebaseUser
                        if (firebaseUser != null) {
                            cont.resume(
                                Result.success(
                                    value = UserData(
                                        userId = firebaseUser.uid,
                                        username = firebaseUser.displayName,
                                        profilePictureUrl = firebaseUser.photoUrl?.toString()
                                    )
                                )
                            )
                        } else {
                            cont.resume(Result.failure(Exception("Firebase user is null")))
                        }
                    } else {
                        cont.resume(value=Result.failure(exception = task.exception ?: Exception("Firebase sign-in failed")))
                    }
                }
                .addOnCanceledListener {
                    cont.resume(value=Result.failure(Exception("Firebase sign-in canceled")))
                }
        }
}
package com.example.tiktok_clone.features.auth.data

import android.content.Context
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.CredentialManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

// Sử dụng Hilt để inject Context (hoặc truyền tay nếu không dùng DI)
class AuthRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // Hàm này chứa đoạn code của bạn
    fun buildGoogleIdOption(): GetGoogleIdOption {
        return GetGoogleIdOption.Builder()
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setFilterByAuthorizedAccounts(false) // Thường set false để user chọn lại tk nếu muốn
            .setAutoSelectEnabled(true)
            .build()
    }

    // Hàm tạo request hoàn chỉnh
    fun getCredentialRequest(): GetCredentialRequest {
        val googleIdOption = buildGoogleIdOption()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }

    // Hàm thực thi việc đăng nhập (Gọi từ ViewModel)
    suspend fun signInWithGoogle(activityContext: Context): Result<GetCredentialResponse> {
        return try {
            val credentialManager = CredentialManager.create(activityContext)
            val request = getCredentialRequest()

            // Gọi Credential Manager (Cần Activity Context để hiện BottomSheet)
            val result = credentialManager.getCredential(
                context = activityContext,
                request = request
            )
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
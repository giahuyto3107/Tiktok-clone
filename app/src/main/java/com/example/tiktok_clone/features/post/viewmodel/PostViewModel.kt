package com.example.tiktok_clone.features.post.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.core.user.domain.repository.UserRepository
import com.example.tiktok_clone.features.post.data.model.PostType
import com.example.tiktok_clone.features.post.data.repository.UploadRepository
import com.example.tiktok_clone.features.post.ui.UploadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostViewModel(
    private val repository: UploadRepository,
    private val currentUser: UserRepository
) : ViewModel() {
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState = _uploadState.asStateFlow()

    fun upload(uri: Uri, description: String, type: PostType) {
        viewModelScope.launch {


            _uploadState.value = UploadState.Loading

            val result = repository.createPost(
                uri = uri,
                description = description,
                type = type,
                userId = currentUser.getCurrentUser()?.id ?: "0"
            )

            result.fold(
                onSuccess = { isSuccess ->
                    if (isSuccess) {
                        _uploadState.value = UploadState.Success
                    } else {
                        _uploadState.value = UploadState.Error("Upload failed at server")
                    }
                },
                onFailure = { exception ->
                    _uploadState.value = UploadState.Error(
                        exception.message ?: "Unknown error"
                    )
                }
            )
        }
    }

    fun resetUploadState() {
        _uploadState.value = UploadState.Idle
    }
}
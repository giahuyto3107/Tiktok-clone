package com.example.tiktok_clone.features.home.post.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.home.post.data.model.PostType
import com.example.tiktok_clone.features.home.post.data.repository.UploadRepository
import com.example.tiktok_clone.features.home.post.ui.UploadState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PostViewModel(private val repository: UploadRepository) : ViewModel() {
    private val _uploadState = MutableStateFlow<UploadState>(UploadState.Idle)
    val uploadState = _uploadState.asStateFlow()

    fun upload(uri: Uri, description: String, type: PostType) {
        viewModelScope.launch {
            _uploadState.value = UploadState.Loading

            val result = repository.createPost(uri, description, type)

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
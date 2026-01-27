package com.example.tiktok_clone.features.social.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SocialViewModel : ViewModel() {
    private val _posts = MutableStateFlow(FakePostData.posts)
    val posts = _posts.asStateFlow()

}
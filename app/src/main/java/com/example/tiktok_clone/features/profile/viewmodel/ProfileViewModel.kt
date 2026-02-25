package com.example.tiktok_clone.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tiktok_clone.core.user.domain.model.UserProfile
import com.example.tiktok_clone.core.user.domain.repository.UserRepository

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    fun getProfileData(): UserProfile? {
        return userRepository.getCurrentUser()
    }

    fun checkStatus(): Boolean {
        return userRepository.isUserLoggedIn()
    }
}
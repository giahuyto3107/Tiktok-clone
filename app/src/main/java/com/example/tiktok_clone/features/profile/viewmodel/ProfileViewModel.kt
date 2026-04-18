package com.example.tiktok_clone.features.profile.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tiktok_clone.core.user.domain.model.UserProfile
import com.example.tiktok_clone.core.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileData = MutableStateFlow<UserProfile?>(null)
    val profileData: StateFlow<UserProfile?> = _profileData.asStateFlow()

    init {
        refreshProfile()
    }

    fun getProfileData(): UserProfile? {
        return userRepository.getCurrentUser()
    }

    fun refreshProfile() {
        _profileData.value = userRepository.getCurrentUser()
    }

    fun checkStatus(): Boolean {
        return userRepository.isUserLoggedIn()
    }

    fun updateProfile(displayName: String, photoUrl: String, onComplete: (Boolean) -> Unit) {
        userRepository.updateProfile(displayName, photoUrl) { success ->
            if (success) {
                // Sync the updated user into the StateFlow so observers re-render
                _profileData.value = userRepository.getCurrentUser()
            }
            onComplete(success)
        }
    }

    suspend fun uploadAvatarLocal(uri: android.net.Uri): String? {
        return userRepository.uploadAvatarLocal(uri)
    }

    fun logout() {
        userRepository.logout()
        _profileData.value = null
    }
}

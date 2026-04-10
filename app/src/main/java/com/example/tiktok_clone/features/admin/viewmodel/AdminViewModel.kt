package com.example.tiktok_clone.features.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiktok_clone.features.admin.data.AdminDashboardStats
import com.example.tiktok_clone.features.admin.data.AdminRepository
import com.example.tiktok_clone.features.admin.data.AdminUserItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(private val repository: AdminRepository) : ViewModel() {

    private val _stats = MutableStateFlow<AdminDashboardStats?>(null)
    val stats: StateFlow<AdminDashboardStats?> = _stats.asStateFlow()

    private val _users = MutableStateFlow<List<AdminUserItem>>(emptyList())
    val users: StateFlow<List<AdminUserItem>> = _users.asStateFlow()

    private val _totalPages = MutableStateFlow(1)
    val totalPages: StateFlow<Int> = _totalPages.asStateFlow()

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadDashboardStats()
        loadUsers(1)
    }

    fun loadDashboardStats() {
        viewModelScope.launch {
            try {
                _stats.value = repository.getDashboardStats()
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load stats"
            }
        }
    }

    fun loadUsers(page: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val res = repository.getUsers(page = page)
                _users.value = res.items
                _totalPages.value = res.total_pages
                _currentPage.value = res.page
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load users"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUserStatus(userId: String, isBanned: Boolean? = null, isVerified: Boolean? = null) {
        viewModelScope.launch {
            try {
                repository.updateUserStatus(userId, isBanned = isBanned, isVerified = isVerified)
                loadUsers(_currentPage.value)
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to update user status"
            }
        }
    }
}

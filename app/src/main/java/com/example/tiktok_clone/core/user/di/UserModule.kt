package com.example.tiktok_clone.core.user.di

import com.example.tiktok_clone.core.user.data.repository.UserRepositoryImpl
import com.example.tiktok_clone.core.user.domain.repository.UserRepository
import com.example.tiktok_clone.features.profile.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

import org.koin.android.ext.koin.androidContext

val userModule = module {
    // Provide Firebase Instance
    single { FirebaseAuth.getInstance() }

    // Provide Repository
    single<UserRepository> { UserRepositoryImpl(get(), androidContext(), get()) }

    // Provide ProfileViewModel
    viewModel { ProfileViewModel(get()) }
}
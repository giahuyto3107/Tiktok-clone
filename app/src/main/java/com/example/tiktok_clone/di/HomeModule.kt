package com.example.tiktok_clone.di

import com.example.tiktok_clone.features.home.home.viewmodel.HomeViewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel(get()) }
    // Other home related dependencies
}

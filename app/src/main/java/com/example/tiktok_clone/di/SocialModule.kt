package com.example.tiktok_clone.di

import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val socialModule = module {
    viewModel { SocialViewModel() }
    // Other social related dependencies
}

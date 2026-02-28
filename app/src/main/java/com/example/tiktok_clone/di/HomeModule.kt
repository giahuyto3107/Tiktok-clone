package com.example.tiktok_clone.di

import com.example.tiktok_clone.features.home.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel(get(), get()) }
    // Other home related dependencies
}

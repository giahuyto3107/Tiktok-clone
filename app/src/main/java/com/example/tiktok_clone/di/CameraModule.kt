package com.example.tiktok_clone.di

import com.example.tiktok_clone.features.home.camera.viewmodel.CameraViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cameraModule = module {
    viewModel { CameraViewModel() }
    // Other camera related dependencies
}

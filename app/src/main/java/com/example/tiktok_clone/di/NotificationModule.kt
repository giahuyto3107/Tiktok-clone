package com.example.tiktok_clone.di

import com.example.tiktok_clone.features.social.data.FollowNotificationApiService
import com.example.tiktok_clone.features.social.data.FollowNotificationRepository
import com.example.tiktok_clone.features.social.data.NotificationApiService
import com.example.tiktok_clone.features.social.data.NotificationRepository
import com.example.tiktok_clone.features.notification.viewModel.FollowNotificationViewModel
import com.example.tiktok_clone.features.notification.viewModel.NotificationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val notificationModule = module {
    // Social notifications
    single<NotificationApiService> { get<Retrofit>().create(NotificationApiService::class.java) }
    single { NotificationRepository(get()) }
    viewModel { NotificationViewModel(get()) }

    // Follow notifications
    single<FollowNotificationApiService> {
        get<Retrofit>().create(FollowNotificationApiService::class.java)
    }
    single { FollowNotificationRepository(get()) }
    viewModel { FollowNotificationViewModel(get()) }
}


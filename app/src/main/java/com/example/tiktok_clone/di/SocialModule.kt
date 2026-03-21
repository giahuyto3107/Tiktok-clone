package com.example.tiktok_clone.di

import com.example.tiktok_clone.features.social.data.NotificationApiService
import com.example.tiktok_clone.features.social.data.NotificationRepository
import com.example.tiktok_clone.features.social.data.SocialApiService
import com.example.tiktok_clone.features.social.data.SocialRepository
import com.example.tiktok_clone.features.social.data.FollowNotificationApiService
import com.example.tiktok_clone.features.social.data.FollowNotificationRepository
import com.example.tiktok_clone.features.social.viewModel.FollowNotificationViewModel
import com.example.tiktok_clone.features.social.viewModel.NotificationViewModel
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.features.user.data.UserRepository
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val socialModule = module {
    single<SocialApiService> { get<Retrofit>().create(SocialApiService::class.java) }
    single { SocialRepository(get()) }
    viewModel {
        SocialViewModel(
            socialRepository = get(),
            userRepository = get<UserRepository>(),
            okHttpClient = get(),
        )
    }

    single<NotificationApiService> { get<Retrofit>().create(NotificationApiService::class.java) }
    single { NotificationRepository(get()) }
    viewModel { NotificationViewModel(get(), get()) }

    // Follow notifications (user tab)
    single<FollowNotificationApiService> { get<Retrofit>().create(FollowNotificationApiService::class.java) }
    single { FollowNotificationRepository(get()) }
    viewModel { FollowNotificationViewModel(get(), get()) }
}

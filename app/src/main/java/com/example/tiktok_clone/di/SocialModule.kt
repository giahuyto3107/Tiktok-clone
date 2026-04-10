package com.example.tiktok_clone.di

import com.example.tiktok_clone.features.inbox.data.InboxRepository
import com.example.tiktok_clone.features.social.data.SocialApiService
import com.example.tiktok_clone.features.social.data.SocialRepository
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
            inboxRepository = get<InboxRepository>(),
        )
    }
}

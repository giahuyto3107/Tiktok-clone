package com.example.tiktok_clone.di

import android.content.Context
import android.content.SharedPreferences
import com.example.tiktok_clone.BuildConfig
import com.example.tiktok_clone.core.config.ApiConfig
import com.example.tiktok_clone.core.network.RealtimeWebSocketClient
import com.example.tiktok_clone.features.inbox.data.InboxApiService
import com.example.tiktok_clone.features.inbox.data.InboxRepository
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.post.data.repository.PostApiService
import com.example.tiktok_clone.features.post.data.repository.UploadRepository
import com.example.tiktok_clone.features.post.viewmodel.PostViewModel
import com.example.tiktok_clone.features.social.data.SocialApiService
import com.example.tiktok_clone.features.social.data.SocialRepository
import com.example.tiktok_clone.features.social.viewModel.SocialViewModel
import com.example.tiktok_clone.features.user.data.UserApiService
import com.example.tiktok_clone.features.user.data.UserRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences("tiktok_prefs", Context.MODE_PRIVATE)
    }

    // OkHttpClient singleton — dùng cho cả Retrofit và WebSocket
    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
        }
        val auth = FirebaseAuth.getInstance()
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val user = auth.currentUser
                val token: String? = if (user != null) {
                    runBlocking {
                        try {
                            user.getIdToken(false).await().token
                        } catch (_: Exception) {
                            try { user.getIdToken(true).await().token } catch (_: Exception) { null }
                        }
                    }
                } else null

                val request = if (!token.isNullOrBlank()) {
                    chain.request().newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else chain.request()

                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()
    }

    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(ApiConfig.getBaseUrl())
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<PostApiService> { get<Retrofit>().create(PostApiService::class.java) }
    single { UploadRepository(androidContext(), get()) }

    single<UserApiService> { get<Retrofit>().create(UserApiService::class.java) }
    single { UserRepository(get()) }

    single<SocialApiService> { get<Retrofit>().create(SocialApiService::class.java) }
    single { SocialRepository(get()) }
    viewModel { SocialViewModel(get(), get(), get(), get()) }

    single<InboxApiService> { get<Retrofit>().create(InboxApiService::class.java) }
    single { InboxRepository(get()) }
    viewModel { InboxViewModel(get(), get()) }

    single { PostViewModel(get(), get()) }
}

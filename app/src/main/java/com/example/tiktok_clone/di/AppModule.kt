package com.example.tiktok_clone.di

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.abedelazizshe.lightcompressorlibrary.BuildConfig
import com.example.tiktok_clone.core.config.ApiConfig
import com.example.tiktok_clone.features.inbox.data.InboxApiService
import com.example.tiktok_clone.features.inbox.data.InboxRepository
import com.example.tiktok_clone.features.inbox.viewmodel.InboxViewModel
import com.example.tiktok_clone.features.post.data.repository.PostApiService
import com.example.tiktok_clone.features.post.data.repository.UploadRepository
import com.example.tiktok_clone.features.post.viewmodel.PostViewModel
import com.example.tiktok_clone.features.search.SearchViewModel
import com.example.tiktok_clone.features.search.api.SearchApiService
import com.example.tiktok_clone.features.search.data.SearchRepository
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
    // SharedPreferences example
    single<SharedPreferences> {
        androidContext().getSharedPreferences("tiktok_prefs", Context.MODE_PRIVATE)
    }
    single { SocialRepository(get()) }
    viewModel { SocialViewModel(get(), get()) }
    single {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        // ❌ Code cũ (chỉ log, không gắn Authorization)
        // val okHttpClient = OkHttpClient.Builder()
        //     .addInterceptor(logging)
        //     .build()
        //
        // Retrofit.Builder()
        //     .baseUrl(ApiConfig.getBaseUrl())  // Use centralized config
        //     .client(okHttpClient)
        //     .addConverterFactory(GsonConverterFactory.create())
        //     .build()

        // ✅ Code mới: gắn Firebase ID token vào Authorization header
        val auth = FirebaseAuth.getInstance()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val user = auth.currentUser
                val token: String? = if (user != null) {
                    runBlocking {
                        try {
                            // ✅ false = dùng cache, Firebase tự refresh khi gần hết hạn
                            user.getIdToken(false).await().token
                        } catch (e: Exception) {
                            try {
                                // Fallback: force refresh nếu cache fail
                                user.getIdToken(true).await().token
                            } catch (e2: Exception) {
                                null
                            }
                        }
                    }
                } else null

                val request = if (!token.isNullOrBlank()) {
                    chain.request().newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else {
                    chain.request()
                }
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(ApiConfig.getBaseUrl())  // Use centralized config
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single<PostApiService> {
        get<Retrofit>().create(PostApiService::class.java)
    }

    single {
        UploadRepository(androidContext(), get())
    }

    single<UserApiService> {
        get<Retrofit>().create(UserApiService::class.java)
    }

    single {
        UserRepository(get())
    }

    single<SocialApiService> {
        get<Retrofit>().create(SocialApiService::class.java)
    }

    single<InboxApiService> {
        get<Retrofit>().create(InboxApiService::class.java)
    }

    single<SearchApiService> {
        get<Retrofit>().create(SearchApiService::class.java)
    }

    single {
        SearchRepository(get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    single {
        InboxRepository(get())
    }

    single {
        PostViewModel(get(), get())
    }
    viewModel {
        InboxViewModel(get())
    }
    
    // Add other app-level singletons here:
    // single { NetworkUtils() }
    // single { DatabaseHelper(get()) }
    // single { ApiClient() }
}


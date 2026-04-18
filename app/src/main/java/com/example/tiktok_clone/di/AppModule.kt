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

    // OkHttpClient singleton cho Retrofit
    single<OkHttpClient> {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
                    else HttpLoggingInterceptor.Level.NONE
        }
        val auth = FirebaseAuth.getInstance()
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val user = auth.currentUser
                Log.d("DI_DEBUG", "Interceptor triggered. Current User: ${user?.email ?: "Not Logged In"}")
                val token: String? = if (user != null) {
                    runBlocking {
                        try {
                            val t = user.getIdToken(false).await().token  // ✅ assign first
                            Log.d("DI_DEBUG", "Token fetched (cache): $t")  // ✅ then log
                            t  // ✅ return value from try block
                        } catch (_: Exception) {
                            Log.w("DI_DEBUG", "Cache token failed, retrying with forceRefresh...")
                            try { user.getIdToken(true).await().token } catch (_: Exception) { null }
                        }
                    }
                } else null

                val request = if (!token.isNullOrBlank()) {
                    Log.d("DI_DEBUG", "Injecting Bearer Token into Headers...")
                    chain.request().newBuilder()
                        .header("Authorization", "Bearer $token")
                        .build()
                } else chain.request()

                Log.d("DI_DEBUG", "Final Request Headers: ${request.headers}")
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

    single<SearchApiService> {
        get<Retrofit>().create(SearchApiService::class.java)
    }
    single<InboxApiService> { get<Retrofit>().create(InboxApiService::class.java) }
    single { InboxRepository(get()) }
    viewModel { InboxViewModel(get()) }

    single {
        SearchRepository(get(), get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel { PostViewModel(get(), get()) }
}


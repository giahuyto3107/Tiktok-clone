package com.example.tiktok_clone.di

import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Example of how to use Koin for dependency injection
 * 
 * Usage examples:
 * 
 * 1. In Composable:
 * ```kotlin
 * @Composable
 * fun MyScreen() {
 *     val viewModel: MyViewModel = koinViewModel()
 *     // Use viewModel
 * }
 * ```
 * 
 * 2. In regular class:
 * ```kotlin
 * class MyRepository(private val context: Context) {
 *     // Use injected context
 * }
 * ```
 * 
 * 3. Get by lazy:
 * ```kotlin
 * class MyClass {
 *     private val repository: MyRepository by inject()
 * }
 * ```
 */


//object KoinExamples {
//
//    fun initExamples() {
//        // Example module with more dependencies
//        val exampleModule = module {
//            // Singleton
//            single { ApiService() }
//
//            // Factory (new instance each time)
//            factory { SomeRepository(get()) }
//
//            // ViewModel
//            viewModel { ExampleViewModel(get(), get()) }
//
//            // Context dependency
//            androidContext()
//        }
//
//        // Start Koin with modules
//        startKoin {
//            androidLogger()
//            androidContext()
//            modules(exampleModule)
//        }
//    }
//}

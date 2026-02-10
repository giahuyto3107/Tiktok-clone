# Dependency Injection Setup with Koin

This project uses **Koin** for dependency injection alongside Hilt.

## Architecture

```
TikTokApplication (Application)
├── Initializes Koin DI container
├── Loads all DI modules
└── Provides Android context

MainActivity (Activity)
├── Uses Koin for ViewModels via koinViewModel()
├── Handles UI setup and window configuration
└── No DI logic here

DI Modules
├── appModule - Core app dependencies
├── homeModule - Home feature ViewModels
├── cameraModule - Camera feature ViewModels
└── socialModule - Social feature ViewModels
```

## Usage Examples

### In Composables
```kotlin
@Composable
fun MyScreen() {
    // Get ViewModel from Koin
    val viewModel: MyViewModel = koinViewModel()
    
    // Use viewModel
    val uiState by viewModel.uiState.collectAsState()
}
```

### In Regular Classes
```kotlin
class MyRepository {
    // Inject dependency by lazy
    private val apiService: ApiService by inject()
    
    // Or get immediately
    private val context: Context = get()
}
```

### Modules Structure
```kotlin
val myModule = module {
    // ViewModel (factory - new instance each time)
    viewModel { MyViewModel(get()) }
    
    // Singleton (same instance everywhere)
    single { ApiService() }
    
    // Factory with parameters
    factory { (id: String) -> MyRepository(id, get()) }
}
```

## Migration Status

✅ **Koin Setup Complete** - All ViewModels use Koin  
✅ **Application Class** - TikTokApplication handles DI initialization  
✅ **MainActivity Clean** - Only UI and window setup  
✅ **Hilt Still Available** - Can use both DI systems  

## Next Steps

1. Gradually migrate remaining Hilt usage to Koin
2. Add more dependencies to modules as needed
3. Consider removing Hilt once fully migrated
4. Add testing configuration for Koin

## Benefits

- 🚀 **Lightweight** - Smaller footprint than Hilt
- 🔧 **Simple Setup** - Easy to understand and debug
- 🧩 **Compose Native** - Built-in Compose support
- 🧪 **Flexible** - Runtime dependency resolution
- 🧪 **Testable** - Easy mocking for unit tests

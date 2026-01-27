# Tiktok-clone
Đề tài xây dựng mạng xã hội video ngắn (clone TikTok)
```text
app/
├── build.gradle.kts           # Cấu hình build của app
├── proguard-rules.pro         # Quy tắc ProGuard
│
└── src/
    ├── main/
    │   ├── AndroidManifest.xml
    │   │
    │   ├── java/com/example/tiktok_clone/
    │   │   ├── MainActivity.kt           # Activity chính
    │   │   │
    │   │   ├── core/                     # Core functionality
    │   │   │   ├── navigation/
    │   │   │   │   └── AppNavigation.kt  # Điều hướng app
    │   │   │   │
    │   │   │   └── utils/                # Tiện ích chung
    │   │   │       ├── AppColors.kt      # Định nghĩa màu sắc
    │   │   │       ├── AppConstants.kt   # Hằng số ứng dụng
    │   │   │       └── AppStrings.kt     # Chuỗi văn bản
    │   │   │
    │   │   ├── features/                 # Các tính năng
    │   │   │   │
    │   │   │   ├── auth/                 # Xác thực người dùng
    │   │   │   │   ├── model/
    │   │   │   │   │   └── AuthUiState.kt      # State của UI Auth
    │   │   │   │   ├── navigation/
    │   │   │   │   │   └── AuthNavGraph.kt     # Navigation graph Auth
    │   │   │   │   └── ui/
    │   │   │   │       └── AuthViewModel.kt    # ViewModel cho Auth
    │   │   │   │
    │   │   │   └── home/                 # Màn hình chính
    │   │   │       ├── helper/
    │   │   │       │   └── GetLastGalleryImageUri.kt  # Helper lấy ảnh
    │   │   │       │
    │   │   │       └── ui/
    │   │   │           ├── camera/       # Chức năng camera
    │   │   │           │   ├── CameraAccessScreen.kt
    │   │   │           │   └── Helper.kt
    │   │   │           │
    │   │   │           └── home/         # Màn hình Home
    │   │   │               ├── HomeScreen.kt
    │   │   │               └── Helper.kt
    │   │   │
    │   │   └── ui/                       # UI Theme
    │   │       └── theme/
    │   │           ├── Color.kt          # Định nghĩa màu
    │   │           ├── Theme.kt          # Theme ứng dụng
    │   │           └── Type.kt           # Typography
    │   │
    │   └── res/                          # Resources
    │       ├── drawable/                 # Hình ảnh & icons
    │       │   ├── camera_button.png
    │       │   ├── apartment.jpg
    │       │   ├── cat.jpg
    │       │   ├── cherry_flower.jpg
    │       │   ├── city_post_office.jpg
    │       │   ├── independence_palace.jpg
    │       │   ├── river.jpg
    │       │   ├── road.jpg
    │       │   ├── street.jpg
    │       │   ├── uni.jpg
    │       │   ├── video.jpg
    │       │   └── ic_launcher_*.xml
    │       │
    │       ├── mipmap-*/                 # App icons (các độ phân giải)
    │       ├── values/                   # Giá trị resources
    │       │   ├── colors.xml
    │       │   ├── strings.xml
    │       │   └── themes.xml
    │       └── xml/                      # XML configs
    │
    ├── androidTest/                      # Android tests
    └── test/                             # Unit tests

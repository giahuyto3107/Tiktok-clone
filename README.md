# 🎵 TikTok Clone - Short Video Social Network

A modern Android application built with Jetpack Compose that replicates the core features of TikTok, allowing users to create, share, and discover short-form videos.

## 📱 Features

### 🏠 Home Screen
- **Vertical Video Feed**: Swipe vertically to browse through videos
- **Interactive Elements**: Like, comment, save, and share buttons
- **Video Information**: User profiles and video descriptions
- **Bottom Navigation**: Home, Shop, Camera, Inbox, and Profile sections

### 📸 Camera & Video Creation
- **Live Camera Preview**: Real-time camera feed using CameraX
- **Permission Management**: Smart handling of camera and microphone permissions
- **Recording Options**: Multiple duration settings (10m, 60s, 15s, PHOTO)
- **Post Categories**: Choose between POST, CREATE, and LIVE modes

### 🎨 User Interface
- **Modern Design**: Clean, intuitive interface following Material Design 3
- **Dark Theme**: Optimized for video viewing experience
- **Smooth Animations**: Fluid transitions and interactions
- **Responsive Layout**: Adapts to different screen sizes

## 🏗️ Architecture

This project follows **Clean Architecture** principles with a feature-based structure:

```bash
📁 com.example.tiktok_clone/
├── 🏛️ core/                    # Core application functionality
│   ├── navigation/             # App-wide navigation
│   └── utils/                  # Shared utilities & constants
├── 🎯 features/                # Feature modules
│   ├── auth/                   # User authentication
│   └── home/                   # Main app features
│       ├── ui/
│       │   ├── camera/         # Camera functionality
│       │   └── home/           # Home feed
│       └── helper/             # Feature-specific helpers
└── 🎨 ui/theme/               # App theming & styling
```

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit
- **Android Architecture Components** - ViewModel, Navigation, Lifecycle

### Camera & Media
- **CameraX** - Camera library for consistent camera experience
- **Android View Integration** - Camera preview using AndroidView

### UI & Navigation
- **Material Design 3** - Design system
- **Compose Navigation** - Type-safe navigation
- **FontAwesome Icons** - Comprehensive icon library

### State Management
- **State Hoisting** - Compose state management patterns
- **MutableState** - Reactive state handling

## 📋 Requirements

- **Android Studio**: Arctic Fox or later
- **Kotlin**: 1.8.0+
- **Android SDK**: API 24 (Android 7.0) minimum
- **Gradle**: 8.0+

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/tiktok-clone.git
cd tiktok-clone
```

### 2. Open in Android Studio
1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to the cloned directory
4. Wait for Gradle sync to complete

### 3. Build and Run
```bash
# Using command line
./gradlew assembleDebug

# Install on device/emulator
./gradlew installDebug
```

Or use Android Studio's built-in run button.

## 📱 App Permissions

The app requires the following permissions:

| Permission | Purpose | When Requested |
|-----------|---------|----------------|
| `CAMERA` | Access camera for video recording | When opening camera |
| `RECORD_AUDIO` | Record audio for videos | When opening camera |

## 🎯 Key Components

### Navigation System
```kotlin
// App Navigation Structure
HomeScreen ↔ CameraAccessScreen
```

### Camera Implementation
```kotlin
// Camera Preview Setup
CameraPreviewScreen()
├── Permission Handling
├── CameraX Integration
└── Live Preview Feed
```

### Interactive Elements
```kotlin
// Home Screen Interactions
MiddleSection()
├── ❤️ Heart (with animation)
├── 💬 Comment
├── 🔖 Save
└── 📤 Share
```

## 🔧 Configuration

### Build Configuration
- **Compile SDK**: 34
- **Target SDK**: 34
- **Minimum SDK**: 24

### Dependencies
Key dependencies include:
```kotlin
implementation("androidx.compose.ui:ui:1.5.4")
implementation("androidx.camera:camera-camera2:1.3.0")
implementation("androidx.navigation:navigation-compose:2.7.5")
```

## 🎨 UI Components

### Home Screen Layout
- **Video Feed**: Full-screen vertical scrolling
- **Action Buttons**: Right-aligned interactive elements
- **Bottom Navigation**: Fixed navigation bar
- **Top Bar**: Search and user options

### Camera Screen Layout
- **Preview Area**: Full-screen camera view
- **Controls**: Bottom-aligned recording options
- **Settings**: Permission request interface

## 🔄 State Management

### Home Screen State
```kotlin
// Example state management
var selectedIndex by remember { mutableIntStateOf(0) }
var isLiked by remember { mutableStateOf(false) }
var likeCount by remember { mutableIntStateOf(2293) }
```

### Camera Permissions
```kotlin
// Permission state handling
var hasPermissions by remember { 
    mutableStateOf(checkCameraPermissions(context)) 
}
```

## 🐛 Troubleshooting

### Common Issues

#### Camera Black Screen
**Problem**: Camera shows black screen after granting permissions
**Solution**: Ensure proper surface provider setup and camera binding

#### Navigation Issues
**Problem**: Navigation between screens not working
**Solution**: Check NavHost setup and route definitions

#### Permission Denied
**Problem**: App doesn't request permissions properly
**Solution**: Verify manifest permissions and launcher implementation

### Debug Tips
1. Check Logcat for camera binding errors
2. Verify permissions in device settings
3. Test on multiple API levels
4. Use Compose Preview for UI debugging

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **TikTok** - For the original app concept and design inspiration
- **Android Developers** - For excellent documentation and samples
- **Compose Community** - For valuable insights and best practices

## 📞 Support

For questions, suggestions, or issues:
- 📧 Email: giahuyto3107@gmail.com
- 🐛 Issues: [GitHub Issues](https://github.com/giahuyto3107/tiktok-clone/issues)
- 💬 Discussions: [GitHub Discussions](https://github.com/giahuyto3107/tiktok-clone/discussions)

---

**Built with ❤️ using Jetpack Compose**

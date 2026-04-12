# 🎵 TikTok Clone — Android Short-Video Social Network

A full-featured Android application built with **Kotlin** and **Jetpack Compose** that replicates the core experience of TikTok. The app covers the entire user journey — from authentication to video creation, social interactions, real-time messaging, push notifications, and an admin dashboard — all backed by a custom **Node.js** API and **Firebase** services.

---

## ✨ Key Features (Resume Highlights)

- **Full-stack mobile application** built with Jetpack Compose, Clean Architecture, and a custom REST API (Node.js + Firebase Admin SDK).
- **Multi-provider authentication**: Email/Password, Google Sign-In, and Facebook Login via Firebase Auth.
- **Short-video feed** with infinite vertical paging (Paging 3 + ExoPlayer) and smooth lifecycle-aware playback.
- **Camera & content creation**: Live preview with CameraX, video/photo capture, on-device video compression, and media upload to the server.
- **Social layer**: Real-time likes with animation, comment threads, emoji reactions, follow system, and a share sheet.
- **Direct messaging**: Inbox with friend/chat lists and a full message conversation screen.
- **Push notifications**: Firebase Cloud Messaging (FCM) for social and follow activity alerts.
- **Search**: Multi-tab result screen (Top, Video, User, Image, Live, Shop) powered by a Retrofit API.
- **User profiles**: Own profile page and other-user profile pages with follow/unfollow actions.
- **Admin dashboard**: In-app admin panel for user management and content moderation.
- **Dependency Injection**: Hilt + Koin for scalable, testable module wiring.

---

## 📱 Feature Overview

### 🏠 Home Feed
- **Vertical Pager**: Full-screen video feed powered by `VerticalPager` and Paging 3 for seamless infinite scroll.
- **ExoPlayer Playback**: Lifecycle-aware video player that pauses/resumes correctly on app background/foreground transitions.
- **Interactions**: Animated like button, comment sheet, save, and share actions.
- **Video Info**: Author avatar, display name, and video description fetched from the backend.
- **Bottom Navigation**: Home, Shop, Camera, Inbox, and Profile.

### 🔐 Authentication
- **Email / Password**: Custom sign-up and login forms.
- **Google Sign-In**: One-tap authentication via Credential Manager.
- **Facebook Login**: OAuth flow integrated with the Facebook SDK and Firebase Auth.
- **Token Verification**: Firebase ID tokens verified server-side via the Firebase Admin SDK.

### 📸 Camera & Content Creation
- **Live Preview**: Real-time camera feed using CameraX.
- **Media Capture**: Record videos (15 s / 60 s / 10 min) or take photos; front/rear camera toggle.
- **Video Compression**: On-device compression with LightCompressor before upload.
- **Preview & Publish**: Review media in a dedicated PreviewScreen, add a caption, and post via Retrofit.
- **Permission Handling**: Runtime permission requests for camera and microphone.

### 🔍 Search
- **Discover Screen**: Trending topics and suggestions shown before typing.
- **Result Screen**: Tabbed results — Top, Video, User, Image, Live, Shop.
- **Shared ViewModel**: `SearchViewModel` shared across the search flow via a nested NavGraph.

### 💬 Inbox & Messaging
- **Inbox Screen**: Friend list and chat list with notification items.
- **Message Screen**: Full conversation UI with message bubbles, timestamps, and a bottom input bar.

### 🔔 Notifications
- **Social Notifications**: Likes and comments on your posts.
- **Follow Notifications**: When someone follows your account.
- **FCM**: Firebase Cloud Messaging delivers push notifications even when the app is in the background.

### 👤 Profiles
- **Own Profile**: Displays avatar, stats (followers, following, likes), and a grid of your posts.
- **Other User Profile**: View any user's public profile and follow/unfollow them.

### 🤝 Social Interactions
- **Comments**: Post and view threaded comments with emoji reactions.
- **Share Sheet**: Send videos to friends inside the app or to external platforms.
- **Post Options**: Report, "Not Interested", and playback speed controls.

### 🛒 Shop
- Dedicated Shop screen accessible from the bottom navigation bar.

### 🛡️ Admin Dashboard
- **User Management**: Search, view details, and manage registered users.
- **Content Management**: Review and moderate posts/videos.
- **Sidebar Navigation**: Persistent sidebar layout for the admin panel.

---

## 🏗️ Architecture

The project follows **Clean Architecture** with a **feature-first** package structure:

```
📁 com.example.tiktok_clone/
├── 🏛️ core/
│   ├── config/          # API configuration & environment URLs
│   ├── navigation/      # App-wide NavHost & route definitions
│   ├── ui/              # Shared composables (Wrapper, etc.)
│   └── user/            # Domain model, repository interface & DI module
├── 🎯 features/
│   ├── admin/           # Admin dashboard (user & content management)
│   ├── auth/            # Authentication (Email, Google, Facebook)
│   ├── camera/          # CameraX capture & preview
│   ├── home/            # Video feed (Paging 3 + ExoPlayer)
│   ├── inbox/           # Messaging & friend list
│   ├── notification/    # Social & follow notifications
│   ├── post/            # Media preview, caption, and upload
│   ├── profile/         # Own & other-user profiles
│   ├── search/          # Discover & multi-tab search results
│   ├── shop/            # Shop screen
│   ├── social/          # Comments, sharing, follow, reactions
│   └── user/            # User data access layer
├── 💉 di/               # Koin & Hilt modules
└── 🎨 ui/theme/         # Material 3 theme, colors, typography, dimensions
```

Each feature follows a **data → viewmodel → ui** layering with:
- `data/` — API services (Retrofit), repositories, models
- `viewmodel/` — `ViewModel` holding `StateFlow` / `LiveData`
- `ui/` — Composable screens and reusable components

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material Design 3 |
| Navigation | Compose Navigation (nested graphs) |
| Video Playback | Media3 ExoPlayer |
| Camera | CameraX (Camera2 backend) |
| Image Loading | Coil (with `VideoFrameDecoder`) |
| Video Compression | LightCompressor |
| Networking | Retrofit 2, OkHttp (logging interceptor), Gson |
| Pagination | Paging 3 (`paging-compose`) |
| Authentication | Firebase Auth (Email, Google, Facebook) |
| Database / Storage | Firebase Firestore, Firebase Storage |
| Push Notifications | Firebase Cloud Messaging (FCM) |
| Dependency Injection | Hilt 2.50, Koin 3.5 |
| Async | Kotlin Coroutines, Flow |
| Icons | FontAwesome Compose |
| Build | Gradle (Kotlin DSL), `.env`-based `BuildConfig` fields |

---

## 📋 Requirements

- **Android Studio**: Hedgehog or later
- **Kotlin**: 1.9+
- **Android SDK**: API 24 (Android 7.0) minimum, API 35 target
- **Gradle**: 8.0+

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/giahuyto3107/tiktok-clone.git
cd tiktok-clone
```

### 2. Add Firebase Configuration
1. Create a Firebase project and download **`google-services.json`**.
2. Place it in the `app/` directory.

### 3. Configure Environment Variables
Create a `.env` file in the project root:
```env
API_BASE_URL=http://your-api-host
API_URL_EMULATOR=http://10.0.2.2:3000
API_URL_DEVICE=http://192.168.x.x:3000
API_URL_STAGING=https://staging.your-api.com
API_URL_PRODUCTION=https://api.your-domain.com
```

### 4. Open in Android Studio
1. Open Android Studio → *Open an existing project*.
2. Navigate to the cloned directory.
3. Wait for Gradle sync to complete.

### 5. Build and Run
```bash
# Debug build
./gradlew assembleDebug

# Install on connected device / emulator
./gradlew installDebug
```

---

## 📱 App Permissions

| Permission | Purpose |
|---|---|
| `CAMERA` | Live camera preview and video/photo capture |
| `RECORD_AUDIO` | Audio recording during video capture |
| `INTERNET` | API calls and Firebase services |
| `POST_NOTIFICATIONS` | FCM push notifications (Android 13+) |

---

## 🔧 Build Configuration

| Setting | Value |
|---|---|
| Compile SDK | 35 |
| Target SDK | 35 |
| Min SDK | 24 |
| Java compatibility | 17 |

---

## 🤝 Contributing

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature`.
3. Commit your changes: `git commit -m 'Add your feature'`.
4. Push to the branch: `git push origin feature/your-feature`.
5. Open a Pull Request.

---

## 📄 License

This project is licensed under the MIT License.

---

## 🙏 Acknowledgments

- **TikTok** — Original concept and design inspiration.
- **Android Developers** — Excellent documentation and official samples.
- **Jetpack Compose Community** — Valuable insights and best practices.

---

## 📞 Contact

- 📧 Email: giahuyto3107@gmail.com
- 🐛 Issues: [GitHub Issues](https://github.com/giahuyto3107/tiktok-clone/issues)
- 💬 Discussions: [GitHub Discussions](https://github.com/giahuyto3107/tiktok-clone/discussions)

---

*Built with ❤️ using Jetpack Compose*

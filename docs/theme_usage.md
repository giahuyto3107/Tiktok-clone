## TikTok Clone – Compose Theme Guide

This folder centralizes all theming for the TikTok clone using Jetpack Compose + Material 3.  
Use these files instead of hard‑coding colors, text sizes, or spacings in your UI.

### Files overview

- **`Color.kt`**  
  Defines the app’s color palette:
  - **Brand / primary colors**: `TikTokRed`, `RedHeart`, `BlueAccent`, `YellowSave`
  - **Base colors**: `Black`, `White`, `DarkSurface`, `GrayBackground`, etc.
  - **Text colors**: `TextOnLight`, `TextOnDark`, `TextSecondary`, `TextPrimaryLight`, `TextPrimaryDark`, etc.  
  These are used by `Theme.kt` to build the Material 3 `ColorScheme`, and can also be used directly in custom composables if needed.

- **`Dimens.kt`**  
  Central place for **spacing, icon sizes, radii, and font sizes** via the `Dimens` object:
  - Spacing: `SpacingXS`, `SpacingM`, `SpacingXL`, …
  - Icon sizes: `IconS`, `IconM`, `IconXL`, …
  - Layout: `AppBarHeight`, `BottomNavHeight`, `CardHeightM`, …
  - Typography sizes: `FontSizeS`, `FontTitleM`, etc.  
  Use these instead of hard‑coded `dp`/`sp` values to keep layouts consistent across the app.

- **`Type.kt`**  
  Defines the app’s **Material 3 typography** in the `Typography` instance, mapping TikTok‑like text roles to concrete styles:
  - `displayMedium` – big hero titles (profile names / headers)
  - `headlineSmall` – main page headlines
  - `titleLarge`, `titleMedium` – usernames, list titles, post descriptions
  - `bodyLarge`, `bodyMedium` – body text and captions
  - `labelLarge`, `labelMedium`, `labelSmall` – buttons, counters, metadata/timestamps  
  Fonts are sized using `Dimens` so you can adjust typography scales in one place.

- **`Theme.kt`**  
  Defines the top‑level **`Tiktok_cloneTheme`** composable that:
  - Chooses **dark** / **light** color schemes (`DarkColorScheme`, `LightColorScheme`)
  - Optionally uses **dynamic color** (Material You) if enabled and available
  - Configures status bar color + icon appearance
  - Exposes Material 3’s `MaterialTheme` with this app’s `colorScheme` and `Typography`

### How to apply the theme in your app

Wrap your entire UI in `Tiktok_cloneTheme`, usually in `MainActivity`:

```kotlin
setContent {
    Tiktok_cloneTheme(
        darkTheme = isSystemInDarkTheme(), // or true/false if you want to force a mode
        dynamicColor = false               // keep false to match TikTok brand
    ) {
        // Root of your app UI (NavHost, HomeScreen, etc.)
        TiktokAppRoot()
    }
}
```

Inside this block, you get access to `MaterialTheme` with the app’s colors and typography:

```kotlin
@Composable
fun ExampleButton() {
    Button(
        onClick = { /* TODO */ },
        modifier = Modifier.padding(Dimens.SpacingM)
    ) {
        Text(
            text = "Follow",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
```

### Using theme values in composables

- **Colors (preferred)** – via `MaterialTheme`:

```kotlin
Box(
    modifier = Modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(Dimens.SpacingL)
) {
    Text(
        text = "Hello TikTok",
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.titleLarge
    )
}
```

If you need brand‑specific accents, you can directly import from `Color.kt`:

```kotlin
import com.example.tiktok_clone.ui.theme.TikTokRed

Icon(
    imageVector = Icons.Default.Favorite,
    contentDescription = null,
    tint = TikTokRed,
    modifier = Modifier.size(Dimens.IconM)
)
```

- **Spacing and sizes** – via `Dimens`:

```kotlin
Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(Dimens.SpacingL),
    verticalArrangement = Arrangement.spacedBy(Dimens.SpacingM)
) {
    // Children here…
}
```

### When to update these files

- **To change app colors**: edit `Color.kt` and, if needed, adjust how they are mapped in `Theme.kt`.
- **To tweak typography scale**: update font sizes in `Dimens.kt` and/or the mappings in `Type.kt`.
- **To adjust global spacing / component sizes**: update `Dimens.kt` and use those values throughout the UI.

By routing all styling through this `ui/theme` layer, you keep the TikTok‑like look consistent and make it easy to reskin the app later.


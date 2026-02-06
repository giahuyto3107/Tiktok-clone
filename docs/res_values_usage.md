## `res/values` – Resource Reference

This folder contains XML resources that can be used from both the **Compose** layer and any remaining **View/XML** code.
Keeping these values centralized avoids magic numbers/strings and makes the app easier to theme and localize.

### Files overview

- **`integers.xml`**
  Numeric configuration values:
  - Animation durations: `animation_short`, `animation_medium`, `animation_long`
  - Pagination: `items_per_page`, `load_more_threshold`
  - Networking / logic: `resend_time_countdown`, `max_retries`, `request_timeout`
  - Database: `database_version`
  These make behavior like timeouts, animation speed, and paging size configurable without touching code.

- **`strings.xml`**
  Application strings and configuration text:
  - App names: `app_name`, `app_display_name`
  - Auth: `default_web_client_id`
  - Date/time formats: `date_format_full`, `time_format_24`, `date_time_format`, …
  - Database: `database_name`
  - Regex patterns: `email_pattern`, `phone_pattern`
  - Asset paths: `background_image_path`
  This file is the entry point for **localization**; additional languages can be added via `values-xx/strings.xml`.

- **`themes.xml`**
  Defines the base **Android theme**:
  - `Theme.Tiktok_clone` – currently extends `android:Theme.Material.Light.NoActionBar`
  This is used mainly by the `AndroidManifest` and the Activity window. Compose uses `Tiktok_cloneTheme` from `ui/theme`, but this XML theme is still important for the system (splash, dialogs, etc.).

### How to use these resources

#### In Compose

Most visual styling should go through the Compose theme layer (`MaterialTheme` and `Dimens`), but you can still access XML resources when needed:

```kotlin
val maxRetries = integerResource(id = R.integer.max_retries)
val appName = stringResource(id = R.string.app_display_name)
```

#### In XML layouts

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="@string/app_display_name"
    android:textSize="@dimen/font_m"
    android:textColor="@color/text_on_light"
    android:padding="@dimen/spacing_m" />
```

### When to update these files

- **Tune timing, paging, or retry behavior** → edit `integers.xml`.
- **Rename text, add copy, or localize** → edit `strings.xml` (and add translated `values-xx/strings.xml`).
- **Change the app’s base Android theme** → edit `themes.xml` and ensure `AndroidManifest.xml` references it.

Using `res/values` consistently across your app makes it easy to reskin the TikTok clone, adjust behavior, and add localization without touching business logic.

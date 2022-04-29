# What is this?

This minimal reproducible example shows that, for Android 10, the method [`PackageManager.getLaunchIntentForPackage(packageName)`](https://developer.android.com/reference/android/content/pm/PackageManager#getLaunchIntentForPackage(java.lang.String)) is buggy. We need to call [`setPackage(null)`]() on the intent returned to make it work correctly.

# Conceptual explanation of the Minimal Reproducible Example:

We have 2 activities:

1. **Splash Activity**: functions as a splash screen. It loads the app (we just simulate by waiting for 1.5 seconds). After loading finishes, we redirect the user to the Main Activity (via `startActivity(Intent)`). Then we call `finish()` on the Splash Activity itself.
2. **Main Activity**: It just posts a notification. Whenever you press the notification, the app should come to the foreground. In other words, it should work exactly as a launcher icon. This is a clear indication that we need to use `PackageManager.getLaunchIntentForPackage(getPackageName())`.

*(Note: since this bug happens only on Android 10, we can't leverage the SplashScreen APIs. But that is irrelevant. The general scenario is when there are two activities and the notification is only supposed to open the current (last) activity. The bug is that it opens the **first** activity. A splash screen is only a specific example of this general scenario).*

To clarify the expected behavior: Assume we already got past the `SplashActivity` and we are now in the `MainActivity`. Then we minimize the app by pressing the Home button (not the Back Button!). Then we press on the app's launcher icon. The app opens again where we left off (that is, on `MainActivity` ... notice that the `SplashActivity` didn't show).

On the contrary, had we pressed the Back button instead of minimizing the app, the `SplashActivity` will show again. This is the expected behavior.

However, when pressing the notification, the app doesn't show this behavior. It turns out that if you don't do `setPackage(null)` on the intent returned by `PackageManager.getLaunchIntentForPackage(getPackageName())`, the notification will create a new instance of `SplashActivity` every time, no matter if the app is already on the foreground.

Therefore, we can just conclude that `PackageManager.getLaunchIntentForPackage(packageName)` is buggy.

# Code

1. Commit [1f8494a](https://github.com/HectorRicardo/getLaunchIntent-bug/commit/1f8494a674dc92a160c393e4b6585d52fdc19f55) presents a blank Android project created from scratch in Android Studio.
2. Commit [63ee09b](https://github.com/HectorRicardo/getLaunchIntent-bug/commit/63ee09b71213bd0bc721d3cbace395cde1c3906b) adds a splash screen.
3. Commit [f109c0d](https://github.com/HectorRicardo/getLaunchIntent-bug/commit/f109c0d529f48bc7ee232240a141b8a780915520) adds the notification logic.
4. Commit [f2d9f658](https://github.com/HectorRicardo/getLaunchIntent-bug/commit/f2d9f65874abf71d0a3f0596e0395c4ea4f3ea46) presents the fix by calling `setPackage(null)` to the intent returned by `PackageManager.getLaunchIntentForPackage(packageName)`.

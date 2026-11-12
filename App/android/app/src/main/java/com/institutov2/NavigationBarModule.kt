package com.routinefights

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.core.view.WindowInsetsControllerCompat
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.UiThreadUtil

/** Lets JS colour the system navigation bar to match the in-app theme. */
class NavigationBarModule(context: ReactApplicationContext) : ReactContextBaseJavaModule(context) {

  override fun getName(): String = "NavigationBar"

  @ReactMethod
  fun setColor(color: String, lightIcons: Boolean) {
    val activity = currentActivity ?: return
    UiThreadUtil.runOnUiThread {
      val window = activity.window
      @Suppress("DEPRECATION")
      window.navigationBarColor = Color.parseColor(color)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isNavigationBarContrastEnforced = false
      }
      WindowInsetsControllerCompat(window, window.decorView)
          .isAppearanceLightNavigationBars = lightIcons
    }
  }

  /**
   * Colours the window behind every screen. Android picks it from the system
   * theme, so with the app in dark mode and the phone in light mode screen
   * transitions flashed white.
   */
  @ReactMethod
  fun setWindowBackground(color: String) {
    val activity = currentActivity ?: return
    UiThreadUtil.runOnUiThread {
      activity.window.setBackgroundDrawable(ColorDrawable(Color.parseColor(color)))
    }
  }
}

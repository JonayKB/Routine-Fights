import { Platform, Vibration } from "react-native";
import type { HapticFeedbackTypes } from "react-native-haptic-feedback";

/**
 * Short vibrations to confirm what the user just did.
 *
 * Uses the system haptic engine (Taptic Engine on iOS, haptic constants on
 * Android), which respects the user's system haptics setting.
 */

type Trigger = (type: keyof typeof HapticFeedbackTypes, options?: object) => void;

// Loaded lazily: the native module throws on import if the app was built
// without it (e.g. an old build, or tests), and haptics must never crash.
let trigger: Trigger | null | undefined;
const native = (): Trigger | null => {
  if (trigger === undefined) {
    try {
      trigger = require("react-native-haptic-feedback").trigger;
    } catch {
      trigger = null;
    }
  }
  return trigger;
};

const OPTIONS = { enableVibrateFallback: true, ignoreAndroidSystemSettings: false };

const feel = (type: keyof typeof HapticFeedbackTypes, fallback: number | number[]) => {
  try {
    const t = native();
    if (t) t(type, OPTIONS);
    // iOS ignores vibration lengths (always ~400 ms), so no fallback there.
    else if (Platform.OS === "android") Vibration.vibrate(fallback);
  } catch {
    // Haptics are a nice-to-have.
  }
};

export const haptics = {
  /** A tap that changes something small: like, follow, toggle. */
  tap: () => feel("impactLight", 12),
  /** A long press that opened something (menu, sheet). */
  press: () => feel("impactMedium", 25),
  /** An action that finished well: comment sent, post uploaded. */
  success: () => feel("notificationSuccess", [0, 18, 70, 28]),
  /** An action that failed. */
  error: () => feel("notificationError", [0, 40, 60, 40]),
};

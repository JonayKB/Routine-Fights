/**
 * Routine Fights design tokens.
 *
 * Direction: a campfire at night. Warm, low-saturation neutrals for every
 * surface; one ember accent taken from the logo. Rich colour is reserved for
 * the streak flame (see StreakFlame.tsx), which heats up as a streak grows.
 */

export const palette = {
  light: {
    page: "#F7F4EE",
    card: "#FFFFFF",
    raised: "#EFE9DF",
    hairline: "#1F1B161A",
    text: "#1F1B16",
    textSecondary: "#5E554A",
    textMuted: "#7B7266",
  },
  dark: {
    page: "#161513",
    card: "#1C1A18",
    raised: "#262421",
    hairline: "#EFE9DE1A",
    text: "#EFE9DE",
    textSecondary: "#AAA398",
    textMuted: "#8A847A",
  },
  /** Filled buttons and the active selection. White text on it is 5.2:1. */
  accent: "#C2410C",
  /** Accent used as ink or icon colour on dark surfaces. */
  accentOnDark: "#FB923C",
  like: "#E5484D",
};

export const colors = (darkmode: boolean) =>
  darkmode ? palette.dark : palette.light;

/** Display face for figures and titles; the interface stays on the system font. */
export const fonts = {
  display: "Lexend-SemiBold",
  displayMedium: "Lexend-Medium",
  displayRegular: "Lexend-Regular",
};

/**
 * React Navigation theme in the app's colours. Its background is what shows
 * between screens while they animate.
 */
export const navigationTheme = <T extends { dark: boolean; colors: Record<string, string> }>(
  base: T,
  darkmode: boolean
): T => ({
  ...base,
  dark: darkmode,
  colors: {
    ...base.colors,
    primary: darkmode ? palette.accentOnDark : palette.accent,
    background: colors(darkmode).page,
    card: colors(darkmode).card,
    text: colors(darkmode).text,
    border: colors(darkmode).hairline,
  },
});

/** @type {import('tailwindcss').Config} */
// Same tokens as the app (App/src/styles/theme.ts): warm neutrals, one ember accent.
// Colours are CSS variables so the dark theme only swaps the variables.
const v = (name) => `rgb(var(--${name}) / <alpha-value>)`;

module.exports = {
  content: ["./src/**/*.{js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        page: v("page"),
        card: v("card"),
        raised: v("raised"),
        ink: v("ink"),
        "ink-2": v("ink-2"),
        "ink-3": v("ink-3"),
        accent: v("accent"),
        "accent-ink": v("accent-ink"),
        danger: v("danger"),
        success: v("success"),
      },
      fontFamily: {
        display: ["Lexend", "ui-sans-serif", "system-ui", "sans-serif"],
        sans: ["ui-sans-serif", "system-ui", "-apple-system", "Segoe UI", "Roboto", "sans-serif"],
      },
      boxShadow: {
        card: "0 1px 2px rgb(31 27 22 / 0.06), 0 1px 1px rgb(31 27 22 / 0.04)",
        pop: "0 12px 32px rgb(31 27 22 / 0.18)",
      },
    },
  },
  plugins: [],
};

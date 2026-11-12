import React, { ReactNode, useEffect, useMemo, useState } from "react";
import { createTheme, ThemeProvider } from "@mui/material/styles";

const query = "(prefers-color-scheme: dark)";

/** Follows the system theme, like the CSS variables in index.css. */
export const useDarkMode = () => {
  const [dark, setDark] = useState(() => window.matchMedia?.(query).matches ?? false);
  useEffect(() => {
    const media = window.matchMedia?.(query);
    if (!media) return;
    const onChange = () => setDark(media.matches);
    media.addEventListener("change", onChange);
    return () => media.removeEventListener("change", onChange);
  }, []);
  return dark;
};

/** MUI (charts and their tooltips) in the panel's colours. */
export const MuiTheme = ({ children }: { children: ReactNode }) => {
  const dark = useDarkMode();
  const theme = useMemo(
    () =>
      createTheme({
        palette: {
          mode: dark ? "dark" : "light",
          primary: { main: dark ? "#EA580C" : "#C2410C" },
          background: { paper: dark ? "#1C1A18" : "#FFFFFF", default: dark ? "#161513" : "#F7F4EE" },
          text: { primary: dark ? "#EFE9DE" : "#1F1B16", secondary: dark ? "#AAA398" : "#5E554A" },
        },
        typography: { fontFamily: "ui-sans-serif, system-ui, -apple-system, Segoe UI, Roboto, sans-serif" },
      }),
    [dark]
  );
  return <ThemeProvider theme={theme}>{children}</ThemeProvider>;
};

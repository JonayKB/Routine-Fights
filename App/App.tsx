import 'react-native-gesture-handler';
import React, { useMemo } from 'react';
import { DarkTheme, DefaultTheme, NavigationContainer } from '@react-navigation/native';
import { SafeAreaProvider, initialWindowMetrics } from 'react-native-safe-area-context';
import LoginNavigation from './src/navigation/LoginStackNavigation';
import SettingsContextProvider, { useSettingsContext } from './src/contexts/SettingsContextProvider';
import { navigationTheme } from './src/styles/theme';
import './global.css';
import { navigationRef } from './src/utils/session';

/** Navigation painted with the in-app theme, so screen changes never flash. */
const ThemedNavigation = () => {
  const { darkmode } = useSettingsContext();
  const theme = useMemo(
    () => navigationTheme(darkmode ? DarkTheme : DefaultTheme, darkmode),
    [darkmode]
  );
  return (
    <NavigationContainer ref={navigationRef} theme={theme}>
      <LoginNavigation />
    </NavigationContainer>
  );
};

function App(): React.JSX.Element {
  return (
    <SafeAreaProvider initialMetrics={initialWindowMetrics}>
      <SettingsContextProvider>
        <ThemedNavigation />
      </SettingsContextProvider>
    </SafeAreaProvider>
  );
}

export default App;

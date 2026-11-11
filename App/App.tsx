import 'react-native-gesture-handler';
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import { SafeAreaProvider, initialWindowMetrics } from 'react-native-safe-area-context';
import LoginNavigation from './src/navigation/LoginStackNavigation';
import './global.css';
import { navigationRef } from './src/utils/session';

function App(): React.JSX.Element {
  return (
    <SafeAreaProvider initialMetrics={initialWindowMetrics}>
      <NavigationContainer ref={navigationRef}>
        <LoginNavigation />
      </NavigationContainer>
    </SafeAreaProvider>
  );
}

export default App;

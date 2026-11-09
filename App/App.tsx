import 'react-native-gesture-handler';
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import LoginNavigation from './src/navigation/LoginStackNavigation';
import './global.css';
import { navigationRef } from './src/utils/session';

function App(): React.JSX.Element {
  return (
    <NavigationContainer ref={navigationRef}>
      <LoginNavigation />
    </NavigationContainer>
  );
}

export default App;

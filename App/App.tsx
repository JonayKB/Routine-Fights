import 'react-native-gesture-handler';
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import LoginNavigation from './src/navigation/LoginNavigation';
import './global.css'

function App(): React.JSX.Element {
  return (
    <NavigationContainer>
      <LoginNavigation />
    </NavigationContainer>
  );
}

export default App;

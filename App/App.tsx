import 'react-native-gesture-handler';
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';
import LoginNavigation from './src/navigation/LoginStackNavigation';
import './global.css';
import { getMessaging, requestPermission, getToken, onMessage, subscribeToTopic } from '@react-native-firebase/messaging';
import { useEffect } from 'react';


function App(): React.JSX.Element {
    async function requestUserPermission() {
      const authStatus = await requestPermission();
      console.log('Auth status:', authStatus);
    }

    function useFirebaseMessaging() {
      useEffect(() => {
        const messaging = getMessaging();

        requestUserPermission();

        getToken(messaging)
          .then(token => {
            console.log('FCM Token:', token);
            return subscribeToTopic(messaging,'general');
          })
          .then(() => {
            console.log('Subscribed to general topic');
          })
          .catch(err => {
            console.error('Error subscribing to general topic:', err);
          });

        const unsubscribe = onMessage(messaging, async remoteMessage => {
          console.log('Foregorund message:', remoteMessage);
        });

        return unsubscribe;
      }, []);
    }
    useFirebaseMessaging();
  return (
    <NavigationContainer>
      <LoginNavigation />
    </NavigationContainer>
  );
}

export default App;

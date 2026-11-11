/**
 * @format
 */

import {AppRegistry} from 'react-native';
import App from './App';
import {name as appName} from './app.json';
import {registerBackgroundHandler} from './src/utils/push';

// Must run before the app registers, also when it starts in the background.
registerBackgroundHandler();

AppRegistry.registerComponent(appName, () => App);

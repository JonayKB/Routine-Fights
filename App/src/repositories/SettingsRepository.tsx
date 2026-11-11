import RNSecureKeyStore from 'react-native-secure-key-store';
import { tearDownPush } from '../utils/push';
import { realtime } from '../utils/realtime';

export const logout = async () => {
    realtime.stop();
    // Before forgetting the session, so this phone stops getting its pushes.
    await tearDownPush();
    await RNSecureKeyStore.remove("token");
}

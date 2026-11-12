import { tearDownPush } from '../utils/push';
import { realtime } from '../utils/realtime';
import { Account, forgetCurrent } from '../utils/accounts';

/**
 * Signs the active account out of this phone.
 * @returns the other accounts still signed in, to switch to one of them
 */
export const logout = async (): Promise<Account[]> => {
    realtime.stop();
    // Before forgetting the session, so this phone stops getting its pushes.
    await tearDownPush();
    return forgetCurrent();
}

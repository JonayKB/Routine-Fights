import { PermissionsAndroid, Platform } from "react-native";
import AsyncStorage from "@react-native-async-storage/async-storage";
import messaging, { FirebaseMessagingTypes } from "@react-native-firebase/messaging";
import { registerDevice, unregisterDevice } from "../repositories/NotificationRepository";

export type PushMessage = FirebaseMessagingTypes.RemoteMessage;

const TOPIC_KEY = "pushTopic";
const LANGUAGE_PREFIXES = ["en", "es", "fr", "de"];

/**
 * Firebase is only there when the build has its config (google-services.json /
 * GoogleService-Info.plist). Without it every call here quietly does nothing.
 */
const fcm = (): FirebaseMessagingTypes.Module | null => {
  try {
    return messaging();
  } catch {
    return null;
  }
};

const askPermission = async (m: FirebaseMessagingTypes.Module): Promise<boolean> => {
  if (Platform.OS === "android") {
    // Android 13+ asks at runtime; older versions always allow.
    if (Number(Platform.Version) < 33) return true;
    const result = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS);
    return result === PermissionsAndroid.RESULTS.GRANTED;
  }
  const status = await m.requestPermission();
  return (
    status === messaging.AuthorizationStatus.AUTHORIZED || status === messaging.AuthorizationStatus.PROVISIONAL
  );
};

/** Topic for announcements to everyone, one per language ("general-es"). */
const topicFor = (language: string) => {
  const prefix = (language || "en").split(/[-_]/)[0].toLowerCase();
  return `general-${LANGUAGE_PREFIXES.includes(prefix) ? prefix : "en"}`;
};

/**
 * Asks for permission, then tells the API where to send this user's pushes and
 * follows the announcements topic of the app language. Safe to call on every
 * start: tokens and topics are idempotent.
 *
 * @returns a function that stops listening for token refreshes
 */
export const setUpPush = async (language: string): Promise<() => void> => {
  const m = fcm();
  if (!m) return () => {};
  try {
    if (!(await askPermission(m))) return () => {};
    if (Platform.OS === "ios" && !m.isDeviceRegisteredForRemoteMessages) {
      await m.registerDeviceForRemoteMessages();
    }
    await registerDevice(await m.getToken(), language);

    const topic = topicFor(language);
    const previous = await AsyncStorage.getItem(TOPIC_KEY);
    if (previous && previous !== topic) await m.unsubscribeFromTopic(previous);
    if (previous !== topic) {
      await m.subscribeToTopic(topic);
      await AsyncStorage.setItem(TOPIC_KEY, topic);
    }

    return m.onTokenRefresh((token) => {
      registerDevice(token, language).catch(() => {});
    });
  } catch (error) {
    console.warn("Push notifications unavailable:", error);
    return () => {};
  }
};

/** On logout: this phone stops getting the previous user's pushes. */
export const tearDownPush = async () => {
  const m = fcm();
  if (!m) return;
  try {
    const token = await m.getToken();
    await unregisterDevice(token);
    const topic = await AsyncStorage.getItem(TOPIC_KEY);
    if (topic) await m.unsubscribeFromTopic(topic);
    await AsyncStorage.removeItem(TOPIC_KEY);
    await m.deleteToken();
  } catch {
    // Offline or no Firebase: the API drops dead tokens on its own.
  }
};

/** Pushes that arrive while the app is open (the system doesn't show those). */
export const onForegroundPush = (handler: (message: PushMessage) => void) => {
  const m = fcm();
  return m ? m.onMessage(async (message) => handler(message)) : () => {};
};

/** Taps on a push, both with the app in the background and from a cold start. */
export const onPushOpened = (handler: (message: PushMessage) => void) => {
  const m = fcm();
  if (!m) return () => {};
  m.getInitialNotification()
    .then((message) => message && handler(message))
    .catch(() => {});
  return m.onNotificationOpenedApp(handler);
};

/** Required by Firebase for data that arrives with the app in the background. */
export const registerBackgroundHandler = () => {
  const m = fcm();
  m?.setBackgroundMessageHandler(async () => {
    // The system already shows the notification; nothing else to do.
  });
};

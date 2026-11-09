import { createNavigationContainerRef } from "@react-navigation/native";

/** Lets code outside screens (e.g. the API client) move the app to another route. */
export const navigationRef = createNavigationContainerRef<any>();

let onExpired: (() => void) | null = null;
let expired = false;

/** Registers what to do when the API rejects the saved session. */
export const setSessionExpiredHandler = (handler: (() => void) | null) => {
  onExpired = handler;
};

/** Called by the API client on a 401; runs the handler once per session. */
export const notifySessionExpired = () => {
  if (expired) return;
  expired = true;
  onExpired?.();
};

/** Called after logging in so a later expiry is handled again. */
export const sessionStarted = () => {
  expired = false;
};

import axios from "axios";
import RNSecureKeyStore from "react-native-secure-key-store";
import { neo4jUri } from "./Utils";
import { translations } from "../../translations/translation";
import { notifySessionExpired } from "./session";

/** An error returned by the API, with a message meant for the user. */
export class ApiError extends Error {}

/** Message of the error thrown when the saved session is no longer accepted. */
export const SESSION_EXPIRED = "Session expired";

const readToken = async (): Promise<string | null> => {
  try {
    return await RNSecureKeyStore.get("token");
  } catch {
    return null;
  }
};

/**
 * Runs a GraphQL operation. Values always travel as variables, never pasted
 * into the query text, so quotes or new lines in user input are safe.
 * Throws an ApiError carrying the server's message when the API reports one.
 */
export const gql = async <T = any>(
  query: string,
  variables: Record<string, unknown> = {},
  options: { auth?: boolean } = { auth: true }
): Promise<T> => {
  const token = options.auth === false ? null : await readToken();
  let data: any;
  try {
    ({ data } = await axios.post(
      neo4jUri,
      { query, variables },
      { headers: token ? { Authorization: `Bearer ${token}` } : {} }
    ));
  } catch (error: any) {
    // The API answers 401 when the saved token has expired or is no longer valid.
    if (token && error?.response?.status === 401) {
      notifySessionExpired();
      throw new ApiError(SESSION_EXPIRED);
    }
    throw error;
  }
  if (data?.errors?.length) {
    throw new ApiError(data.errors[0].message);
  }
  return data.data as T;
};

let currentLanguage = "en-EN";

/** Called by the settings context so errors follow the app language. */
export const setErrorLanguage = (language: string | null) => {
  currentLanguage = language || "en-EN";
};

// Known API messages (matched by their start) and the key of their translation.
const KNOWN_ERRORS: [string, string][] = [
  ["User not found or Invalid Credentials", "invalidCredentials"],
  ["User not verified", "notVerified"],
  ["Email already exists", "emailTaken"],
  ["Username already exists", "usernameTaken"],
  ["We could not send the verification e-mail", "mailFailed"],
  ["Password must be at least", "passwordShort"],
  ["Invalid email", "invalidEmail"],
  ["Invalid phone number", "invalidPhone"],
  ["Comment cannot be empty", "emptyComment"],
  ["You have already completed this activity", "alreadyDone"],
  ["User is not on activity", "notSubscribed"],
  ["Image not found", "imageMissing"],
  ["Image is too large", "imageTooLarge"],
  ["Image resolution is too large", "imageTooLarge"],
  ["File is not a", "invalidImage"],
  ["Something went wrong", "generic"],
  ["Access Denied", "forbidden"],
  ["You can only delete your own account", "forbidden"],
  ["Network Error", "noConnection"],
  [SESSION_EXPIRED, "sessionExpired"],
];

const localize = (message: string): string => {
  const t = (translations[currentLanguage] ?? translations["en-EN"]).screens.Errors;
  const hit = KNOWN_ERRORS.find(([prefix]) => message.startsWith(prefix));
  return hit && t?.[hit[1]] ? t[hit[1]] : message;
};

/** Human-readable, translated message for any error thrown by a repository or by axios. */
export const errorMessage = (error: any): string => {
  const body = error?.response?.data;
  const raw =
    body?.errors?.[0]?.message ??
    body?.error ??
    (typeof body === "string" && body.length < 300 ? body : null) ??
    error?.message ??
    "Something went wrong";
  return localize(String(raw));
};

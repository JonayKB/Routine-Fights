import axios from "axios";

export const BASE_URL = "https://routinefights.jonaykb.com/";
const TOKEN_KEY = "token";

export class ApiError extends Error {}

let onUnauthorized: (() => void) | null = null;

/** What to do when the API stops accepting the session (expired, not admin). */
export const setUnauthorizedHandler = (handler: (() => void) | null) => {
  onUnauthorized = handler;
};

export const readToken = () => {
  try {
    return localStorage.getItem(TOKEN_KEY) ?? "";
  } catch {
    return "";
  }
};

export const saveToken = (token: string) => {
  try {
    if (token) localStorage.setItem(TOKEN_KEY, token);
    else localStorage.removeItem(TOKEN_KEY);
  } catch {
    // Private mode: the session lasts until the tab closes.
  }
};

/**
 * Runs a GraphQL operation. Values always travel as variables, never pasted
 * into the query, so quotes in names or texts are safe.
 */
export const gql = async <T = any>(
  query: string,
  variables: Record<string, unknown> = {},
  options: { auth?: boolean } = {}
): Promise<T> => {
  const token = options.auth === false ? "" : readToken();
  try {
    const { data } = await axios.post(
      BASE_URL + "graphql",
      { query, variables },
      { headers: token ? { Authorization: `Bearer ${token}` } : {} }
    );
    if (data?.errors?.length) {
      const message: string = data.errors[0].message ?? "Something went wrong";
      if (token && message === "Access Denied") onUnauthorized?.();
      throw new ApiError(message);
    }
    return data.data as T;
  } catch (error: any) {
    if (error instanceof ApiError) throw error;
    if (token && error?.response?.status === 401) {
      onUnauthorized?.();
      throw new ApiError("Your session has expired");
    }
    throw new ApiError(error?.response?.data?.error ?? error?.message ?? "Network error");
  }
};

/** Role stored in the JWT (no signature check: the API does that). */
export const roleOf = (token: string): string | null => {
  try {
    const payload = JSON.parse(atob(token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/")));
    return payload.role ?? null;
  } catch {
    return null;
  }
};

import RNSecureKeyStore, { ACCESSIBLE } from "react-native-secure-key-store";

/** A signed-in account kept on this phone. */
export type Account = {
  email: string;
  username?: string | null;
  image?: string | null;
  token: string;
};

const ACCOUNTS_KEY = "accounts";
const TOKEN_KEY = "token";
const OPTIONS = { accessible: ACCESSIBLE.ALWAYS_THIS_DEVICE_ONLY };

const read = async (key: string): Promise<string | null> => {
  try {
    return await RNSecureKeyStore.get(key);
  } catch {
    return null;
  }
};

export const listAccounts = async (): Promise<Account[]> => {
  const raw = await read(ACCOUNTS_KEY);
  if (!raw) return [];
  try {
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed.filter((a) => a?.email && a?.token) : [];
  } catch {
    return [];
  }
};

const save = async (accounts: Account[]) => {
  if (accounts.length) {
    await RNSecureKeyStore.set(ACCOUNTS_KEY, JSON.stringify(accounts), OPTIONS);
  } else {
    await RNSecureKeyStore.remove(ACCOUNTS_KEY).catch(() => {});
  }
};

/** Adds the account, or refreshes its token, name and picture. */
export const upsertAccount = async (account: Account) => {
  const accounts = await listAccounts();
  const index = accounts.findIndex((a) => a.email === account.email);
  if (index >= 0) accounts[index] = { ...accounts[index], ...account };
  else accounts.push(account);
  await save(accounts);
};

/** The account whose token is the active one. */
export const currentAccount = async (): Promise<Account | null> => {
  const token = await read(TOKEN_KEY);
  if (!token) return null;
  return (await listAccounts()).find((a) => a.token === token) ?? null;
};

/** Makes the account the one every request uses. */
export const activate = async (account: Account) => {
  await RNSecureKeyStore.set(TOKEN_KEY, account.token, OPTIONS);
};

/**
 * Forgets the active account on this phone.
 * @returns the accounts that are still signed in
 */
export const forgetCurrent = async (): Promise<Account[]> => {
  const token = await read(TOKEN_KEY);
  const remaining = (await listAccounts()).filter((a) => a.token !== token);
  await save(remaining);
  await RNSecureKeyStore.remove(TOKEN_KEY).catch(() => {});
  return remaining;
};

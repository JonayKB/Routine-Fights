import { gql } from "../utils/graphql";
import { AppNotification } from "../utils/Notification";

const FIELDS = `id type targetId actorCount preview extra targetImage updatedAt read
  actors { email username image }`;

export const getNotifications = async (page = 0, perPage = 20): Promise<AppNotification[]> => {
  const data = await gql(
    `query MyNotifications($page: Int, $perPage: Int) { myNotifications(page: $page, perPage: $perPage) { ${FIELDS} } }`,
    { page, perPage }
  );
  return data.myNotifications ?? [];
};

/** Unread notifications and chats with unread messages, for the badges. */
export const getUnreadCounts = async (): Promise<{ notifications: number; messages: number }> => {
  const data = await gql(`query { unreadNotificationsCount unreadConversationsCount }`);
  return {
    notifications: data.unreadNotificationsCount ?? 0,
    messages: data.unreadConversationsCount ?? 0,
  };
};

/** Marks one entry as read, or all of them when no id is given. */
export const markNotificationsRead = async (id?: string) => {
  await gql(`mutation MarkRead($id: String) { markNotificationsRead(id: $id) }`, { id: id ?? null });
};

export const registerDevice = async (token: string, language: string) => {
  await gql(`mutation Register($token: String!, $language: String) { registerDevice(token: $token, language: $language) }`, {
    token,
    language,
  });
};

export const unregisterDevice = async (token: string) => {
  await gql(`mutation Unregister($token: String!) { unregisterDevice(token: $token) }`, { token }, { auth: false });
};

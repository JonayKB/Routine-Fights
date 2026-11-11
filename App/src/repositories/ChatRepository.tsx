import { gql } from "../utils/graphql";
import { ChatMessage, Conversation } from "../utils/Chat";

const MESSAGE = `id conversationId text senderEmail createdAt mine`;
const CONVERSATION = `id unread updatedAt otherReadAt
  other { email username image }
  lastMessage { ${MESSAGE} }`;

export const getConversations = async (page = 0, perPage = 30): Promise<Conversation[]> => {
  const data = await gql(
    `query Conversations($page: Int, $perPage: Int) { conversations(page: $page, perPage: $perPage) { ${CONVERSATION} } }`,
    { page, perPage }
  );
  return data.conversations ?? [];
};

export const getConversation = async (id: string): Promise<Conversation | null> => {
  const data = await gql(`query Conversation($id: String!) { conversation(id: $id) { ${CONVERSATION} } }`, { id });
  return data.conversation ?? null;
};

/** The chat with that user, or null if they never talked. */
export const getConversationWith = async (email: string): Promise<Conversation | null> => {
  const data = await gql(
    `query ConversationWith($email: String!) { conversationWith(email: $email) { ${CONVERSATION} } }`,
    { email }
  );
  return data.conversationWith ?? null;
};

/** Messages older than `before` (newest first). */
export const getMessages = async (conversationId: string, before?: string, limit = 40): Promise<ChatMessage[]> => {
  const data = await gql(
    `query Messages($conversationId: String!, $before: String, $limit: Int) {
      messages(conversationId: $conversationId, before: $before, limit: $limit) { ${MESSAGE} }
    }`,
    { conversationId, before: before ?? null, limit }
  );
  return data.messages ?? [];
};

export const sendMessage = async (
  text: string,
  target: { conversationId?: string | null; toEmail?: string | null }
): Promise<ChatMessage> => {
  const data = await gql(
    `mutation Send($conversationId: String, $toEmail: String, $text: String!) {
      sendMessage(conversationId: $conversationId, toEmail: $toEmail, text: $text) { ${MESSAGE} }
    }`,
    { text, conversationId: target.conversationId ?? null, toEmail: target.toEmail ?? null }
  );
  return data.sendMessage;
};

export const markConversationRead = async (conversationId: string) => {
  await gql(`mutation Read($conversationId: String!) { markConversationRead(conversationId: $conversationId) }`, {
    conversationId,
  });
};

export const sendTyping = async (conversationId: string) => {
  await gql(`mutation Typing($conversationId: String!) { sendTyping(conversationId: $conversationId) }`, {
    conversationId,
  });
};

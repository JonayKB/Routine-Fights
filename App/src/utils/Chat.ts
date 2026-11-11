import { UserSummary } from "./Notification";

export type ChatMessage = {
  id: string;
  conversationId: string;
  text: string;
  senderEmail: string;
  createdAt: string;
  mine: boolean;
  /** Only on the device that sent it, until the API confirms it. */
  pending?: boolean;
  failed?: boolean;
};

export type Conversation = {
  id: string;
  other: UserSummary;
  lastMessage?: ChatMessage | null;
  unread: number;
  updatedAt: string;
  otherReadAt?: string | null;
};

export const MESSAGE_MAX_LENGTH = 2000;

export type UserSummary = {
  email: string;
  username?: string | null;
  image?: string | null;
};

export type NotificationType = "LIKE" | "COMMENT" | "FOLLOW" | "STREAK_RISK" | "MESSAGE";

/** An inbox entry; events of the same kind about the same thing pile up in one. */
export type AppNotification = {
  id: string;
  type: NotificationType;
  targetId?: string | null;
  actors: UserSummary[];
  actorCount: number;
  preview?: string | null;
  extra?: string | null;
  targetImage?: string | null;
  updatedAt: string;
  read: boolean;
};

export const displayName = (user?: UserSummary | null) =>
  user?.username || user?.email?.split("@")[0] || "";

/** API dates come as local "yyyy-MM-ddTHH:mm:ss(.SSS)" without zone. */
export const parseApiDate = (value?: string | null): Date | null => {
  if (!value) return null;
  const date = new Date(value.endsWith("Z") ? value : value.replace(" ", "T"));
  return isNaN(date.getTime()) ? null : date;
};

const sameDay = (a: Date, b: Date) =>
  a.getFullYear() === b.getFullYear() && a.getMonth() === b.getMonth() && a.getDate() === b.getDate();

/** "now", "5 min", "3 h", "2 d", or a date: compact, for lists. */
export const timeAgo = (value: string | null | undefined, t: { now: string; min: string; hour: string; day: string }) => {
  const date = parseApiDate(value);
  if (!date) return "";
  const seconds = Math.max(0, (Date.now() - date.getTime()) / 1000);
  if (seconds < 60) return t.now;
  if (seconds < 3600) return `${Math.floor(seconds / 60)} ${t.min}`;
  if (seconds < 86400) return `${Math.floor(seconds / 3600)} ${t.hour}`;
  if (seconds < 7 * 86400) return `${Math.floor(seconds / 86400)} ${t.day}`;
  return date.toLocaleDateString();
};

/** Clock time of a message. */
export const clockTime = (value?: string | null) => {
  const date = parseApiDate(value);
  if (!date) return "";
  return `${String(date.getHours()).padStart(2, "0")}:${String(date.getMinutes()).padStart(2, "0")}`;
};

/** Label of the day separator in a chat: "Today", "Yesterday" or the date. */
export const dayLabel = (value: string, t: { today: string; yesterday: string }) => {
  const date = parseApiDate(value);
  if (!date) return "";
  const now = new Date();
  if (sameDay(date, now)) return t.today;
  const yesterday = new Date(now);
  yesterday.setDate(now.getDate() - 1);
  if (sameDay(date, yesterday)) return t.yesterday;
  return date.toLocaleDateString(undefined, { day: "numeric", month: "long", year: date.getFullYear() === now.getFullYear() ? undefined : "numeric" });
};

export const isSameDay = (a?: string | null, b?: string | null) => {
  const da = parseApiDate(a);
  const db = parseApiDate(b);
  return !!da && !!db && sameDay(da, db);
};

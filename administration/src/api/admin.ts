import axios from "axios";
import { BASE_URL, gql, readToken } from "./client";

export type ChartData = { labels: string[]; data: number[] };

export type CommunityEvent = {
  id: string;
  name: string;
  image: string;
  startDate: string;
  finishDate: string;
  totalRequired: number;
};

export type Badge = { id: string; image: string; level: number };
export type Activity = { id: string; name: string };
export type LanguageCount = { language: string; devices: number };
export type PushText = { language: string; title: string; body: string };

export const login = async (email: string, password: string): Promise<string> =>
  (
    await gql<{ login: string }>(
      `mutation Login($email: String!, $password: String!) { login(email: $email, password: $password) }`,
      { email, password },
      { auth: false }
    )
  ).login;

export const CHARTS = {
  users: "userCreationChart",
  posts: "postCreationChart",
  points: "postPointsChart",
  topUsers: "userPointsChart",
  rates: "activityTimeRateChart",
} as const;

export const getCharts = async (): Promise<Record<keyof typeof CHARTS, ChartData>> => {
  const fields = Object.values(CHARTS)
    .map((name) => `${name} { labels data }`)
    .join("\n");
  const data = await gql<Record<string, ChartData>>(`query { ${fields} }`);
  const result = {} as Record<keyof typeof CHARTS, ChartData>;
  (Object.keys(CHARTS) as (keyof typeof CHARTS)[]).forEach((key) => {
    result[key] = data[CHARTS[key]] ?? { labels: [], data: [] };
  });
  return result;
};

export const getEvents = async (): Promise<CommunityEvent[]> =>
  (
    await gql<{ getAllCommunityEvents: CommunityEvent[] }>(`query {
      getAllCommunityEvents { id name image startDate finishDate totalRequired }
    }`)
  ).getAllCommunityEvents ?? [];

export const getActivities = async (): Promise<Activity[]> =>
  (await gql<{ findAllActivities: Activity[] }>(`query { findAllActivities { id name } }`)).findAllActivities ?? [];

export const getBadges = async (eventId: string): Promise<Badge[]> =>
  (
    await gql<{ findBadgeByCommunityEvent: Badge[] }>(
      `query Badges($id: String!) { findBadgeByCommunityEvent(communityEventId: $id) { id image level } }`,
      { id: eventId }
    )
  ).findBadgeByCommunityEvent ?? [];

/** Local date-time without zone, as the API stores it ("2026-11-12T00:00:00"). */
const apiDate = (date: Date) => {
  const pad = (n: number) => String(n).padStart(2, "0");
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(
    date.getMinutes()
  )}:${pad(date.getSeconds())}`;
};

export const createEvent = async (input: {
  name: string;
  image: string;
  startDate: Date;
  finishDate: Date;
  totalRequired: number;
  activitiesIDs: string[];
}): Promise<CommunityEvent> =>
  (
    await gql(
      `mutation Create($name: String!, $totalRequired: Int!, $startDate: String!, $finishDate: String!,
                       $image: String!, $activitiesIDs: [String!]!) {
        createCommunityEvent(name: $name, totalRequired: $totalRequired, startDate: $startDate,
          finishDate: $finishDate, image: $image, activitiesIDs: $activitiesIDs) {
          id name image startDate finishDate totalRequired
        }
      }`,
      { ...input, startDate: apiDate(input.startDate), finishDate: apiDate(input.finishDate) }
    )
  ).createCommunityEvent;

export const createBadge = async (eventId: string, image: string, level: number): Promise<Badge> =>
  (
    await gql(
      `mutation Badge($id: String!, $image: String!, $level: Int!) {
        createBadge(communityEventId: $id, image: $image, level: $level) { id image level }
      }`,
      { id: eventId, image, level }
    )
  ).createBadge;

export const uploadImage = async (file: File): Promise<string> => {
  const form = new FormData();
  form.append("file", file, file.name);
  const { data } = await axios.post(BASE_URL + "images/", form, {
    headers: { Authorization: `Bearer ${readToken()}` },
  });
  return data;
};

const imageCache = new Map<string, Promise<string>>();

/** Stored images need the session, so they are fetched and shown as blob URLs. */
export const imageUrl = (name: string, size: "small" | "medium" | "large" = "medium"): Promise<string> => {
  const key = `${name}@${size}`;
  if (!imageCache.has(key)) {
    imageCache.set(
      key,
      axios
        .get(`${BASE_URL}images/${encodeURIComponent(name)}`, {
          params: { size },
          headers: { Authorization: `Bearer ${readToken()}` },
          responseType: "blob",
        })
        .then((r) => URL.createObjectURL(r.data))
        .catch((e) => {
          imageCache.delete(key);
          throw e;
        })
    );
  }
  return imageCache.get(key)!;
};

export const getPushDevices = async (): Promise<LanguageCount[]> =>
  (await gql<{ pushDevices: LanguageCount[] }>(`query { pushDevices { language devices } }`)).pushDevices ?? [];

export const getPushLanguages = async (): Promise<string[]> =>
  (await gql<{ pushLanguages: string[] }>(`query { pushLanguages }`)).pushLanguages ?? [];

/** @returns FCM message ids per language ("es=…; en=…") */
export const broadcast = async (texts: PushText[]): Promise<string> =>
  (
    await gql<{ broadcastNotification: string }>(
      `mutation Broadcast($texts: [PushTextInput!]!) { broadcastNotification(texts: $texts) }`,
      { texts }
    )
  ).broadcastNotification;

/** @returns devices that accepted it */
export const notifyUser = async (email: string, title: string, body: string): Promise<number> =>
  (
    await gql<{ notifyUser: number }>(
      `mutation Notify($email: String!, $title: String!, $body: String) { notifyUser(email: $email, title: $title, body: $body) }`,
      { email, title, body }
    )
  ).notifyUser;

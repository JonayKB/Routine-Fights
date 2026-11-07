import { Event } from "../utils/Event";
import { gql } from "../utils/graphql";

export type RankingEntry = {
  username: string;
  email: string;
  image: string | null;
  points: number;
};

export const getNearestEvent = async (): Promise<Event | null> =>
  (
    await gql(
      `query Nearest { getNearestCommunityEvent { finishDate id image name startDate totalRequired } }`
    )
  ).getNearestCommunityEvent;

export const getCurrentPoints = async (eventId: string): Promise<number> =>
  (
    await gql(
      `query Points($id: String!) { getCommunityEventPointsById(id: $id) }`,
      { id: eventId }
    )
  ).getCommunityEventPointsById ?? 0;

export const getEventRanking = async (
  eventId: string,
  size: number = 10
): Promise<RankingEntry[]> =>
  (
    await gql(
      `query Ranking($id: String!, $limit: Int) {
        communityEventRanking(id: $id, limit: $limit) { username email image points }
      }`,
      { id: eventId, limit: size }
    )
  ).communityEventRanking ?? [];

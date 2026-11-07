import { limit } from "../utils/Utils";
import { Activity, ActivityWithStreak } from "../utils/Activity";
import { gql } from "../utils/graphql";

export const getActivitiesNotSubscribed = async (
  page: number,
  name: string,
  perPage: number = limit
): Promise<Activity[]> =>
  (
    await gql(
      `query NotSubscribed($page: Int!, $perPage: Int, $name: String) {
        paginationActivitiesNotSubscribed(page: $page, perPage: $perPage, activityName: $name) {
          id image name description timeRate timesRequiered
        }
      }`,
      { page, perPage, name: name ?? "" }
    )
  ).paginationActivitiesNotSubscribed;

export const getSubscribedActivities = async (): Promise<ActivityWithStreak[]> =>
  (
    await gql(
      `query Subscribed {
        getSubscribedActivitiesWithStreakByName(activityName: "") {
          id name description image timeRate timesRequiered timesRemaining streak
        }
      }`
    )
  ).getSubscribedActivitiesWithStreakByName;

export const subscribeActivity = async (activityId: string) =>
  (
    await gql(
      `mutation Subscribe($id: String!) { subscribeActivity(activityID: $id) }`,
      { id: activityId }
    )
  ).subscribeActivity;

export const unsubscribeActivity = async (activityId: string) =>
  (
    await gql(
      `mutation Unsubscribe($id: String!) { unSubscribeActivity(activityID: $id) }`,
      { id: activityId }
    )
  ).unSubscribeActivity;

export const createActivity = async (
  name: string,
  description: string,
  timeRate: string,
  timesRequiered: string,
  image: string
): Promise<string> =>
  (
    await gql(
      `mutation CreateActivity($input: ActivityInputV2!) {
        createActivity(activityInput: $input) { id }
      }`,
      { input: { name, description, image, timeRate, timesRequiered } }
    )
  ).createActivity;

/** Days (yyyy-MM-dd) on which the current user posted in the activity. */
export const getMyPostDays = async (
  activityID: string,
  days: number = 84
): Promise<string[]> =>
  (
    await gql(
      `query PostDays($id: String!, $days: Int) { myPostDaysByActivity(activityID: $id, days: $days) }`,
      { id: activityID, days }
    )
  ).myPostDaysByActivity ?? [];

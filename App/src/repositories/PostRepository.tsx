import { limit } from "../utils/Utils";
import { Post } from "../utils/Post";
import { gql } from "../utils/graphql";

const POST_FIELDS = `
      id
      image
      updatedAt
      createdAt
      streak
      comments
      likes
      isLiked
      user {
        email
        image
        username
      }
      activity {
        id
        name
      }
`;

export const getPostsFollowing = async (
  lastDate: string,
  perPage: number = limit
): Promise<Post[]> =>
  (
    await gql(
      `query PostsFollowing($lastDate: String!, $limit: Int) {
        postsFollowingV2(lastDate: $lastDate, limit: $limit) { ${POST_FIELDS} }
      }`,
      { lastDate, limit: perPage }
    )
  ).postsFollowingV2;

export const getPosts = async (
  lastDate: string,
  perPage: number = limit
): Promise<Post[]> =>
  (
    await gql(
      `query Posts($lastDate: String!, $limit: Int) {
        postsV2(lastDate: $lastDate, limit: $limit) { ${POST_FIELDS} }
      }`,
      { lastDate, limit: perPage }
    )
  ).postsV2;

export const getUserPosts = async (
  lastDate: string,
  email: string,
  perPage: number = limit
): Promise<Post[]> =>
  (
    await gql(
      `query UserPosts($lastDate: String!, $email: String!, $limit: Int) {
        postsByUserV2(lastDate: $lastDate, userEmail: $email, limit: $limit) { ${POST_FIELDS} }
      }`,
      { lastDate, email, limit: perPage }
    )
  ).postsByUserV2;

export const getPostBySuscribedActivities = async (
  lastDate: string,
  perPage: number = limit
): Promise<Post[]> =>
  (
    await gql(
      `query PostsSubscribed($lastDate: String!, $limit: Int) {
        postsSubscribedActivitiesV2(lastDate: $lastDate, limit: $limit) { ${POST_FIELDS} }
      }`,
      { lastDate, limit: perPage }
    )
  ).postsSubscribedActivitiesV2;

export const uploadPost = async (activityID: string, image: string) =>
  (
    await gql(
      `mutation UploadPost($activityID: String!, $image: String!) {
        uploadPost(activityID: $activityID, image: $image) { id streak }
      }`,
      { activityID, image }
    )
  ).uploadPost;

export const likePost = async (postID: string) =>
  (
    await gql(`mutation Like($postID: String!) { likePost(postID: $postID) }`, {
      postID,
    })
  ).likePost;

export const unLikePost = async (postID: string) =>
  (
    await gql(
      `mutation Unlike($postID: String!) { unLikePost(postID: $postID) }`,
      { postID }
    )
  ).unLikePost;

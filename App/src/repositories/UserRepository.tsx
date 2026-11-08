import { UserIn, UserOut } from "../utils/User";
import { gql } from "../utils/graphql";

const FOLLOW_FIELDS = `id username nationality image createdAt followers following isFollowing email`;

export const getOwnUser = async (): Promise<UserOut> =>
  (
    await gql(
      `query Me { getOwnUser { id username email nationality phoneNumber image createdAt followers following } }`
    )
  ).getOwnUser;

export const getOwnUserImageAndEmail = async (): Promise<UserOut> =>
  (await gql(`query MeShort { getOwnUser { image email username } }`)).getOwnUser;

export const getUser = async (email: string): Promise<UserOut> =>
  (
    await gql(
      `query User($email: String!) {
        getUserV2IsFollowing(email: $email) {
          id username email phoneNumber image followers following isFollowing
        }
      }`,
      { email }
    )
  ).getUserV2IsFollowing;

export const getFollows = async (email: string, name?: string) =>
  gql(
    `query Follows($email: String!, $name: String!) {
      followersByEmail(email: $email, usernameFilter: $name) { ${FOLLOW_FIELDS} }
      followedByEmail(email: $email, usernameFilter: $name) { ${FOLLOW_FIELDS} }
    }`,
    { email, name: name ?? "" }
  );

export const followUser = async (email: string) =>
  (
    await gql(`mutation Follow($email: String!) { followUser(followingEmail: $email) }`, {
      email,
    })
  ).followUser;

export const unfollowUser = async (email: string) =>
  (
    await gql(
      `mutation Unfollow($email: String!) { unfollowUser(followingEmail: $email) }`,
      { email }
    )
  ).unfollowUser;

/** `language` picks the language of the e-mail sent if the address changes. */
export const updateUser = async (user: UserIn, language: string): Promise<UserOut> =>
  (
    await gql(
      `mutation UpdateUser($user: UserInputV2, $language: String) {
        updateUserV2(user: $user, language: $language) {
          id username email nationality phoneNumber image createdAt followers following
        }
      }`,
      {
        user: {
          id: "",
          username: user.username,
          image: user.image,
          email: user.email,
          nationality: user.nationality,
          phoneNumber: user.phoneNumber,
          password: user.password ? user.password : null,
        },
        language,
      }
    )
  ).updateUserV2;

/** Hottest current streak of a user, used for the flame next to their name. */
export const getBestStreak = async (email: string): Promise<number> =>
  (
    await gql(`query Best($email: String!) { bestStreakByEmail(email: $email) }`, {
      email,
    })
  ).bestStreakByEmail ?? 0;

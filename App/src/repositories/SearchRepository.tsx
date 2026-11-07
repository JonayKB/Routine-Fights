import { limit } from "../utils/Utils";
import { gql } from "../utils/graphql";
import { Followers } from "../utils/User";

export const fetchUsersByName = async (
  pageNum: number,
  text: string,
  perPage: number = limit
): Promise<Followers[]> =>
  (
    await gql(
      `query Users($page: Int!, $perPage: Int!, $name: String!) {
        getUserPaginationByName(page: $page, perPage: $perPage, userName: $name) {
          followers following id image username email isFollowing
        }
      }`,
      { page: pageNum, perPage, name: text ?? "" }
    )
  ).getUserPaginationByName ?? [];

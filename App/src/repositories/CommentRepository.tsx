import { Comment } from "../utils/Comment";
import { gql } from "../utils/graphql";

export const getComments = async (postID: string): Promise<Comment[]> =>
  (
    await gql(
      `query Comments($postID: String!) {
        getComments(postID: $postID) { createdAt id message user { image email username } }
      }`,
      { postID }
    )
  ).getComments ?? [];

export const postComment = async (message: string, postID: string) =>
  (
    await gql(
      `mutation PostComment($input: CommentInputV2!) { postComment(commentInput: $input) { id } }`,
      { input: { message, postID } }
    )
  ).postComment;

import { UserIn } from "../utils/User";
import { gql } from "../utils/graphql";

/** `language` picks the language of the verification e-mail. */
export const register = async (userIn: UserIn, language: string): Promise<void> => {
  await gql(
    `mutation Register($user: RegisterInput!, $language: String) {
      register(user: $user, language: $language) { username email }
    }`,
    { user: userIn, language },
    { auth: false }
  );
};

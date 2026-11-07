import { UserIn } from "../utils/User";
import { gql } from "../utils/graphql";

export const register = async (userIn: UserIn): Promise<void> => {
  await gql(
    `mutation Register($user: RegisterInput!) {
      register(user: $user) { username email }
    }`,
    { user: userIn },
    { auth: false }
  );
};

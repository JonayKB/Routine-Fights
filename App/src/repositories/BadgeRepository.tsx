import { Badge } from "../utils/Badge";
import { gql } from "../utils/graphql";

const BADGE_FIELDS = `
  image
  level
  id
  communityEvent { id name totalRequired finishDate image startDate }
`;

export const getBadgesByEmail = async (userEmail: string): Promise<Badge[]> =>
  (
    await gql(
      `query Badges($email: String!) { getBadgesByEmail(email: $email) { ${BADGE_FIELDS} } }`,
      { email: userEmail }
    )
  ).getBadgesByEmail ?? [];

export const getBadgesByEvent = async (eventId: string): Promise<Badge[]> =>
  (
    await gql(
      `query EventBadges($id: String!) { findBadgeByCommunityEvent(communityEventId: $id) { ${BADGE_FIELDS} } }`,
      { id: eventId }
    )
  ).findBadgeByCommunityEvent ?? [];

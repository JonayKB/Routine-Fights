# Entities (Nodes)

## 1. User
- **id:** Integer (PK)
- **username:** String
- **email:** String
- **password:** String
- **nationality:** String
- **phoneNumber:** String
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 3. Post
- **id:** Integer (PK)
- **image:** String
- **streak:** Integer
- **points:** Integer
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 4. Comment
- **id:** Integer (PK)
- **message:** String
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 6. Report
- **id:** Integer (PK)
- **message:** String
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 8. Badge
- **id:** Integer (PK)
- **icon:** String
- **level:** Integer
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 10. CommunityEvent
- **id:** Integer (PK)
- **name:** String
- **totalRequiered:** Integer
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 12. Activity
- **id:** Integer (PK)
- **icon:** String
- **name:** String
- **description:** String
- **timeRate:** String
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 14. Category
- **id:** Integer (PK)
- **name:** String
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 16. Team
- **id:** Integer (PK)
- **name:** String
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 18. Meeting
- **id:** Integer (PK)
- **date:** Timestamp
- **latitude:** Float (10,6)
- **longitude:** Float (10,6)

# Relationships

- **FOLLOWS**  
  - Between: (User) → (User)  
  - Properties: createdAt, updatedAt, deletedAt

- **POSTED**  
  - Between: (User) → (Post)  
  - Properties: createdAt, updatedAt, deletedAt

- **RELATED_TO**  
  - Between: (Post) → (Activity)  
  - Properties: createdAt, updatedAt, deletedAt

- **COMMENTED**  
  - Between: (User) → (Comment)  
  - Properties: createdAt, updatedAt, deletedAt

- **ON**  
  - Between: (Comment) → (Post)  
  - Properties: createdAt, updatedAt, deletedAt

- **REPLIED_TO**  
  - Between: (Comment) → (Comment)  
  - Properties: createdAt, updatedAt, deletedAt

- **REPORTED**  
  - Between: (User) → (Report)  
  - Properties: createdAt, updatedAt, deletedAt  
  - Complementary: (Report) → (Post)

- **HAS_BADGE**  
  - Between: (User) → (Badge)  
  - Properties: createdAt, updatedAt, deletedAt

- **ASSOCIATED_WITH**  
  - Between: (Badge) → (CommunityEvent)  
  - Properties: createdAt, updatedAt, deletedAt

- **CREATED_ACTIVITY**  
  - Between: (User) → (Activity)  
  - Properties: createdAt, updatedAt, deletedAt

- **BELONGS_TO**  
  - Between: (Activity) → (Category)  
  - Properties: createdAt, updatedAt, deletedAt

- **MEMBER_OF**  
  - Between: (User) → (Team)  
  - Properties: createdAt, updatedAt, deletedAt

- **HAS_MEETING**  
  - Between: (Team) → (Meeting)  
  - Properties: createdAt, updatedAt, deletedAt

- **ATTENDED**  
  - Between: (User) → (Meeting)  
  - Properties: createdAt, updatedAt, deletedAt

- **PART_OF**  
  - Between: (Meeting) → (CommunityEvent)  
  - Properties: createdAt, updatedAt, deletedAt

# Entities (Nodes)

## 1. User
- **id:** String (PK)
- **username:** String
- **email:** String
- **password:** String
- **nationality:** String
- **phoneNumber:** String
- **image:** String
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 3. Post
- **id:** String (PK)
- **image:** String
- **streak:** Integer
- **points:** Integer
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp
- **filedAt:** Timestamp

## 4. Comment
- **id:** String (PK)
- **message:** String
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 6. Report
- **id:** String (PK)
- **message:** String
- **createdAt:** Timestamp

## 8. Badge
- **id:** String (PK)
- **image:** String
- **level:** Integer

## 10. CommunityEvent
- **id:** String (PK)
- **name:** String
- **totalRequiered:** Integer
- **createdAt:** Timestamp

## 12. Activity
- **id:** String (PK)
- **image:** String
- **name:** String
- **description:** String
- **timeRate:** String
- **timesRequired:** String
- **createdAt:** Timestamp
- **updatedAt:** Timestamp
- **deletedAt:** Timestamp

## 14. Category
- **id:** String (PK)
- **name:** String

## 16. Team
- **id:** String (PK)
- **name:** String
- **createdAt:** Timestamp
- **deletedAt:** Timestamp

## 18. Meeting
- **id:** String (PK)
- **date:** Timestamp
- **latitude:** Float (10,6)
- **longitude:** Float (10,6)

# Relationships

- **FOLLOWS**  
  - Between: (User) → (User)  
  - Properties: createdAt, deletedAt

- **POSTED**  
  - Between: (User) → (Post)  

- **RELATED_TO**  
  - Between: (Post) → (Activity)  

- **COMMENTED**  
  - Between: (User) → (Comment)  

- **ON**  
  - Between: (Comment) → (Post)  

- **REPLIED_TO**  
  - Between: (Comment) → (Comment)  
  - Properties: createdAt, deletedAt

- **REPORTED**  
  - Between: (User) → (Report)
  - Complementary: (Report) → (Post)

- **HAS_BADGE**  
  - Between: (User) → (Badge)  

- **ASSOCIATED_WITH**  
  - Between: (Badge) → (CommunityEvent)  

- **CREATED_ACTIVITY**  
  - Between: (User) → (Activity)  

- **BELONGS_TO**  
  - Between: (Activity) → (Category)  

- **MEMBER_OF**  
  - Between: (User) → (Team)  
  - Properties: createdAt, deletedAt

- **HAS_MEETING**  
  - Between: (Team) → (Meeting)  
  - Properties: createdAt, deletedAt

- **ATTENDED**  
  - Between: (User) → (Meeting)  

- **PART_OF**  
  - Between: (Meeting) → (CommunityEvent)
- **RELATED**
-  Between: (CommunityEvent) → (Activity)

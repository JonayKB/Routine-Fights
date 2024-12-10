# Classes

## Index
- [Classes](#classes)
  - [Index](#index)
    - [User](#user)
    - [Follows](#follows)
    - [Post](#post)
    - [Comment](#comment)
    - [Badge](#badge)
    - [User\_Badge](#user_badge)
    - [CommunityEvent](#communityevent)
    - [Activity](#activity)
    - [Category](#category)

### User
1. ID (int) :key:
2. Username (string)
3. Email (string)
4. Password (string)
5. Experience (int)
6. CreatedAt (timestamp)
7. UpdatedAt (timestamp)
8. DeletedAt (timestamp)

### Follows
1. follower_id (int) :paperclip: :key: 
2. following_id (int) :paperclip: :key: 
3. CreatedAt (timestamp)
4. UpdatedAt (timestamp)
5. DeletedAt (timestamp)

### Post
1. ID (int) :key:
2. Image (string)
3. Likes (int)
4. Reports (int)
5. User ID (int) :paperclip:
6. CreatedAt (timestamp)
7. UpdatedAt (timestamp)
8. DeletedAt (timestamp)

### Comment
1. ID (int) :key:
2. Message (string)
3. Likes (int)
4. Reports (int)
5. User ID (int) :paperclip:
6. Post ID (int) :paperclip:
7. CreatedAt (timestamp)
8. UpdatedAt (timestamp)
9. DeletedAt (timestamp)

### Badge
1. ID (int) :key:
2. Icon (blob)
3. Level (int)
4. ComunnityEvent ID (int) :paperclip:
5. CreatedAt (timestamp)
6. UpdatedAt (timestamp)
7. DeletedAt (timestamp)

### User_Badge
1. User ID (int) :paperclip: :key:
2. Badge ID (int) :paperclip: :key:
3. CreatedAt (timestamp)
4. UpdatedAt (timestamp)
5. DeletedAt (timestamp)

### CommunityEvent
1. ID (int) :key:
2. Name (string)
3. CreatedAt (timestamp)
4. UpdatedAt (timestamp)
5. DeletedAt (timestamp)

### Activity
1. ID (int) :key:
2. Icon (blob)
3. Name (string)
4. Description (string)
5. NumberOfTimes (int)
6. TimeRate (string)
7. CreatedBy (int) :paperclip:
8. Category ID (int) :paperclip:
9. CreatedAt (timestamp)
10. UpdatedAt (timestamp)
11. DeletedAt (timestamp)

### Category
1. ID (int) :key:
2. Name (string)
3. CreatedAt (timestamp)
4. UpdatedAt (timestamp)
5. DeletedAt (timestamp)
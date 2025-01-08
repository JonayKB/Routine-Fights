# Classes

## Index
- [Classes](#classes)
  - [Index](#index)
    - [User](#user)
    - [Follows](#follows)
    - [Post](#post)
    - [Report](#report)
    - [Comment](#comment)
    - [Badge](#badge)
    - [User\_Badge](#user_badge)
    - [CommunityEvent](#communityevent)
    - [Activity](#activity)
    - [Category](#category)
    - [Team](#team)
    - [Team\_User](#team_user)
    - [Meeting\_Team](#meeting_team)
    - [Meeting](#meeting)
    - [Meeting\_User](#meeting_user)

### User
1. ID (int) :key:
2. Username (string)
3. Email (string)
4. Password (string)
5. Nacionality (string)
6. PhoneNumber (string)
7. CreatedAt (timestamp)
8. UpdatedAt (timestamp)
9. DeletedAt (timestamp)

### Follows
1. followerID (int) :paperclip: :key: 
2. followingID (int) :paperclip: :key: 
3. CreatedAt (timestamp)
4. UpdatedAt (timestamp)
5. DeletedAt (timestamp)

### Post
1. ID (int) :key:
2. Image (string)
3. Likes (int)
4. Streak (int)
5. PointToAdd (int)
6. UserID (int) :paperclip:
7. ActivityID (int) :paperclip:
8. CreatedAt (timestamp)
9.  UpdatedAt (timestamp)
10. DeletedAt (timestamp)

### Report
1. ID :key:
2. UserID (int) :paperclip:
3. PostID (int) :paperclip:
4. Message (string)

### Comment
1. ID (int) :key:
2. Message (string)
3. Likes (int)
4. ReplyID(int) :paperclip:
5. Reports (int)
6. UserID (int) :paperclip:
7. PostID (int) :paperclip:
8. CreatedAt (timestamp)
9. UpdatedAt (timestamp)
10. DeletedAt (timestamp)

### Badge
1. ID (int) :key:
2. Icon (string)
3. Level (int)
4. ComunnityEventID (int) :paperclip:
5. CreatedAt (timestamp)
6. UpdatedAt (timestamp)
7. DeletedAt (timestamp)

### User_Badge
1. UserID (int) :paperclip: :key:
2. BadgeID (int) :paperclip: :key:
3. CreatedAt (timestamp)
4. UpdatedAt (timestamp)
5. DeletedAt (timestamp)

### CommunityEvent
1. ID (int) :key:
2. Name (string)
3. TotalRequiered (int)
4. CreatedAt (timestamp)
5. UpdatedAt (timestamp)
6. DeletedAt (timestamp)

### Activity
1. ID (int) :key:
2. Icon (string)
3. Name (string)
4. Description (string)
5. TimeRate (string)
6. CreatedBy (int) :paperclip:
7. Category ID (int) :paperclip:
8. CreatedAt (timestamp)
9. UpdatedAt (timestamp)
10. DeletedAt (timestamp)

### Category
1. ID (int) :key:
2. Name (string)
3. CreatedAt (timestamp)
4. UpdatedAt (timestamp)
5. DeletedAt (timestamp)

### Team
1. ID (int) :key:
2. Name (string)
3. CreatedAt (timestamp)
4. UpdatedAt (timestamp)
5. DeletedAt (timestamp)

### Team_User
1. TeamID (int) :paperclip: :key:
2. UserID (int) :paperclip: :key:
3. CreatedAt (timestamp)
4. UpdatedAt (timestamp)
5. DeletedAt (timestamp)

### Meeting_Team
1. TeamID (int) :paperclip: :key:
2. MeetingID (int) :paperclip: :key:

### Meeting
1. ID (int) :key:
2. TeamID (int) :paperclip:
3. Date (timestamp)
4. ComunnityEventID (int) :paperclip:
5. Latitude (float(10,6))
6. Longitude (float(10,6))

### Meeting_User
1. UserID (int) :paperclip: :key:
2. MeetingID (int) :paperclip: :key:
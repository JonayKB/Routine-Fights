<div align="center">
    <h1>Routine-Fights</h1>
    <h2>Jonay Contreras Rodríguez & Rubén Abreu González</h2>
    <img src="./img/logoRoutineFights.png" alt="routine-fights logo" width="400px">
</div>

## Index

- [Index](#index)
- [Introduction](#introduction)
- [Tools](#tools)
- [Usage](#usage)
  - [Using Docker](#using-docker)
  - [Without Docker](#without-docker)
    - [Clone Repository](#clone-repository)
    - [Setup your Neo4j](#setup-your-neo4j)
    - [Start App](#start-app)
- [Endpoints Usage](#endpoints-usage)
  - [GraphQL](#graphql)
    - [Queries](#queries)
    - [Mutations](#mutations)
  - [REST](#rest)
  - [SOAP](#soap)
- [Diagrams](#diagrams)
- [Design](#design)

## Introduction

**Routine Fights** is a productivity-focused social media platform designed to help users enhance their habits, acquire new skills, and engage more consistently in doing routines.

## Tools

| **Database** | **API**         | **CI/CD**        | **FrontEnd APP** | **FrontEnd Administration** |
|--------------|-----------------|------------------|------------------|-----------------------------|
| Neo4J        | Spring          | Github Actions   | React Native     | React                       |
| ![Neo4j logo](https://upload.wikimedia.org/wikipedia/commons/e/e5/Neo4j-logo_color.png) | ![Spring logo](https://cdn.worldvectorlogo.com/logos/spring-3.svg) | ![Github Actions logo](https://cdn-icons-png.flaticon.com/512/25/25231.png) | ![React Native logo](https://static-00.iconduck.com/assets.00/sdk-react-native-icon-512x490-ynyk8t4w.png) | ![React logo](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSlGmKtrnxElpqw3AExKXPWWBulcwjlvDJa1Q&s) |

## Usage

### Using Docker

### Without Docker

#### Clone Repository

```bash
git clone https://github.com/JonayKB/Routine-Fights/
```

#### Setup your Neo4j

Setup your Neo4J or by the client.

Then add the username, url and password on application.properties

#### Start App

```bash
mvn clean spring-boot:run
```

## Endpoints Usage

### GraphQL

#### Queries

| Query                                                            | Description                                                                             |
| ---------------------------------------------------------------- | --------------------------------------------------------------------------------------- |
| `getUserPaginationByName(page, perPage, userName)`               | Returns a paginated list of users whose username matches the filter.                    |
| `getUserV2(email)`                                               | Fetches a single user by email.                                                         |
| `getUserV2IsFollowing(email)`                                    | Fetches a single user by email and includes whether the current user is following them. |
| `followersByEmail(email, usernameFilter)`                        | Lists followers of a user, filtered by follower username.                               |
| `followedByEmail(email, usernameFilter)`                         | Lists users that the given email is following, filtered by username.                    |
| `images`                                                         | Returns a list of image URLs (e.g. for a gallery or assets).                            |
| `postsV2(lastDate, limit)`                                       | Retrieves recent posts before `lastDate`, up to `limit`.                                |
| `postsByUserV2(userID, lastDate, limit)`                         | Retrieves a user’s posts before `lastDate`, up to `limit`.                              |
| `postsByActivityV2(activityID, lastDate, limit)`                 | Retrieves posts for a given activity before `lastDate`, up to `limit`.                  |
| `postsFollowingV2(lastDate, limit)`                              | Retrieves posts from users the current user follows.                                    |
| `postsSubscribedActivitiesV2(lastDate, limit)`                   | Retrieves posts from activities the user is subscribed to.                              |
| `paginationActivitiesNotSubscribed(page, perPage, activityName)` | Paginates activities the user is *not* subscribed to, optionally filtered by name.      |
| `getOwnUser`                                                     | Returns the currently authenticated user.                                               |
| `getSubscribedActivitiesWithStreakByName(activityName)`          | Lists subscribed activities matching the name and includes current streak info.         |
| `getComments(postID)`                                            | Fetches all comments for a given post.                                                  |
| `getCommunityEventById(id)`                                      | Fetches detailed info for one community event (v3).                                     |
| `getUsersParticipatingInCommunityEvent(id)`                      | Lists users participating in a specified community event.                               |
| `getActiveCommunityEvents`                                       | Lists all currently active community events (v2).                                       |
| `getNearestCommunityEvent`                                       | Retrieves the geographically or temporally nearest community event.                     |
| `getCommunityEventPointsById(id)`                                | Returns the total points accumulated for an event by the current user.                  |
| `getBadgesByEmail(email)`                                        | Lists all badges earned by a specified user (v2).                                       |
| `getOwnBadges`                                                   | Lists badges earned by the current user (v2).                                           |
| `findBadgeById(id)`                                              | Fetches a badge by its ID (v3), including event details.                                |
| `findBadgeByCommunityEvent(communityEventId)`                    | Lists all badges associated with a given community event.                               |

#### Mutations

| Mutation                                      | Description                                                                                               |
| --------------------------------------------- | --------------------------------------------------------------------------------------------------------- |
| `saveUserV3(user)`                            | Creates a new user (v3) or returns existing if `id` provided.                                             |
| `updateUserV3(user)`                          | Updates all fields of an existing user (v3).                                                              |
| `deleteUserV3(id)`                            | Soft‐deletes a user (v3).                                                                                 |
| `restoreUserV3(id)`                           | Restores a previously deleted user (v3).                                                                  |
| `updateUserV2(user)`                          | Updates an existing user (v2) by ID.                                                                      |
| `deleteUserV2(id)`                            | Deletes a user (v2) by ID.                                                                                |
| `likePost(postID)`                            | Adds a “like” to the specified post.                                                                      |
| `unLikePost(postID)`                          | Removes the authenticated user’s like from the post.                                                      |
| `followUser(followingEmail)`                  | Authenticated user follows another user by email.                                                         |
| `unfollowUser(followingEmail)`                | Authenticated user unfollows another user.                                                                |
| `subscribeActivity(activityID)`               | Subscribes the authenticated user to an activity.                                                         |
| `unSubscribeActivity(activityID)`             | Unsubscribes the authenticated user from an activity.                                                     |
| `uploadPost(image, activityID)`               | Creates a new post with an image under a specific activity.                                               |
| `createActivity(activityInput)`               | Creates a new activity with metadata (name, description, image, time rate, required times).               |
| `postComment(commentInput)`                   | Posts a new comment or reply under a post.                                                                |
| `createCommunityEvent(...)`                   | Creates a new community event (v3) with name, requirements, date range, image, and associated activities. |
| `createBadge(image, level, communityEventId)` | Creates a new badge tied to a community event (v3).                                                       |
| `addBadgeToUser(userEmail, badgeId)`          | Awards a badge to a single user.                                                                          |
| `addBadgeToUsers(userEmail[], badgeId)`       | Awards a badge to multiple users, returning an array of success flags.                                    |

### REST

### SOAP

## Diagrams

- **Case-Use Diagram**

<img src="#" alt="case-use diagram image">

---

- **Entity-Relation Diagram**

<img src="./documentation/Entity–Relationship Diagram.drawio.png" alt="entity-relation diagram image">

---

- **Database Diagram**

<img src="./documentation/DatabaseDiagram.png" alt="database image">

## Design

<img src="#" alt="design images">

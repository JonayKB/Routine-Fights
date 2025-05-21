package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

@Repository
public interface IUserEntityRepository extends Neo4jRepository<UserEntity, String> {
        public UserEntity findByEmail(String email);

        @Query("MATCH (u:User {email: $email}) RETURN u")
        public UserEntity findByEmailOnlyBase(String email);

        public boolean existsByEmail(String email);

        @Query("MATCH (fr:User {email: $email})-[:FOLLOWS]->(fd: User) WHERE lower(fd.username) CONTAINS lower($usernameFilter) RETURN DISTINCT fd")
        public List<UserEntity> findFollowedUsersByEmail(@Param("email") String email,
                        @Param("usernameFilter") String usernameFilter);

        @Query("MATCH (fr:User)-[:FOLLOWS]->(fd: User {email: $email}) WHERE lower(fr.username) CONTAINS lower($usernameFilter) RETURN DISTINCT fr")
        public List<UserEntity> findFollowersByEmail(@Param("email") String email,
                        @Param("usernameFilter") String usernameFilter);

        @Query("MATCH (fr: User {email: $frEmail}) MATCH (fd: User {email: $fdEmail}) MERGE (fr)-[:FOLLOWS]->(fd) RETURN COUNT(*) > 0")
        public boolean followUser(@Param("frEmail") String frEmail, @Param("fdEmail") String fdEmail);

        @Query("MATCH (fr: User {email: $frEmail})-[r:FOLLOWS]->(fd: User {email: $fdEmail}) DELETE r RETURN COUNT(*) > 0")
        public boolean unfollowUser(@Param("frEmail") String frEmail, @Param("fdEmail") String fdEmail);

        @Query("MATCH (u: User) WHERE u.image IS NOT NULL RETURN u.image")
        public Set<String> findAllImages();

        @Query("MATCH (u: User) WHERE u.username CONTAINS $regex RETURN u")
        public List<UserEntity> findByUsername(@Param("regex") String regex);

        @Query("MATCH (u: User {email:$userMail}) MATCH (a: Activity ) Where elementId(a)=$activityID MERGE (u)-[:Participated]->(a) RETURN exists((u)-[:Participated]->(a)) AS relationshipExists")
        public boolean susbcribeActivity(@Param("userMail") String userEmail, @Param("activityID") String activityID);

        @Query("MATCH (u: User {email:$userMail}) MATCH (a: Activity) WHERE elementId(a) = $activityID MATCH (u)-[r:Participated]->(a) DELETE r RETURN COUNT(*) > 0")
        public boolean unSusbcribeActivity(@Param("userMail") String userEmail, @Param("activityID") String activityID);

        @Query("""
                        MATCH (u:User)
                        WHERE lower(u.username) CONTAINS lower($userName) AND elementId(u) <> $userID
                        CALL {
                          WITH u
                          OPTIONAL MATCH (follower:User)-[ff:FOLLOWS]->(u)
                          RETURN collect(DISTINCT follower) AS followers, collect(DISTINCT ff) AS followerRels
                        }
                        CALL {
                          WITH u
                          OPTIONAL MATCH (u)-[f:FOLLOWS]->(following:User)
                          RETURN collect(DISTINCT following) AS following, collect(DISTINCT f) AS followingRels
                        }
                        RETURN u AS user, followers, following, followerRels AS ff, followingRels AS f

                                    """)
        public List<UserEntity> getPaginationByName(@Param("offset") int offset, @Param("limit") int limit,
                        @Param("userName") String userName, @Param("userID") String userID);

        @Query("MATCH (u: User) MATCH (p: Post) WHERE elementId(p) = $postID AND elementId(u) = $userID MERGE (u)-[:Liked]->(p) RETURN exists((u)-[:Liked]->(p))")
        public Boolean likePost(@Param("userID") String userID, @Param("postID") String postID);

        @Query("MATCH (u: User) MATCH (p: Post) WHERE elementId(p) = $postID AND elementId(u) = $userID MATCH (u)-[r:Liked]->(p) DELETE r RETURN COUNT(*) > 0")
        public Boolean unLikePost(@Param("userID") String userID, @Param("postID") String postID);
}

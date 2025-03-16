package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.repositories;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities.UserEntity;

@Repository
public interface IUserEntityRepository extends Neo4jRepository<UserEntity, String> {
    public UserEntity findByEmail(String email);

    public boolean existsByEmail(String email);

    @Query("MATCH (fr:User {email: $email})-[:FOLLOWS]->(fd: User) RETURN fd")
    public List<UserEntity> findFollowedUsersByEmail(@Param("email") String email);

    @Query("MATCH (fr:User)-[:FOLLOWS]->(fd: User {email: $email}) RETURN fr")
    public List<UserEntity> findFollowersByEmail(@Param("email") String email);

    @Query("MATCH (fr: User {email: $frEmail}) MATCH (fd: User {email: $fdEmail}) MERGE (fr)-[:FOLLOWS]->(fd) RETURN COUNT(*) > 0")
    public boolean followUser(@Param("frEmail") String frEmail, @Param("fdEmail") String fdEmail);

    @Query("MATCH (fr: User {email: $frEmail})-[r:FOLLOWS]->(fd: User {email: $fdEmail}) DELETE r RETURN COUNT(*) > 0")
    public boolean unfollowUser(@Param("frEmail") String frEmail, @Param("fdEmail") String fdEmail);

    @Query("MATCH (u: User) WHERE u.image IS NOT NULL RETURN u.image")
    public List<String> findAllImages();

    @Query("MATCH (u: User) WHERE u.username CONTAINS $regex RETURN u")
    public List<UserEntity> findByUsername(@Param("regex") String regex);

    @Query("MATCH (u: User {email:$userMail}) MATCH (a: Activity ) Where elementId(a)=$activityID MERGE (u)-[:Participated]->(a) RETURN exists((u)-[:Participated]->(a)) AS relationshipExists")
    public boolean susbcribeActivity(@Param("userMail") String userEmail, @Param("activityID") String activityID);

    @Query("MATCH (u: User {email:$userMail}) MATCH (a: Activity) WHERE elementId(a) = $activityID MATCH (u)-[r:Participated]->(a) DELETE r RETURN COUNT(*) > 0")
    public boolean unSusbcribeActivity(@Param("userMail") String userEmail, @Param("activityID") String activityID);
}

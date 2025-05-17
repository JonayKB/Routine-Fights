package es.iespuertodelacruz.routinefights.user.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.activity.infrastructure.adapters.secondary.entities.ActivityEntity;
import es.iespuertodelacruz.routinefights.badge.infrastructure.adapters.secondary.entities.BadgeEntity;
import es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities.CommentEntity;
import es.iespuertodelacruz.routinefights.meeting.infrastructure.adapters.secondary.entities.MeetingEntity;
import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;
import es.iespuertodelacruz.routinefights.report.infrastructure.adapters.secondary.entities.ReportEntity;
import es.iespuertodelacruz.routinefights.team.infrastructure.adapters.secondary.entities.TeamEntity;
import es.iespuertodelacruz.routinefights.user.common.UserCommon;

import java.util.Objects;

/**
 * UserEntity
 */
@Node("User")
public class UserEntity extends UserCommon {
    @Id
    @GeneratedValue
    private String id;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.INCOMING)
    private List<UserEntity> followers;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private List<UserEntity> following;

    @Relationship(type = "Reported", direction = Relationship.Direction.OUTGOING)
    private List<ReportEntity> reports;

    @Relationship(type = "Posted", direction = Relationship.Direction.OUTGOING)
    private List<PostEntity> posts;

    @Relationship(type = "Commented", direction = Relationship.Direction.OUTGOING)
    private List<CommentEntity> comments;

    @Relationship(type = "Liked", direction = Relationship.Direction.OUTGOING)
    private List<PostEntity> likedPosts;

    @Relationship(type = "Attended", direction = Relationship.Direction.OUTGOING)
    private List<MeetingEntity> meetings;

    @Relationship(type = "Created", direction = Relationship.Direction.OUTGOING)
    private List<ActivityEntity> createdActivities;

    @Relationship(type = "Belongs_To", direction = Relationship.Direction.OUTGOING)
    private TeamEntity team;

    @Relationship(type = "Has_Badge", direction = Relationship.Direction.OUTGOING)
    private List<BadgeEntity> badges;

    @Relationship(type = "Participated", direction = Relationship.Direction.OUTGOING)
    private List<ActivityEntity> activities;

    /**
     * Default constructor
     */
    public UserEntity() {
    }

    /**
     * Constructor with all the attributes
     * 
     * @param id                The id of the user
     * @param username          The username of the user
     * @param email             The email of the user
     * @param password          The password of the user
     * @param nationality       The nationality of the user
     * @param phoneNumber       The phone number of the user
     * @param image             The image of the user
     * @param role              The role of the user
     * @param verified          The verification status of the user
     * @param verificationToken The verification token of the user
     * @param createdAt         The date when the user was created
     * @param updatedAt         The date when the user was updated
     * @param deletedAt         The date when the user was deleted
     * @param followers         The list of followers of the user
     * @param following         The list of users that the user is following
     */
    public UserEntity(String id, String username, String email, String password, String nationality, String phoneNumber,
            String image, String role, boolean verified, String verificationToken, LocalDateTime createdAt,
            LocalDateTime updatedAt, LocalDateTime deletedAt, List<UserEntity> followers,
            List<UserEntity> following) {
        super(username, email, password, nationality, phoneNumber, image, role, verified, verificationToken, createdAt,
                updatedAt, deletedAt);
        this.id = id;
        this.followers = followers;
        this.following = following;
    }

    public UserEntity(String id, List<UserEntity> followers, List<UserEntity> following, List<ReportEntity> reports,
            List<PostEntity> posts, List<CommentEntity> comments, List<PostEntity> likedPosts,
            List<MeetingEntity> meetings, List<ActivityEntity> createdActivities, TeamEntity team,
            List<BadgeEntity> badges, List<ActivityEntity> activities) {
        this.id = id;
        this.followers = followers;
        this.following = following;
        this.reports = reports;
        this.posts = posts;
        this.comments = comments;
        this.likedPosts = likedPosts;
        this.meetings = meetings;
        this.createdActivities = createdActivities;
        this.team = team;
        this.badges = badges;
        this.activities = activities;
    }

    public List<ReportEntity> getReports() {
        return this.reports;
    }

    public void setReports(List<ReportEntity> reports) {
        this.reports = reports;
    }

    public List<PostEntity> getPosts() {
        return this.posts;
    }

    public void setPosts(List<PostEntity> posts) {
        this.posts = posts;
    }

    public List<CommentEntity> getComments() {
        return this.comments;
    }

    public void setComments(List<CommentEntity> comments) {
        this.comments = comments;
    }

    public List<PostEntity> getLikedPosts() {
        return this.likedPosts;
    }

    public void setLikedPosts(List<PostEntity> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public List<MeetingEntity> getMeetings() {
        return this.meetings;
    }

    public void setMeetings(List<MeetingEntity> meetings) {
        this.meetings = meetings;
    }

    public List<ActivityEntity> getCreatedActivities() {
        return this.createdActivities;
    }

    public void setCreatedActivities(List<ActivityEntity> createdActivities) {
        this.createdActivities = createdActivities;
    }

    public TeamEntity getTeam() {
        return this.team;
    }

    public void setTeam(TeamEntity team) {
        this.team = team;
    }

    public List<BadgeEntity> getBadges() {
        return this.badges;
    }

    public void setBadges(List<BadgeEntity> badges) {
        this.badges = badges;
    }

    public UserEntity id(String id) {
        setId(id);
        return this;
    }

    public List<ActivityEntity> getActivities() {
        return this.activities;
    }

    public void setActivities(List<ActivityEntity> activities) {
        this.activities = activities;
    }

    public UserEntity activities(List<ActivityEntity> activities) {
        setActivities(activities);
        return this;
    }

    public UserEntity followers(List<UserEntity> followers) {
        setFollowers(followers);
        return this;
    }

    public UserEntity following(List<UserEntity> following) {
        setFollowing(following);
        return this;
    }

    public UserEntity reports(List<ReportEntity> reports) {
        setReports(reports);
        return this;
    }

    public UserEntity posts(List<PostEntity> posts) {
        setPosts(posts);
        return this;
    }

    public UserEntity comments(List<CommentEntity> comments) {
        setComments(comments);
        return this;
    }

    public UserEntity likedPosts(List<PostEntity> likedPosts) {
        setLikedPosts(likedPosts);
        return this;
    }

    public UserEntity meetings(List<MeetingEntity> meetings) {
        setMeetings(meetings);
        return this;
    }

    public UserEntity createdActivities(List<ActivityEntity> createdActivities) {
        setCreatedActivities(createdActivities);
        return this;
    }

    public UserEntity team(TeamEntity team) {
        setTeam(team);
        return this;
    }

    public UserEntity badges(List<BadgeEntity> badges) {
        setBadges(badges);
        return this;
    }

    /**
     * Get the id of the user
     * 
     * @return The id of the user
     */
    public String getId() {
        return this.id;
    }

    /**
     * Set the id of the user
     * 
     * @param id The id of the user
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the list of followers of the user
     * 
     * @return The list of followers of the user
     */
    public List<UserEntity> getFollowers() {
        return this.followers;
    }

    /**
     * Set the list of followers of the user
     * 
     * @param followers The list of followers of the user
     */
    public void setFollowers(List<UserEntity> followers) {
        this.followers = followers;
    }

    /**
     * Get the list of users that the user is following
     * 
     * @return The list of users that the user is following
     */
    public List<UserEntity> getFollowing() {
        return this.following;
    }

    /**
     * Set the list of users that the user is following
     * 
     * @param following The list of users that the user is following
     */
    public void setFollowing(List<UserEntity> following) {
        this.following = following;
    }

    /**
     * Equals method to compare two UserEntity objects
     * 
     * @param o The object to compare
     * @return True if the objects are equals, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof UserEntity)) {
            return false;
        }
        UserEntity userEntity = (UserEntity) o;
        return Objects.equals(id, userEntity.id);
    }

    /**
     * Hash code method
     * 
     * @return The hash code of the object
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * To string method
     * 
     * @return The string representation of the object
     */
    @Override
    public String toString() {
        return "{" +
                " id='" + getId() + "'" +
                ", username='" + getUsername() + "'" +
                ", email='" + getEmail() + "'" +
                ", verified='" + getVerified() + "'" +
                "}";
    }
}

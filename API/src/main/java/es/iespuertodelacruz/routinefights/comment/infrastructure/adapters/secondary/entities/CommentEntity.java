package es.iespuertodelacruz.routinefights.comment.infrastructure.adapters.secondary.entities;

import java.time.LocalDateTime;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import es.iespuertodelacruz.routinefights.post.infrastructure.adapters.secondary.entities.PostEntity;

@Node("Comment")
public class CommentEntity {
    @Id
    @GeneratedValue
    private String id;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    @Relationship(type = "On", direction = Relationship.Direction.OUTGOING)
    private PostEntity post;

}

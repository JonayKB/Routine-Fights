package es.iespuertodelacruz.routinefights.comment.domain.ports.secondary;

import java.util.List;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;

public interface ICommentRepository {
    Comment comment(Comment comment);
    List<Comment> findByPostID(String postID);
    Comment findById(String commentID);
    
}

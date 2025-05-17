package es.iespuertodelacruz.routinefights.comment.domain.ports.primary;


import java.util.List;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.user.domain.User;

public interface ICommentService {

    Comment save(String message, User user, String postID, String replingID);
    List<Comment> findByPostID(String postID);
    
}

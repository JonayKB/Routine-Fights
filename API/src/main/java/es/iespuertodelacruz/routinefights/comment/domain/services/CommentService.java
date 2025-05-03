package es.iespuertodelacruz.routinefights.comment.domain.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import es.iespuertodelacruz.routinefights.comment.domain.Comment;
import es.iespuertodelacruz.routinefights.comment.domain.ports.primary.ICommentService;
import es.iespuertodelacruz.routinefights.comment.domain.ports.secondary.ICommentRepository;
import es.iespuertodelacruz.routinefights.post.domain.Post;
import es.iespuertodelacruz.routinefights.post.domain.ports.secondary.IPostRepository;
import es.iespuertodelacruz.routinefights.user.domain.User;
@Service
public class CommentService implements ICommentService {

    private ICommentRepository commentRepository;
    private IPostRepository postRepository;

    public CommentService(ICommentRepository commentRepository, IPostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public Comment save(String message, User user, String postID, String replingID) {
        Comment comment = new Comment();
        comment.setMessage(message);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(user);
        Post post = postRepository.findById(postID);
        if (post == null) {
            throw new IllegalArgumentException("Post not found");
        }
        comment.setPost(post);
        if (replingID != null) {
            Comment replingComment = commentRepository.findById(replingID);
            if (replingComment == null) {
                throw new IllegalArgumentException("Repling comment not found");
            }
            comment.setReplingComment(replingComment);
        }

        return commentRepository.comment(comment);
    }

    @Override
    public List<Comment> findByPostID(String postID) {
        return commentRepository.findByPostID(postID);
    }
}

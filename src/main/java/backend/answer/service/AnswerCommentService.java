package backend.answer.service;

import backend.answer.model.Answer;
import backend.answer.model.Comment;
import backend.answer.repository.AnswerCommentRepository;
import backend.answer.repository.AnswerRepository;
import backend.common.service.GenericServiceException;
import backend.user.model.User;
import backend.user.repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static backend.answer.model.Comment.hasAnswerId;
import static org.springframework.data.jpa.domain.Specification.where;

@Service
public class AnswerCommentService {
    private final UserRepository userRepository;

    private final AnswerRepository answerRepository;

    private final AnswerCommentRepository answerCommentRepository;

    public AnswerCommentService(UserRepository userRepository, AnswerRepository answerRepository,
                                AnswerCommentRepository answerCommentRepository) {
        this.userRepository = userRepository;
        this.answerRepository = answerRepository;
        this.answerCommentRepository = answerCommentRepository;
    }

    public List<Comment> getComments(Integer answerId) {
        Answer answer = this.answerRepository.findById(answerId).orElseThrow(
                () -> new GenericServiceException(String.format("Answer with id = %d does not exist", answerId)));

        return this.answerCommentRepository.findAll(where(hasAnswerId(answerId)));
    }

    public Comment createComment(Integer answerId, Integer userId, String content) {
        Answer answer = this.answerRepository.findById(answerId).orElseThrow(
                () -> new GenericServiceException(String.format("Answer with id = %d does not exist", answerId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setAnswer(answer);
        comment.setContent(content);
        Timestamp createdAt = Timestamp.from(Instant.now());
        comment.setCreatedAt(createdAt);
        comment.setUpdatedAt(createdAt);

        try {
            this.answerCommentRepository.save(comment);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return comment;
    }

    public Comment editComment(Integer commentId, Integer userId, String content) {
        Comment comment = this.answerCommentRepository.findById(commentId).orElseThrow(
                () -> new GenericServiceException(String.format("Comment with id = %d does not exist", commentId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (!comment.getAuthor().getId().equals(userId) && !user.getIsAdmin()) {
            throw new GenericServiceException("You are not allowed to edit this comment");
        }

        if (content != null) {
            comment.setContent(content);
        }
        comment.setUpdatedAt(Timestamp.from(Instant.now()));

        try {
            this.answerCommentRepository.save(comment);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }

        return comment;
    }

    public void deleteComment(Integer commentId, Integer userId) {
        Comment comment = this.answerCommentRepository.findById(commentId).orElseThrow(
                () -> new GenericServiceException(String.format("Comment with id = %d does not exist", commentId)));

        User user = this.userRepository.findById(userId).orElseThrow(
                () -> new GenericServiceException(String.format("User with id = %d does not exist", userId)));

        if (!comment.getAuthor().getId().equals(userId) && !user.getIsAdmin()) {
            throw new GenericServiceException("You are not allowed to delete this comment");
        }

        // XXX: Mark as deleted ?

        try {
            this.answerCommentRepository.delete(comment);
        } catch (DataAccessException exception) {
            throw new GenericServiceException(exception.getMessage());
        }
    }
}
